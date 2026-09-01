//! RufloRunner trait + NoopRunner (P1 stub).
//!
//! The ruflo agent is a Node.js process that exposes an MCP-over-stdio
//! interface for LLM-grade intent disambiguation. HOMECORE-ASSIST manages
//! a long-lived subprocess via `tokio::process::Child`.
//!
//! ## P1 scope
//!
//! Only the trait + `NoopRunner` stub ship in P1. No subprocess is spawned.
//!
//! ## P2 scope
//!
//! Real subprocess management with Windows-safe teardown per ADR-133 §Q3:
//! - `Child` wrapped in `Arc<Mutex<Option<Child>>>`.
//! - Explicit `async shutdown()` calls `child.kill().await` before drop.
//! - `tokio::signal` handler registered for `Ctrl+C`/`SIGINT` that calls
//!   `shutdown()` before exit.
//! - Windows job object approach (option 3 per Q3) deferred to P3.

use std::process::Stdio;
use std::sync::Arc;
use std::time::Duration;

use async_trait::async_trait;
use serde::{Deserialize, Serialize};
use thiserror::Error;
use tokio::io::{AsyncBufReadExt, AsyncWriteExt, BufReader};
use tokio::process::{Child, ChildStdin, ChildStdout, Command};
use tokio::sync::Mutex;

use crate::intent::Intent;

/// Error type for the assist pipeline (runner + pipeline-level errors).
#[derive(Error, Debug)]
pub enum AssistError {
    #[error("runner not started")]
    NotStarted,
    #[error("runner IO error: {0}")]
    Io(String),
    #[error("runner response parse error: {0}")]
    ParseError(String),
    #[error("recognizer error: {0}")]
    Recognizer(#[from] crate::recognizer::RecognizerError),
    #[error("handler error: {0}")]
    Handler(#[from] crate::handler::HandlerError),
    #[error("no handler registered for intent: {0}")]
    NoHandler(String),
}

/// Configuration for launching the ruflo agent subprocess.
#[derive(Clone, Debug, Serialize, Deserialize)]
pub struct RufloRunnerOpts {
    /// Path to the `ruflo-agent.js` entry point.
    pub script_path: String,
    /// Additional environment variables to pass to the subprocess.
    pub env: std::collections::HashMap<String, String>,
    /// Request timeout in milliseconds (default 5000).
    pub timeout_ms: u64,
}

impl Default for RufloRunnerOpts {
    fn default() -> Self {
        Self {
            script_path: "ruflo-agent.js".into(),
            env: Default::default(),
            timeout_ms: 5000,
        }
    }
}

/// JSON response from the ruflo agent subprocess.
#[derive(Clone, Debug, Serialize, Deserialize)]
pub struct RufloResponse {
    /// Recognised intent, if the LLM resolved one.
    pub intent: Option<Intent>,
    /// Spoken text from the LLM, if any.
    pub speech: Option<String>,
}

/// Trait for the ruflo agent subprocess runner.
///
/// P1 ships only this trait + `NoopRunner`. The real subprocess runner
/// lands in P2 with Windows-safe teardown (ADR-133 §Q3).
#[async_trait]
pub trait RufloRunner: Send + Sync + 'static {
    /// Spawn (or reconnect to) the ruflo agent subprocess.
    async fn spawn(&mut self, opts: RufloRunnerOpts) -> Result<(), AssistError>;

    /// Send an utterance payload to the agent and await a response.
    ///
    /// `payload` is an arbitrary JSON object; at minimum it should include
    /// `{ "utterance": "...", "language": "..." }`.
    async fn send_request(
        &self,
        payload: serde_json::Value,
    ) -> Result<RufloResponse, AssistError>;

    /// Gracefully shut down the subprocess.
    ///
    /// Must be idempotent — calling `shutdown` on an already-stopped runner
    /// must return `Ok(())` rather than an error.
    async fn shutdown(&mut self) -> Result<(), AssistError>;
}

/// P1 no-op implementation. Spawn/send/shutdown are all immediate Ok.
///
/// `send_request` returns an empty `RufloResponse` (no intent, no speech),
/// which causes the pipeline to fall through to the regex recognizer path.
#[derive(Default)]
pub struct NoopRunner {
    started: bool,
}

impl NoopRunner {
    pub fn new() -> Self {
        Self { started: false }
    }
}

#[async_trait]
impl RufloRunner for NoopRunner {
    async fn spawn(&mut self, _opts: RufloRunnerOpts) -> Result<(), AssistError> {
        self.started = true;
        tracing::debug!("NoopRunner: spawn called (P1 stub — no subprocess started)");
        Ok(())
    }

    async fn send_request(
        &self,
        _payload: serde_json::Value,
    ) -> Result<RufloResponse, AssistError> {
        // P1 stub: always returns empty response so the pipeline falls through
        // to the regex recognizer.
        Ok(RufloResponse {
            intent: None,
            speech: None,
        })
    }

