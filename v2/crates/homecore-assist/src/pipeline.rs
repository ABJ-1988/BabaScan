//! AssistPipeline — wires recognizer → handler → response.
//!
//! The pipeline is the public entry point for the HOMECORE-ASSIST subsystem.
//! The HOMECORE-API WebSocket `assist` command will call
//! `pipeline.process(utterance, language, &hc).await`.
//!
//! ## Processing flow
//!
//! 1. Call `recognizer.recognize(utterance, language)`.
//! 2. If no intent matched and a `RufloRunner` is configured, send the
//!    utterance to it for LLM-grade disambiguation (P2).
//! 3. If still no intent → return `IntentResponse::not_understood()`
//!    (or the runner's free-form speech, if it gave one).
//! 4. Look up the handler by intent name.
//! 5. Call `handler.handle(intent, hc)`.
//! 6. Return the `IntentResponse`.

use std::collections::HashMap;
use std::sync::Arc;

use homecore::HomeCore;
use tracing::debug;

use crate::handler::IntentHandler;
use crate::intent::{Intent, IntentResponse};
use crate::recognizer::IntentRecognizer;
use crate::runner::{AssistError, RufloRunner};

/// Boxed type alias so the pipeline can hold heterogeneous handlers.
type BoxedHandler = Arc<dyn IntentHandler>;

/// The main Assist pipeline.
///
/// Construct with `AssistPipeline::new(recognizer)`, register handlers
/// with `register_handler`, then call `process`.
pub struct AssistPipeline<R: IntentRecognizer> {
    recognizer: R,
    handlers: HashMap<String, BoxedHandler>,
    runner: Option<Arc<dyn RufloRunner>>,
}

impl<R: IntentRecognizer> AssistPipeline<R> {
    /// Create a new pipeline with the given recognizer and no handlers.
    pub fn new(recognizer: R) -> Self {
        Self {
            recognizer,
            handlers: HashMap::new(),
            runner: None,
        }
    }

    /// Register an intent handler.  If a handler for the same intent name
    /// was already registered, it is replaced.
    pub fn register_handler<H: IntentHandler>(&mut self, handler: H) {
        self.handlers
            .insert(handler.intent_name().to_owned(), Arc::new(handler));
    }

    /// Configure the P2 ruflo runner used for LLM disambiguation when the
    /// regex recognizer finds no match. Replaces any previously set runner.
    ///
    /// The runner's `spawn`/`shutdown` lifecycle is managed by the caller
    /// (e.g. at server startup / `Ctrl+C`); the pipeline only ever calls
    /// `send_request` on it.
    pub fn set_runner<Run: RufloRunner>(&mut self, runner: Run) {
        self.runner = Some(Arc::new(runner));
    }

    /// Process an utterance through the full pipeline.
    ///
    /// # Errors
    ///
    /// Returns `AssistError` only for unexpected internal failures.
    /// Unknown intents and unrecognised utterances are returned as
    /// `IntentResponse::not_understood()` — not as errors — so the caller
    /// (WebSocket handler) can always synthesise a speech reply.
    pub async fn process(
        &self,
        utterance: &str,
        language: &str,
        hc: &HomeCore,
    ) -> Result<IntentResponse, AssistError> {
        debug!(%utterance, %language, "AssistPipeline: processing utterance");

        let regex_intent = match self.recognizer.recognize(utterance, language).await {
            Ok(i) => i,
            Err(e) => return Err(AssistError::Recognizer(e)),
        };

        let intent = match regex_intent {
            Some(i) => i,
            None => match self.resolve_via_runner(utterance, language).await? {
                RunnerFallback::Intent(i) => i,
                RunnerFallback::Speech(s) => return Ok(IntentResponse::speech_only(s)),
                RunnerFallback::None => {
                    debug!("no intent recognised — returning not_understood");
                    return Ok(IntentResponse::not_understood());
                }
            },
        };

        let name = intent.name.as_str().to_owned();
        let handler = self.handlers.get(&name).cloned();

        match handler {
            Some(h) => h
                .handle(intent, hc)
                .await
                .map_err(AssistError::Handler),
            None => {
                debug!(%name, "no handler registered for intent");
                Ok(IntentResponse::not_understood())
            }
        }
    }

