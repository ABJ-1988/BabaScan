//! HOMECORE-ASSIST — Voice/intent pipeline + ruflo agent bridge.
//!
//! Implements [ADR-133](../../../docs/adr/ADR-133-homecore-assist-ruflo.md):
//! the Assist pipeline that takes a voice utterance through intent
//! recognition, intent handling, and response synthesis.
//!
//! ## Module layout (P1 scaffold)
//!
//! - [`intent`] — `IntentName`, `Intent`, `IntentResponse`, `Card`
//! - [`recognizer`] — `IntentRecognizer` trait + `RegexIntentRecognizer` (P1)
//! - [`handler`] — `IntentHandler` trait + 5 built-in HA-mirroring handlers
//! - [`runner`] — `RufloRunner` trait + `NoopRunner` (P1 stub)
//! - [`pipeline`] — `AssistPipeline`: wires recognizer → handler → response
//!
//! ## P1 scope
//!
//! - Regex-based intent recognition (HA classic intent matching).
//! - Built-in handlers: `HassTurnOn`, `HassTurnOff`, `HassLightSet`,
//!   `HassNevermind`, `HassCancelAll`.
//! - `RufloRunner` trait surface + `NoopRunner` stub.
//!
//! ## P2 scope (this crate)
//!
//! - [`runner::SubprocessRufloRunner`] — real `tokio::process::Child`
//!   subprocess runner for `node ruflo-agent.js`, with the Windows-safe
//!   explicit-shutdown teardown decided in ADR-133 §Q3 (option 2).
//! - `AssistPipeline::set_runner` — when the P1 regex recognizer finds no
//!   match, the pipeline now falls through to the configured `RufloRunner`
//!   for LLM-grade intent disambiguation or a free-form conversational
//!   reply before giving up with "not understood".
//!
//! ## What's NOT here yet (deferred to P3+)
//!
//! - `SemanticIntentRecognizer` using ruvector HNSW embeddings (P2, still a
//!   stub pending the exemplar format decision in ADR-133 §Q4).
//! - STT/TTS bridge and satellite protocol (P3).

pub mod intent;
pub mod recognizer;
pub mod handler;
pub mod runner;
pub mod pipeline;

pub use intent::{Card, Intent, IntentName, IntentResponse};
pub use recognizer::{IntentRecognizer, RecognizerError, RegexIntentRecognizer};
pub use handler::{
    HandlerError, HassCancelAll, HassLightSet, HassNevermind, HassTurnOff, HassTurnOn,
    IntentHandler,
};
pub use runner::{
    AssistError, NoopRunner, RufloResponse, RufloRunner, RufloRunnerOpts, SubprocessRufloRunner,
};
pub use pipeline::AssistPipeline;
