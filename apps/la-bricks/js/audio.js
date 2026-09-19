/**
 * LA-BRICKS — Klang
 *
 * Rein prozedurale WebAudio-Synthese, keine Sample-Dateien: das haelt das
 * Paket klein und macht das Spiel offline vollstaendig spielbar.
 *
 * Drei Klangfarben ("KLANG: ..."), analog zu SNAKEone:
 *   LASER — Abwaerts-Sweeps, scharf, Sawtooth
 *   RETRO — Square-Wave-Pieper im Stil frueher Handhelds
 *   PUR   — weiche Sinus-Toene, deutlich zurueckhaltender
 *
 * Der AudioContext darf erst nach einer Nutzergeste starten (Autoplay-Policy),
 * deshalb wird er beim ersten Tippen via unlock() erzeugt.
 */

let ctx = null;
let master = null;
let enabled = true;
let timbre = 'LASER';

/** Pro Frame maximal so viele Stimmen — schuetzt vor Klangbrei bei Multiball. */
const MAX_VOICES = 6;
let voicesThisFrame = 0;

export function configure({ sound, klang }) {
  if (sound !== undefined) enabled = sound !== 'AUS';
  if (klang !== undefined) timbre = klang;
  if (master && ctx) {
    master.gain.setTargetAtTime(enabled ? volumeFor(timbre) : 0, ctx.currentTime, 0.01);
  }
}

function volumeFor(t) {
  if (t === 'PUR') return 0.16;
  if (t === 'RETRO') return 0.22;
  return 0.26;
}

/** Nach der ersten Nutzergeste aufrufen. Mehrfachaufrufe sind unschaedlich. */
export function unlock() {
  if (ctx) {
    if (ctx.state === 'suspended') ctx.resume().catch(() => {});
    return;
  }
  try {
    const AC = window.AudioContext || window.webkitAudioContext;
    if (!AC) return;
    ctx = new AC();
    master = ctx.createGain();
    master.gain.value = enabled ? volumeFor(timbre) : 0;
    master.connect(ctx.destination);
  } catch {
    ctx = null;
  }
}

/** Stimmenzaehler zuruecksetzen — einmal pro gerendertem Frame. */
export function frame() { voicesThisFrame = 0; }

/**
 * Ein Ton mit Huellkurve.
 * @param {object} o
 * @param {number} o.freq      Startfrequenz (Hz)
 * @param {number} [o.to]      Zielfrequenz fuer den Sweep
 * @param {number} o.dur       Dauer (s)
 * @param {OscillatorType} o.type
 * @param {number} [o.gain]    Spitzenpegel 0..1
 */
function tone({ freq, to, dur, type, gain = 0.5 }) {
  if (!ctx || !enabled || voicesThisFrame >= MAX_VOICES) return;
  voicesThisFrame++;

  const t0 = ctx.currentTime;
  const osc = ctx.createOscillator();
  const env = ctx.createGain();

  osc.type = type;
  osc.frequency.setValueAtTime(freq, t0);
  if (to && to !== freq) {
    osc.frequency.exponentialRampToValueAtTime(Math.max(20, to), t0 + dur);
  }

  // Kurzer Anschlag, exponentielles Ausklingen — klickfrei.
  env.gain.setValueAtTime(0.0001, t0);
  env.gain.exponentialRampToValueAtTime(gain, t0 + 0.006);
  env.gain.exponentialRampToValueAtTime(0.0001, t0 + dur);

  osc.connect(env);
  env.connect(master);
  osc.start(t0);
  osc.stop(t0 + dur + 0.02);
}

/** Rauschstoss — fuer Explosionen und den Verlust eines Lebens. */
function noise(dur, gain = 0.3, lowpass = 1200) {
  if (!ctx || !enabled || voicesThisFrame >= MAX_VOICES) return;
  voicesThisFrame++;

  const t0 = ctx.currentTime;
  const frames = Math.max(1, Math.floor(ctx.sampleRate * dur));
  const buf = ctx.createBuffer(1, frames, ctx.sampleRate);
  const data = buf.getChannelData(0);
  for (let i = 0; i < frames; i++) {
    data[i] = (Math.random() * 2 - 1) * (1 - i / frames);
  }

  const src = ctx.createBufferSource();
  src.buffer = buf;

  const filt = ctx.createBiquadFilter();
  filt.type = 'lowpass';
  filt.frequency.setValueAtTime(lowpass, t0);

  const env = ctx.createGain();
  env.gain.setValueAtTime(gain, t0);
  env.gain.exponentialRampToValueAtTime(0.0001, t0 + dur);

  src.connect(filt);
  filt.connect(env);
  env.connect(master);
  src.start(t0);
}