    async fn shutdown(&mut self) -> Result<(), AssistError> {
        // Idempotent: Ok whether or not spawn was called.
        self.started = false;
        tracing::debug!("NoopRunner: shutdown called (idempotent no-op in P1)");
        Ok(())
    }
}

/// Live handle to the spawned `node ruflo-agent.js` subprocess, plus the
/// piped stdio ends used for the JSON-lines request/response protocol.
struct ChildState {
    child: Child,
    stdin: ChildStdin,
    stdout: BufReader<ChildStdout>,
    timeout_ms: u64,
}

/// P2 real subprocess runner (ADR-133 §Q3, decision: option 2).
///
/// Manages a long-lived `node <script_path>` child process. Requests and
/// responses are newline-delimited JSON on stdin/stdout: one `send_request`
/// call writes one line to stdin and reads one line back from stdout, so
/// only one request may be in flight at a time (enforced by holding the
/// `Mutex` across the round trip).
///
/// ## Windows subprocess teardown (ADR-133 §Q3)
///
/// `tokio::process::Child` does not send `SIGTERM` on Windows (not a
/// Windows concept) and is not killed automatically on drop. Per the ADR
/// decision, teardown is explicit: call [`RufloRunner::shutdown`], which
/// kills the child and waits for it to exit. `kill_on_drop(true)` is also
/// set on the spawned `Command` as a defence-in-depth safety net, but it is
/// not a substitute for calling `shutdown()` — callers (e.g. the server's
/// `Ctrl+C`/`SIGINT` handler) must call it explicitly before exit.
#[derive(Default)]
pub struct SubprocessRufloRunner {
    inner: Arc<Mutex<Option<ChildState>>>,
}

impl SubprocessRufloRunner {
    pub fn new() -> Self {
        Self::default()
    }

    /// True if a subprocess is currently spawned and tracked.
    pub async fn is_running(&self) -> bool {
        self.inner.lock().await.is_some()
    }
}

#[async_trait]
impl RufloRunner for SubprocessRufloRunner {
    async fn spawn(&mut self, opts: RufloRunnerOpts) -> Result<(), AssistError> {
        let mut guard = self.inner.lock().await;
        if guard.is_some() {
            // Idempotent: already running, treat as a successful reconnect.
            return Ok(());
        }

        let mut child = Command::new("node")
            .arg(&opts.script_path)
            .envs(&opts.env)
            .stdin(Stdio::piped())
            .stdout(Stdio::piped())
            .stderr(Stdio::null())
            .kill_on_drop(true)
            .spawn()
            .map_err(|e| AssistError::Io(format!("failed to spawn ruflo agent: {e}")))?;

        let stdin = child
            .stdin
            .take()
            .ok_or_else(|| AssistError::Io("ruflo agent subprocess has no stdin".into()))?;
        let stdout = child
            .stdout
            .take()
            .ok_or_else(|| AssistError::Io("ruflo agent subprocess has no stdout".into()))?;

        *guard = Some(ChildState {
            child,
            stdin,
            stdout: BufReader::new(stdout),
            timeout_ms: opts.timeout_ms,
        });
        Ok(())
    }

    async fn send_request(
        &self,
        payload: serde_json::Value,
    ) -> Result<RufloResponse, AssistError> {
        let mut guard = self.inner.lock().await;
        let state = guard.as_mut().ok_or(AssistError::NotStarted)?;

        let mut line = serde_json::to_string(&payload)
            .map_err(|e| AssistError::ParseError(e.to_string()))?;
        line.push('\n');

        let timeout_dur = Duration::from_millis(state.timeout_ms);
        let stdin = &mut state.stdin;
        let stdout = &mut state.stdout;

        let round_trip = async {
            stdin
                .write_all(line.as_bytes())
                .await
                .map_err(|e| AssistError::Io(format!("write to ruflo agent failed: {e}")))?;
            stdin
                .flush()
                .await
                .map_err(|e| AssistError::Io(format!("flush to ruflo agent failed: {e}")))?;

            let mut response_line = String::new();
            let n = stdout
                .read_line(&mut response_line)
                .await
                .map_err(|e| AssistError::Io(format!("read from ruflo agent failed: {e}")))?;
            if n == 0 {
                return Err(AssistError::Io(
                    "ruflo agent subprocess closed stdout (EOF)".into(),
                ));
            }
            serde_json::from_str::<RufloResponse>(response_line.trim())
                .map_err(|e| AssistError::ParseError(e.to_string()))
        };

        match tokio::time::timeout(timeout_dur, round_trip).await {
            Ok(result) => result,
            Err(_) => Err(AssistError::Io(format!(
                "ruflo agent request timed out after {}ms",
                state.timeout_ms
            ))),
        }
    }

