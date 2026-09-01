#!/usr/bin/env bash
# Test double for the real `hermes` CLI's automation contract, used only by
# homecore-assist's HermesCliRunner tests. Mirrors what
# NousResearch/hermes-agent's `hermes --query "<text>" --quiet` does: print
# the plain-text response to stdout, the session id to stderr, exit 0.
set -euo pipefail

query=""
sleep_secs=""
fail=0

while [[ $# -gt 0 ]]; do
  case "$1" in
    --query)
      query="$2"
      shift 2
      ;;
    --quiet)
      shift
      ;;
    --sleep)
      sleep_secs="$2"
      shift 2
      ;;
    --fail)
      fail=1
      shift
      ;;
    *)
      shift
      ;;
  esac
done

if [[ -n "$sleep_secs" ]]; then
  sleep "$sleep_secs"
fi

echo "session_id: fake-session-123" >&2

if [[ "$fail" -eq 1 ]]; then
  echo "boom: simulated hermes failure" >&2
  exit 1
fi

if [[ -n "$query" ]]; then
  echo "hermes says: ${query}"
fi