/* -------------------------------------------------------------------------- */
/* Klangfarben-Profile                                                         */
/* -------------------------------------------------------------------------- */

const WAVE = { LASER: 'sawtooth', RETRO: 'square', PUR: 'sine' };

const wave = () => WAVE[timbre] ?? 'sawtooth';

/* -------------------------------------------------------------------------- */
/* Spielereignisse                                                             */
/* -------------------------------------------------------------------------- */

/** Ball trifft Paddle. */
export function sfxPaddle() {
  if (timbre === 'LASER') tone({ freq: 420, to: 190, dur: 0.11, type: 'sawtooth' });
  else if (timbre === 'RETRO') tone({ freq: 330, dur: 0.06, type: 'square', gain: 0.45 });
  else tone({ freq: 300, to: 220, dur: 0.13, type: 'sine', gain: 0.4 });
}

/** Ball trifft Wand. */
export function sfxWall() {
  tone({ freq: 240, to: 200, dur: 0.05, type: wave(), gain: 0.25 });
}

/**
 * Ball trifft Stein — die Tonhoehe steigt mit der Kombo, das gibt dem
 * Kettenabbau ein hoerbares Crescendo.
 * @param {number} combo
 */
export function sfxBrick(combo = 0) {
  const step = Math.min(combo, 12);
  const base = timbre === 'PUR' ? 440 : 520;
  const freq = base * Math.pow(2, step / 12);
  if (timbre === 'LASER') tone({ freq, to: freq * 0.55, dur: 0.09, type: 'sawtooth' });
  else if (timbre === 'RETRO') tone({ freq, dur: 0.055, type: 'square', gain: 0.4 });
  else tone({ freq, dur: 0.1, type: 'sine', gain: 0.35 });
}

/** Stein zerstoert. */
export function sfxDestroy() {
  if (timbre === 'PUR') { tone({ freq: 660, to: 990, dur: 0.12, type: 'sine', gain: 0.3 }); return; }
  noise(0.13, timbre === 'LASER' ? 0.3 : 0.22, 2400);
}

/** Stahlstein — nicht zerstoerbar, dumpfer Aufprall. */
export function sfxSteel() {
  tone({ freq: 140, to: 110, dur: 0.07, type: 'square', gain: 0.3 });
}

/** Power-Up eingesammelt. */
export function sfxPowerup() {
  const t = timbre === 'PUR' ? 'sine' : 'square';
  tone({ freq: 520, dur: 0.07, type: t, gain: 0.35 });
  setTimeout(() => tone({ freq: 780, dur: 0.07, type: t, gain: 0.35 }), 70);
  setTimeout(() => tone({ freq: 1040, dur: 0.11, type: t, gain: 0.3 }), 140);
}

/** Laserschuss. */
export function sfxLaser() {
  tone({ freq: 1400, to: 380, dur: 0.07, type: wave(), gain: 0.22 });
}

/** Leben verloren. */
export function sfxLose() {
  tone({ freq: 300, to: 70, dur: 0.5, type: wave(), gain: 0.4 });
  noise(0.3, 0.2, 700);
}

/** Level geschafft. */
export function sfxLevel() {
  const t = timbre === 'PUR' ? 'sine' : wave();
  [523, 659, 784, 1047].forEach((f, i) => {
    setTimeout(() => tone({ freq: f, dur: 0.16, type: t, gain: 0.32 }), i * 95);
  });
}

/** Spiel vorbei. */
export function sfxGameOver() {
  const t = timbre === 'PUR' ? 'sine' : wave();
  [440, 349, 294, 196].forEach((f, i) => {
    setTimeout(() => tone({ freq: f, dur: 0.3, type: t, gain: 0.34 }), i * 170);
  });
}

/** Extraleben. */
export function sfxExtraLife() {
  const t = timbre === 'PUR' ? 'sine' : 'square';
  [784, 988, 1319].forEach((f, i) => {
    setTimeout(() => tone({ freq: f, dur: 0.2, type: t, gain: 0.3 }), i * 110);
  });
}