    /// Convenience: count of registered handlers.
    pub fn handler_count(&self) -> usize {
        self.handlers.len()
    }

    /// True if a P2 ruflo runner is configured on this pipeline.
    pub fn has_runner(&self) -> bool {
        self.runner.is_some()
    }

    /// P2 fallback when the regex recognizer finds no match: ask the
    /// configured `RufloRunner` (if any) to resolve the utterance.
    ///
    /// A runner error is logged and treated the same as
    /// `RunnerFallback::None` — a slow or crashed LLM agent must never
    /// turn into a hard pipeline error for the caller.
    async fn resolve_via_runner(
        &self,
        utterance: &str,
        language: &str,
    ) -> Result<RunnerFallback, AssistError> {
        let Some(runner) = &self.runner else {
            return Ok(RunnerFallback::None);
        };

        debug!("regex recognizer found no match — falling through to ruflo runner");
        let payload = serde_json::json!({ "utterance": utterance, "language": language });
        match runner.send_request(payload).await {
            Ok(resp) => match (resp.intent, resp.speech) {
                (Some(i), _) => Ok(RunnerFallback::Intent(i)),
                (None, Some(s)) => Ok(RunnerFallback::Speech(s)),
                (None, None) => Ok(RunnerFallback::None),
            },
            Err(e) => {
                debug!(error = %e, "ruflo runner request failed — falling back to not_understood");
                Ok(RunnerFallback::None)
            }
        }
    }
}

/// Outcome of a P2 `RufloRunner` disambiguation pass.
enum RunnerFallback {
    /// The runner resolved a structured intent — dispatch it normally.
    Intent(Intent),
    /// The runner answered conversationally with no actionable intent.
    Speech(String),
    /// No runner configured, or the runner could not help either.
    None,
}

/// Builder that pre-wires the standard set of built-in HA intent handlers.
///
/// Use this when you want all 5 P1 built-ins registered without listing
/// them individually.
pub fn default_pipeline(
    recognizer: impl IntentRecognizer,
) -> AssistPipeline<impl IntentRecognizer> {
    use crate::handler::{HassCancelAll, HassLightSet, HassNevermind, HassTurnOff, HassTurnOn};
    let mut pipeline = AssistPipeline::new(recognizer);
    pipeline.register_handler(HassTurnOn);
    pipeline.register_handler(HassTurnOff);
    pipeline.register_handler(HassLightSet);
    pipeline.register_handler(HassNevermind);
    pipeline.register_handler(HassCancelAll);
    pipeline
}

#[cfg(test)]
mod tests {
    use homecore::service::FnHandler;
    use homecore::{HomeCore, ServiceName};

    use crate::handler::{HassTurnOff, HassTurnOn};
    use crate::recognizer::RegexIntentRecognizer;

    use super::*;

    async fn build_test_pipeline() -> (AssistPipeline<RegexIntentRecognizer>, HomeCore) {
        let r = RegexIntentRecognizer::new();
        r.register(
            "HassTurnOn",
            r"turn on (?:the )?(?P<entity_id>[a-z_][a-z0-9_ ]*(?:\.[a-z0-9_]+)?)",
            "*",
        )
        .await
        .unwrap();
        r.register(
            "HassTurnOff",
            r"turn off (?:the )?(?P<entity_id>[a-z_][a-z0-9_ ]*(?:\.[a-z0-9_]+)?)",
            "*",
        )
        .await
        .unwrap();
        r.register("HassNevermind", r"never ?mind|cancel that", "*")
            .await
            .unwrap();

        let mut pipeline = AssistPipeline::new(r);
        pipeline.register_handler(HassTurnOn);
        pipeline.register_handler(HassTurnOff);
        pipeline.register_handler(crate::handler::HassNevermind);

        let hc = HomeCore::new();
        // Register spy handlers so service calls don't return NotRegistered.
        hc.services()
            .register(
                ServiceName::new("homeassistant", "turn_on"),
                FnHandler(|_| async { Ok(serde_json::json!({})) }),
            )
            .await;
        hc.services()
            .register(
                ServiceName::new("homeassistant", "turn_off"),
                FnHandler(|_| async { Ok(serde_json::json!({})) }),
            )
            .await;
        (pipeline, hc)
    }

