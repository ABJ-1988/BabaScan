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
//! - [`runner::HermesCliRunner`] — the runner actually usable today:
//!   shells out to an already-installed [Hermes
//!   Agent](https://github.com/NousResearch/hermes-agent) CLI
//!   (`hermes --query "<utterance>" --quiet`) as the LLM thinking layer.
//!   Hermes's scripting contract is one process per query (plain-text
//!   response on stdout), not a persistent stdio server, so `spawn`/
//!   `shutdown` are no-ops here — there is nothing to keep alive.
//! - [`runner::SubprocessRufloRunner`] — real `tokio::process::Child`
//!   subprocess runner for the ADR-133 §1.1 `node ruflo-agent.js` model
//!   (long-lived process, newline-JSON stdio protocol), with the
//!   Windows-safe explicit-shutdown teardown decided in §Q3 (option 2).
//!   No such script ships in this repo; use `HermesCliRunner` unless you
//!   are building a bespoke MCP-over-stdio agent.
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
    AssistError, HermesCliRunner, NoopRunner, RufloResponse, RufloRunner, RufloRunnerOpts,
    SubprocessRufloRunner,
};
pub use pipeline::AssistPipeline;