    async fn shutdown(&mut self) -> Result<(), AssistError> {
        let mut guard = self.inner.lock().await;
        if let Some(mut state) = guard.take() {
            // Dropping stdin first signals EOF so a well-behaved agent can
            // exit cleanly; start_kill()/wait() then force it either way.
            drop(state.stdin);
            let _ = state.child.start_kill();
            let _ = state.child.wait().await;
        }
        Ok(())
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    fn fixture_script(name: &str) -> String {
        format!("{}/tests/fixtures/{name}", env!("CARGO_MANIFEST_DIR"))
    }

    #[tokio::test]
    async fn noop_runner_spawn_returns_ok() {
        let mut runner = NoopRunner::new();
        let result = runner.spawn(RufloRunnerOpts::default()).await;
        assert!(result.is_ok());
    }

    #[tokio::test]
    async fn noop_runner_send_request_returns_empty_response() {
        let runner = NoopRunner::new();
        let resp = runner
            .send_request(serde_json::json!({"utterance": "turn on the light", "language": "en"}))
            .await
            .unwrap();
        assert!(resp.intent.is_none());
        assert!(resp.speech.is_none());
    }

    #[tokio::test]
    async fn noop_runner_shutdown_is_idempotent() {
        let mut runner = NoopRunner::new();
        // First shutdown without spawn — must not error.
        assert!(runner.shutdown().await.is_ok());
        // Spawn then shutdown — must not error.
        runner.spawn(RufloRunnerOpts::default()).await.unwrap();
        assert!(runner.shutdown().await.is_ok());
        // Second shutdown — must still not error.
        assert!(runner.shutdown().await.is_ok());
    }

    fn subprocess_opts(timeout_ms: u64) -> RufloRunnerOpts {
        RufloRunnerOpts {
            script_path: fixture_script("mock_ruflo_agent.js"),
            env: Default::default(),
            timeout_ms,
        }
    }

    #[tokio::test]
    async fn subprocess_runner_send_before_spawn_errors() {
        let runner = SubprocessRufloRunner::new();
        let err = runner
            .send_request(serde_json::json!({"utterance": "hello"}))
            .await
            .unwrap_err();
        assert!(matches!(err, AssistError::NotStarted));
    }

    #[tokio::test]
    async fn subprocess_runner_round_trip_speech() {
        let mut runner = SubprocessRufloRunner::new();
        runner.spawn(subprocess_opts(5000)).await.unwrap();
        assert!(runner.is_running().await);

        let resp = runner
            .send_request(serde_json::json!({"utterance": "hello there", "language": "en"}))
            .await
            .unwrap();
        assert_eq!(resp.speech.as_deref(), Some("you said: hello there"));
        assert!(resp.intent.is_none());

        runner.shutdown().await.unwrap();
        assert!(!runner.is_running().await);
    }

    #[tokio::test]
    async fn subprocess_runner_returns_resolved_intent() {
        let mut runner = SubprocessRufloRunner::new();
        runner.spawn(subprocess_opts(5000)).await.unwrap();

        let resp = runner
            .send_request(serde_json::json!({"utterance": "please dim the lights", "language": "en"}))
            .await
            .unwrap();
        let intent = resp.intent.expect("agent should resolve an intent");
        assert_eq!(intent.name.as_str(), "HassLightSet");
        assert_eq!(intent.entity_id(), Some("light.office"));

        runner.shutdown().await.unwrap();
    }

    #[tokio::test]
    async fn subprocess_runner_multiple_requests_reuse_process() {
        let mut runner = SubprocessRufloRunner::new();
        runner.spawn(subprocess_opts(5000)).await.unwrap();

        let first = runner
            .send_request(serde_json::json!({"utterance": "one"}))
            .await
            .unwrap();
        let second = runner
            .send_request(serde_json::json!({"utterance": "two"}))
            .await
            .unwrap();
        assert_eq!(first.speech.as_deref(), Some("you said: one"));
        assert_eq!(second.speech.as_deref(), Some("you said: two"));

        runner.shutdown().await.unwrap();
    }

    #[tokio::test]
    async fn subprocess_runner_times_out_on_slow_agent() {
        let mut runner = SubprocessRufloRunner::new();
        runner.spawn(subprocess_opts(200)).await.unwrap();

        let result = runner
            .send_request(serde_json::json!({"utterance": "please sleep now"}))
            .await;
        assert!(result.is_err());
        assert!(matches!(result.unwrap_err(), AssistError::Io(_)));

        runner.shutdown().await.unwrap();
    }

    #[tokio::test]
    async fn subprocess_runner_spawn_is_idempotent() {
        let mut runner = SubprocessRufloRunner::new();
        runner.spawn(subprocess_opts(5000)).await.unwrap();
        // Second spawn while already running must not error or replace the child.
        runner.spawn(subprocess_opts(5000)).await.unwrap();
        assert!(runner.is_running().await);
        runner.shutdown().await.unwrap();
    }

    #[tokio::test]
    async fn subprocess_runner_shutdown_is_idempotent() {
        let mut runner = SubprocessRufloRunner::new();
        assert!(runner.shutdown().await.is_ok());
        runner.spawn(subprocess_opts(5000)).await.unwrap();
        assert!(runner.shutdown().await.is_ok());
        assert!(runner.shutdown().await.is_ok());
    }
}
