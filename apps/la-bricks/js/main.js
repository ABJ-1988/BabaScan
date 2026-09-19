/**
 * LA-BRICKS — Bootstrap und Oberflaeche
 *
 * Bindet Renderer, Spiel und Eingabe zusammen und haelt als einzige Datei
 * eine DOM-Referenz. Die Spiellogik bleibt dadurch testbar ohne Browser-
 * Oberflaeche.
 */

import { Renderer } from './render.js';
import { Game, STATE } from './game.js';
import { Input } from './input.js';
import { OPTIONS } from './config.js';
import * as audio from './audio.js';
import * as store from './storage.js';

const $ = (sel) => document.querySelector(sel);

const el = {
  frame:   $('#frame'),
  canvas:  $('#board'),
  hud:     $('#hud'),
  score:   $('#hud-score'),
  level:   $('#hud-level'),
  lives:   $('#hud-lives-n'),
  pause:   $('#btn-pause'),
  toast:   $('#toast'),

  screens: {
    [STATE.TITLE]:       $('#screen-title'),
    [STATE.PAUSED]:      $('#screen-pause'),
    [STATE.LEVEL_CLEAR]: $('#screen-level'),
    [STATE.GAME_OVER]:   $('#screen-over'),
  },

  recordN:    $('#record-n'),
  pauseScore: $('#pause-score'),
  levelN:     $('#level-n'),
  levelBonus: $('#level-bonus'),
  levelScore: $('#level-score'),
  overScore:  $('#over-score'),
  overHigh:   $('#over-high'),
  overLevel:  $('#over-level'),
  overRecord: $('#over-record'),
};

const fmt = (n) => String(n).padStart(2, '0');

/* -------------------------------------------------------------------------- */
/* Oberflaechen-Rueckmeldungen fuer das Spiel                                  */
/* -------------------------------------------------------------------------- */

let toastTimer = 0;

const ui = {
  onHud({ score, level, lives }) {
    el.score.textContent = score;
    el.level.textContent = fmt(level);
    el.lives.textContent = lives;
  },

  onState(state, data) {
    for (const [name, node] of Object.entries(el.screens)) {
      if (node) node.hidden = name !== state;
    }
    el.hud.hidden = state === STATE.TITLE;

    if (state === STATE.TITLE) {
      el.recordN.textContent = store.getHighscore();
    }
    if (state === STATE.PAUSED) {
      el.pauseScore.textContent = data.score;
    }
    if (state === STATE.LEVEL_CLEAR) {
      el.levelN.textContent = fmt(data.level);
      el.levelBonus.textContent = data.bonus ?? 0;
      el.levelScore.textContent = data.score;
    }
    if (state === STATE.GAME_OVER) {
      el.overScore.textContent = data.score;
      el.overHigh.textContent = data.highscore ?? store.getHighscore();
      el.overLevel.textContent = fmt(data.level);
      el.overRecord.hidden = !data.record;
    }
  },

  onToast(msg) {
    el.toast.textContent = msg;
    el.toast.classList.remove('show');
    // Reflow erzwingen, damit die Animation auch bei schneller Folge neu startet.
    void el.toast.offsetWidth;
    el.toast.classList.add('show');
    clearTimeout(toastTimer);
    toastTimer = setTimeout(() => el.toast.classList.remove('show'), 1300);
  },

  onTapOutsidePlay(state) {
    if (state === STATE.TITLE) game.start();
    else if (state === STATE.LEVEL_CLEAR) game.nextLevel();
    else if (state === STATE.GAME_OVER) game.start();
  },
};

/* -------------------------------------------------------------------------- */
/* Aufbau                                                                      */
/* -------------------------------------------------------------------------- */

const renderer = new Renderer(el.canvas);
const game = new Game(renderer, ui);

const input = new Input(el.frame, renderer.proj, {
  onAim:   (x) => game.aim(x),
  onTap:   () => game.tap(),
  onPause: () => game.togglePause(),
});

audio.configure(game.settings);
renderSettings();
ui.onState(STATE.TITLE, game.snapshot());
ui.onHud(game.snapshot());

/* -------------------------------------------------------------------------- */
/* Einstellungs-Buttons — "KEY: WERT" durchschalten                            */
/* -------------------------------------------------------------------------- */

function renderSettings() {
  for (const btn of document.querySelectorAll('[data-opt]')) {
    const key = btn.dataset.opt;
    const spec = OPTIONS[key];
    if (!spec) continue;
    btn.querySelector('.k').textContent = `${spec.label}:`;
    btn.querySelector('.v').textContent = game.settings[key];
    btn.setAttribute('aria-label', `${spec.label}: ${game.settings[key]}`);
  }
}

for (const btn of document.querySelectorAll('[data-opt]')) {
  btn.addEventListener('click', (e) => {
    e.stopPropagation();
    audio.unlock();

    const key = btn.dataset.opt;
    const spec = OPTIONS[key];
    if (!spec) return;

    const cur = spec.values.indexOf(game.settings[key]);
    const next = spec.values[(cur + 1) % spec.values.length];

    game.applySettings({ ...game.settings, [key]: next });
    renderSettings();

    // Direkt hoerbare Rueckmeldung beim Umstellen der Klangfarbe.
    if (key === 'klang' || key === 'sound') audio.sfxPowerup();
    if (key === 'vibration') game.vibrate(20);
  });
}

/* -------------------------------------------------------------------------- */
/* Schaltflaechen                                                              */
/* -------------------------------------------------------------------------- */

const click = (sel, fn) => $(sel)?.addEventListener('click', (e) => {
  e.stopPropagation();
  audio.unlock();
  fn();
});

click('#btn-pause',  () => game.togglePause());
click('#btn-resume', () => game.togglePause());
click('#btn-quit',   () => game.toTitle());
click('#btn-again',  () => game.start());
click('#btn-title',  () => game.toTitle());

// Tippen auf den Bildschirmhintergrund (nicht auf einen Button).
for (const node of Object.values(el.screens)) {
  if (!node || !node.dataset.tap) continue;
  node.addEventListener('click', (e) => {
    if (e.target.closest('button')) return;
    audio.unlock();
    ui.onTapOutsidePlay(game.state);
  });
}

/* -------------------------------------------------------------------------- */
/* Fenster- und Sichtbarkeitsereignisse                                        */
/* -------------------------------------------------------------------------- */

let resizeTimer = 0;
function onResize() {
  clearTimeout(resizeTimer);
  resizeTimer = setTimeout(() => renderer.resize(), 80);
}

window.addEventListener('resize', onResize);
window.addEventListener('orientationchange', onResize);

// Wegtippen pausiert — ein Anruf soll nicht das Spiel kosten.
document.addEventListener('visibilitychange', () => {
  if (document.hidden && game.state === STATE.PLAYING) game.togglePause();
});

/* -------------------------------------------------------------------------- */
/* Schleife                                                                    */
/* -------------------------------------------------------------------------- */

function loop(now) {
  game.keyAxis = input.axis();
  game.holdFire = input.held && game.paddle.laser;
  game.frame(now);
  requestAnimationFrame(loop);
}
requestAnimationFrame(loop);

/* -------------------------------------------------------------------------- */
/* Service Worker — Offline-Betrieb                                            */
/* -------------------------------------------------------------------------- */

if ('serviceWorker' in navigator) {
  window.addEventListener('load', () => {
    navigator.serviceWorker.register('sw.js').catch(() => {
      // Offline-Betrieb ist eine Zugabe; das Spiel laeuft auch ohne.
    });
  });
}

// Fuer Tests und manuelle Diagnose in der Konsole.
window.LABricks = { game, renderer, input };