    #[tokio::test]
    async fn pipeline_turn_on_end_to_end() {
        let (pipeline, hc) = build_test_pipeline().await;
        let resp = pipeline
            .process("turn on light.kitchen", "en", &hc)
            .await
            .unwrap();
        assert!(resp.speech.contains("light.kitchen"));
    }

    #[tokio::test]
    async fn pipeline_turn_off_end_to_end() {
        let (pipeline, hc) = build_test_pipeline().await;
        let resp = pipeline
            .process("turn off switch.fan", "en", &hc)
            .await
            .unwrap();
        assert!(resp.speech.to_lowercase().contains("off") || resp.speech.contains("switch.fan"));
    }

    #[tokio::test]
    async fn pipeline_unknown_utterance_returns_not_understood() {
        let (pipeline, hc) = build_test_pipeline().await;
        let resp = pipeline
            .process("what is the weather like", "en", &hc)
            .await
            .unwrap();
        assert!(resp.speech.contains("not sure") || resp.speech.contains("I'm not"));
    }

    #[tokio::test]
    async fn pipeline_recognized_but_no_handler_returns_not_understood() {
        // Register a pattern but NOT its handler.
        let r = RegexIntentRecognizer::new();
        r.register("HassGetState", r"what is (?P<entity_id>\S+)", "*")
            .await
            .unwrap();
        let pipeline = AssistPipeline::new(r);
        let hc = HomeCore::new();
        let resp = pipeline
            .process("what is light.kitchen", "en", &hc)
            .await
            .unwrap();
        assert!(resp.speech.contains("not sure") || resp.speech.contains("I'm not"));
    }

    #[tokio::test]
    async fn default_pipeline_registers_five_handlers() {
        let r = RegexIntentRecognizer::new();
        let pipeline = default_pipeline(r);
        assert_eq!(pipeline.handler_count(), 5);
    }

    #[tokio::test]
    async fn pipeline_nevermind_response() {
        let (pipeline, hc) = build_test_pipeline().await;
        let resp = pipeline
            .process("never mind", "en", &hc)
            .await
            .unwrap();
        assert!(
            resp.speech.to_lowercase().contains("okay")
                || resp.speech.to_lowercase().contains("never")
                || resp.speech.to_lowercase().contains("cancel")
        );
    }

    #[tokio::test]
    async fn pipeline_use_homecore_service_fn_handler() {
        use homecore::service::FnHandler;
        let hc = HomeCore::new();
        hc.services()
            .register(
                ServiceName::new("homeassistant", "turn_on"),
                FnHandler(|_| async { Ok(serde_json::json!({"ok": true})) }),
            )
            .await;
        let r = RegexIntentRecognizer::new();
        r.register(
            "HassTurnOn",
            r"on (?P<entity_id>\S+)",
            "*",
        )
        .await
        .unwrap();
        let mut pipeline = AssistPipeline::new(r);
        pipeline.register_handler(HassTurnOn);
        let resp = pipeline.process("on light.bed", "en", &hc).await.unwrap();
        assert!(resp.speech.contains("light.bed"));
    }

    /// In-process fake `RufloRunner` for pipeline fallback tests — no real
    /// subprocess involved, so it can assert exact pipeline wiring without
    /// depending on `node` being on `$PATH`. `SubprocessRufloRunner` has its
    /// own dedicated tests in `runner.rs`.
    struct MockRunner {
        response: crate::runner::RufloResponse,
    }

