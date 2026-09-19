/**
 * LA-BRICKS — Persistenz
 *
 * localStorage kann in Privatfenstern, bei blockierten Website-Daten oder in
 * eingebetteten WebViews fehlschlagen — jeder Zugriff ist daher gekapselt.
 * Das Spiel bleibt ohne Speicher vollstaendig funktionsfaehig, es merkt sich
 * dann lediglich Highscore und Einstellungen nicht.
 */

import { DEFAULT_SETTINGS, OPTIONS } from './config.js';

const KEY_SCORE = 'labricks.highscore';
const KEY_SETTINGS = 'labricks.settings';
const KEY_PROGRESS = 'labricks.progress';

/** In-Memory-Spiegel, damit die Sitzung auch ohne localStorage konsistent ist. */
const memory = new Map();

function read(key) {
  try {
    const v = localStorage.getItem(key);
    if (v !== null) return v;
  } catch { /* faellt auf den Speicher-Spiegel zurueck */ }
  return memory.has(key) ? memory.get(key) : null;
}

function write(key, value) {
  memory.set(key, value);
  try {
    localStorage.setItem(key, value);
  } catch { /* Sitzung laeuft trotzdem weiter */ }
}

/* -------------------------------------------------------------------------- */
/* Highscore                                                                   */
/* -------------------------------------------------------------------------- */

export function getHighscore() {
  const n = parseInt(read(KEY_SCORE) ?? '0', 10);
  return Number.isFinite(n) && n > 0 ? n : 0;
}

/**
 * Speichert den Punktestand, wenn er den bisherigen Rekord uebertrifft.
 * @returns {boolean} true, wenn ein neuer Rekord gesetzt wurde.
 */
export function submitScore(score) {
  if (!Number.isFinite(score) || score <= getHighscore()) return false;
  write(KEY_SCORE, String(Math.floor(score)));
  return true;
}

/* -------------------------------------------------------------------------- */
/* Einstellungen                                                               */
/* -------------------------------------------------------------------------- */

/**
 * Laedt die Einstellungen und verwirft dabei alles, was nicht in OPTIONS
 * definiert ist — so kann ein alter oder manipulierter Speicherstand das
 * Spiel nicht in einen ungueltigen Zustand bringen.
 */
export function getSettings() {
  const out = { ...DEFAULT_SETTINGS };
  const raw = read(KEY_SETTINGS);
  if (!raw) return out;

  let parsed;
  try {
    parsed = JSON.parse(raw);
  } catch {
    return out;
  }
  if (!parsed || typeof parsed !== 'object') return out;

  for (const [key, spec] of Object.entries(OPTIONS)) {
    const v = parsed[key];
    if (typeof v === 'string' && spec.values.includes(v)) out[key] = v;
  }
  return out;
}

export function saveSettings(settings) {
  write(KEY_SETTINGS, JSON.stringify(settings));
}

/* -------------------------------------------------------------------------- */
/* Fortschritt — hoechstes erreichtes Level                                    */
/* -------------------------------------------------------------------------- */

export function getProgress() {
  const n = parseInt(read(KEY_PROGRESS) ?? '1', 10);
  return Number.isFinite(n) && n > 0 ? n : 1;
}

export function submitProgress(level) {
  if (!Number.isFinite(level) || level <= getProgress()) return;
  write(KEY_PROGRESS, String(Math.floor(level)));
}
