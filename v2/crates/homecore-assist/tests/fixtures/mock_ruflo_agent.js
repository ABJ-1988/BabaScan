#!/usr/bin/env node
// Test double for the real ruflo-agent.js MCP-over-stdio process (ADR-133).
// Reads one JSON request per line on stdin, writes one JSON response per
// line on stdout. Used only by homecore-assist's SubprocessRufloRunner tests.
'use strict';

const readline = require('readline');

const rl = readline.createInterface({ input: process.stdin, terminal: false });

rl.on('line', (line) => {
  let req;
  try {
    req = JSON.parse(line);
  } catch (err) {
    process.stdout.write(JSON.stringify({ intent: null, speech: null }) + '\n');
    return;
  }

  const utterance = String(req.utterance || '').toLowerCase();

  if (utterance.includes('dim the lights')) {
    process.stdout.write(
      JSON.stringify({
        intent: {
          name: 'HassLightSet',
          slots: { entity_id: 'light.office', brightness: 64 },
          language: req.language || 'en',
        },
        speech: null,
      }) + '\n'
    );
    return;
  }

  if (utterance.includes('please sleep')) {
    setTimeout(() => {
      process.stdout.write(JSON.stringify({ intent: null, speech: 'woke up' }) + '\n');
    }, 3000);
    return;
  }

  process.stdout.write(
    JSON.stringify({ intent: null, speech: `you said: ${req.utterance || ''}` }) + '\n'
  );
});