    #[async_trait::async_trait]
    impl RufloRunner for MockRunner {
        async fn spawn(&mut self, _opts: crate::runner::RufloRunnerOpts) -> Result<(), AssistError> {
            Ok(())
        }

        async fn send_request(
            &self,
            _payload: serde_json::Value,
        ) -> Result<crate::runner::RufloResponse, AssistError> {
            Ok(self.response.clone())
        }

        async fn shutdown(&mut self) -> Result<(), AssistError> {
            Ok(())
        }
    }

    #[tokio::test]
    async fn pipeline_without_runner_has_runner_is_false() {
        let r = RegexIntentRecognizer::new();
        let pipeline = AssistPipeline::new(r);
        assert!(!pipeline.has_runner());
    }

    #[tokio::test]
    async fn pipeline_falls_through_to_runner_for_unmatched_utterance() {
        use homecore::service::FnHandler;

        let r = RegexIntentRecognizer::new(); // no patterns registered at all
        let mut pipeline = AssistPipeline::new(r);
        pipeline.register_handler(HassTurnOn);
        pipeline.set_runner(MockRunner {
            response: crate::runner::RufloResponse {
                intent: Some(Intent::with_entity("HassTurnOn", "light.office", "en")),
                speech: None,
            },
        });
        assert!(pipeline.has_runner());

        let hc = HomeCore::new();
        hc.services()
            .register(
                ServiceName::new("homeassistant", "turn_on"),
                FnHandler(|_| async { Ok(serde_json::json!({})) }),
            )
            .await;

        let resp = pipeline
            .process("please switch on the office light", "en", &hc)
            .await
            .unwrap();
        assert!(resp.speech.contains("light.office"));
    }

    #[tokio::test]
    async fn pipeline_uses_runner_speech_when_no_intent_resolved() {
        let r = RegexIntentRecognizer::new();
        let mut pipeline = AssistPipeline::new(r);
        pipeline.set_runner(MockRunner {
            response: crate::runner::RufloResponse {
                intent: None,
                speech: Some("It's sunny and 21 degrees.".into()),
            },
        });

        let hc = HomeCore::new();
        let resp = pipeline
            .process("what's the weather like", "en", &hc)
            .await
            .unwrap();
        assert_eq!(resp.speech, "It's sunny and 21 degrees.");
    }

    #[tokio::test]
    async fn pipeline_runner_returning_nothing_falls_back_to_not_understood() {
        let r = RegexIntentRecognizer::new();
        let mut pipeline = AssistPipeline::new(r);
        pipeline.set_runner(MockRunner {
            response: crate::runner::RufloResponse {
                intent: None,
                speech: None,
            },
        });

        let hc = HomeCore::new();
        let resp = pipeline
            .process("asdkjfh nonsense utterance", "en", &hc)
            .await
            .unwrap();
        assert!(resp.speech.contains("not sure"));
    }

    #[tokio::test]
    async fn pipeline_regex_match_skips_runner_entirely() {
        // If the regex recognizer matches, the runner must never be consulted.
        struct PanicIfCalledRunner;

        #[async_trait::async_trait]
        impl RufloRunner for PanicIfCalledRunner {
            async fn spawn(
                &mut self,
                _opts: crate::runner::RufloRunnerOpts,
            ) -> Result<(), AssistError> {
                Ok(())
            }

            async fn send_request(
                &self,
                _payload: serde_json::Value,
            ) -> Result<crate::runner::RufloResponse, AssistError> {
                panic!("runner must not be called when the regex recognizer already matched");
            }

            async fn shutdown(&mut self) -> Result<(), AssistError> {
                Ok(())
            }
        }

        let (mut pipeline, hc) = build_test_pipeline().await;
        pipeline.set_runner(PanicIfCalledRunner);
        let resp = pipeline
            .process("turn on light.kitchen", "en", &hc)
            .await
            .unwrap();
        assert!(resp.speech.contains("light.kitchen"));
    }
}
