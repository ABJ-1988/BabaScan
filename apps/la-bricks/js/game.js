/**
 * LA-BRICKS — Spiellogik und Zustandsautomat
 *
 * Feste Simulationsschrittweite (STEP) mit Akkumulator: die Physik laeuft
 * auf einem 60-Hz- wie auf einem 144-Hz-Display identisch, und ein
 * Tab-Wechsel erzeugt keinen Sprung durch die halbe Steinwand.
 *
 * Diese Datei kennt weder DOM noch Canvas. Sie meldet Aenderungen ueber das
 * uebergebene ui-Objekt; gezeichnet wird ausschliesslich in js/render.js.
 */

import { PADDLE, BALL, LASER, RULES, STEP, MAX_FRAME_DT, MODES } from './config.js';
import { Paddle, Ball, Shot, buildBricks } from './entities.js';
import { Particles } from './particles.js';
import { getLevel, getDecorLayout } from './levels.js';
import { updateBalls } from './physics.js';
import {
  SLOW_FACTOR, tickEffects, maybeDropPowerup,
  updatePowerups, collect, updateShots,
} from './powerups.js';
import * as audio from './audio.js';
import * as store from './storage.js';

export const STATE = Object.freeze({
  TITLE: 'title',
  PLAYING: 'playing',
  PAUSED: 'paused',
  LEVEL_CLEAR: 'levelclear',
  GAME_OVER: 'gameover',
});

export class Game {
  /**
   * @param {import('./render.js').Renderer} renderer
   * @param {object} ui Rueckmeldungen an die Oberflaeche.
   */
  constructor(renderer, ui) {
    this.renderer = renderer;
    this.ui = ui;

    this.paddle = new Paddle();
    this.particles = new Particles();
    this.balls = [];
    this.bricks = [];
    this.powerups = [];
    this.shots = [];

    this.state = STATE.TITLE;
    this.settings = store.getSettings();
    this.time = 0;
    this.acc = 0;
    this.lastFrame = 0;
    this.keyAxis = 0;
    this.holdFire = false;

    this.reset();
  }

  /* ---------------------------------------------------------------------- */
  /* Lebenszyklus                                                            */
  /* ---------------------------------------------------------------------- */

  reset() {
    this.score = 0;
    this.level = 1;
    this.lives = MODES[this.settings.modus]?.lives ?? RULES.LIVES_START;
    this.combo = 0;
    this.nextExtraLife = RULES.EXTRA_LIFE_EVERY;
    this.destructibleLeft = 0;
    this.effects = { wide: 0, narrow: 0, laser: 0, slow: 0 };
    this.powerups.length = 0;
    this.shots.length = 0;
    this.particles.clear();
    this.paddle.reset();

    // Kulisse fuer den Titelbildschirm. destructibleLeft bleibt 0, was den
    // Levelabschluss ausloesen wuerde — step() laeuft ausserhalb von PLAYING
    // aber nicht, die Steine sind hier reine Dekoration.
    this.bricks = buildBricks(getDecorLayout()).bricks;
    this.spawnBall();
  }

  applySettings(settings) {
    this.settings = settings;
    store.saveSettings(settings);
    audio.configure(settings);
    // Der Modus bestimmt die Startleben — nur ausserhalb einer laufenden
    // Partie uebernehmen, sonst aendert sich mitten im Spiel die Regel.
    if (this.state === STATE.TITLE || this.state === STATE.GAME_OVER) {
      this.lives = MODES[settings.modus]?.lives ?? RULES.LIVES_START;
    }
  }

  start() {
    this.reset();
    this.loadLevel(1);
    this.state = STATE.PLAYING;
    this.ui.onState(this.state, this.snapshot());
    this.pushHud();
  }

  loadLevel(n) {
    this.level = n;
    const { bricks, destructible } = buildBricks(getLevel(n));
    this.bricks = bricks;
    this.destructibleLeft = destructible;

    this.powerups.length = 0;
    this.shots.length = 0;
    this.effects = { wide: 0, narrow: 0, laser: 0, slow: 0 };
    this.paddle.laser = false;
    this.paddle.resize(PADDLE.W);
    this.combo = 0;
    this.spawnBall();
    store.submitProgress(n);
  }

  /** Setzt einen neuen Ball ruhend auf das Paddle. */
  spawnBall() {
    this.balls.length = 0;
    const speed = this.levelSpeed();
    const b = new Ball(this.paddle.x, this.paddle.y - BALL.R - 1, speed);
    b.stuck = true;
    b.stuckOffset = 0;
    this.balls.push(b);
  }

  levelSpeed() {
    const mul = MODES[this.settings.modus]?.speedMul ?? 1;
    return Math.min(
      BALL.SPEED_MAX,
      (BALL.SPEED_START + (this.level - 1) * BALL.SPEED_PER_LEVEL) * mul,
    );
  }

  /* ---------------------------------------------------------------------- */
  /* Eingabe                                                                 */
  /* ---------------------------------------------------------------------- */

  aim(x) {
    if (this.state !== STATE.PLAYING) return;
    this.paddle.aimAt(x);
  }

  /** Tippen: Ball abschiessen, Laser feuern, oder Bildschirm bestaetigen. */
  tap() {
    audio.unlock();

    if (this.state === STATE.PLAYING) {
      const stuck = this.balls.find((b) => b.stuck);
      if (stuck) { this.launch(stuck); return; }
      this.fireLaser();
      return;
    }
    // Andere Zustaende behandelt die Oberflaeche (main.js).
    this.ui.onTapOutsidePlay?.(this.state);
  }

  launch(ball) {
    ball.stuck = false;
    // Leichte Zufallsneigung, damit der Start nicht jedes Mal identisch ist.
    const angle = (Math.random() - 0.5) * 0.5;
    const s = this.levelSpeed();
    ball.vx = Math.sin(angle) * s;
    ball.vy = -Math.cos(angle) * s;
    ball.speed = s;
    audio.sfxPaddle();
  }

  fireLaser() {
    if (!this.paddle.canFire()) return;
    this.shots.push(new Shot(this.paddle.left + 4, this.paddle.y - LASER.H));
    this.shots.push(new Shot(this.paddle.right - 4, this.paddle.y - LASER.H));
    this.paddle.markFired();
    audio.sfxLaser();
    this.vibrate(8);
  }

  togglePause() {
    if (this.state === STATE.PLAYING) {
      this.state = STATE.PAUSED;
    } else if (this.state === STATE.PAUSED) {
      this.state = STATE.PLAYING;
      this.lastFrame = performance.now();
    } else {
      return;
    }
    this.ui.onState(this.state, this.snapshot());
  }

  /* ---------------------------------------------------------------------- */
  /* Hauptschleife                                                           */
  /* ---------------------------------------------------------------------- */

  frame(now) {
    if (!this.lastFrame) this.lastFrame = now;
    let dt = (now - this.lastFrame) / 1000;
    this.lastFrame = now;
    if (dt > MAX_FRAME_DT) dt = MAX_FRAME_DT;

    audio.frame();
    this.time += dt;

    if (this.state === STATE.PLAYING) {
      this.acc += dt;
      let guard = 0;
      while (this.acc >= STEP && guard++ < 240) {
        this.step(STEP);
        this.acc -= STEP;
      }
    } else {
      // Partikel laufen auch auf Titel- und Pausenbildschirm weiter aus.
      this.particles.update(dt);
      this.acc = 0;
    }

    this.renderer.draw(this.snapshot());
  }

  snapshot() {
    return {
      time: this.time,
      state: this.state,
      bricks: this.bricks,
      balls: this.balls,
      paddle: this.paddle,
      powerups: this.powerups,
      shots: this.shots,
      particles: this.particles,
      slowFactor: this.effects.slow > 0 ? SLOW_FACTOR : 1,
      score: this.score,
      level: this.level,
      lives: this.lives,
    };
  }

  /* ---------------------------------------------------------------------- */
  /* Simulationsschritt                                                      */
  /* ---------------------------------------------------------------------- */

  step(dt) {
    // Tastatursteuerung ergaenzt die Zeigereingabe.
    if (this.keyAxis) {
      this.paddle.aimAt(this.paddle.targetX + this.keyAxis * 430 * dt);
    }
    this.paddle.update(dt);
    if (this.holdFire) this.fireLaser();

    tickEffects(this, dt);
    updateBalls(this, dt, this.effects.slow > 0 ? SLOW_FACTOR : 1);
    updatePowerups(this, dt);
    updateShots(this, dt);
    this.particles.update(dt);

    for (const b of this.bricks) {
      if (b.flash > 0) b.flash = Math.max(0, b.flash - dt * 5);
    }

    if (this.destructibleLeft <= 0) this.completeLevel();
  }

  /** Delegat — js/physics.js ruft dies nach jedem zerstoerten Stein auf. */
  maybeDropPowerup(x, y) { maybeDropPowerup(this, x, y); }

  addScore(n) {
    this.score += n;
    if (this.score >= this.nextExtraLife && this.lives < RULES.LIVES_MAX) {
      this.lives++;
      this.nextExtraLife += RULES.EXTRA_LIFE_EVERY;
      audio.sfxExtraLife();
      this.ui.onToast?.('EXTRALEBEN');
    }
    this.pushHud();
  }

  /** Delegat — auch von aussen aufrufbar (Tests, Werkzeuge). */
  collect(type) { collect(this, type); }

  /* ---------------------------------------------------------------------- */
  /* Uebergaenge                                                             */
  /* ---------------------------------------------------------------------- */

  loseLife() {
    this.lives--;
    this.combo = 0;
    this.powerups.length = 0;
    this.shots.length = 0;
    this.effects = { wide: 0, narrow: 0, laser: 0, slow: 0 };
    this.paddle.laser = false;
    this.paddle.resize(PADDLE.W);
    audio.sfxLose();
    this.vibrate(60);

    if (this.lives <= 0) { this.gameOver(); return; }

    this.spawnBall();
    this.pushHud();
  }

  completeLevel() {
    this.state = STATE.LEVEL_CLEAR;
    const bonus = RULES.SCORE_LEVEL_CLEAR + this.lives * RULES.SCORE_LIFE_BONUS;
    this.score += bonus;
    audio.sfxLevel();
    store.submitProgress(this.level + 1);
    this.pushHud();
    this.ui.onState(this.state, { ...this.snapshot(), bonus });
  }

  nextLevel() {
    this.loadLevel(this.level + 1);
    this.state = STATE.PLAYING;
    this.lastFrame = performance.now();
    this.ui.onState(this.state, this.snapshot());
    this.pushHud();
  }

  gameOver() {
    this.state = STATE.GAME_OVER;
    const record = store.submitScore(this.score);
    audio.sfxGameOver();
    this.ui.onState(this.state, {
      ...this.snapshot(),
      record,
      highscore: store.getHighscore(),
    });
  }

  toTitle() {
    this.state = STATE.TITLE;
    this.reset();
    this.ui.onState(this.state, this.snapshot());
  }

  /* ---------------------------------------------------------------------- */
  /* Hilfen                                                                  */
  /* ---------------------------------------------------------------------- */

  pushHud() {
    this.ui.onHud?.({ score: this.score, level: this.level, lives: this.lives });
  }

  /** Haptik gemaess Einstellung "VIBRATION". */
  vibrate(ms) {
    const mode = this.settings.vibration;
    if (mode === 'AUS') return;
    const scale = mode === 'SANFT' ? 0.45 : 1;
    try { navigator.vibrate?.(Math.round(ms * scale)); } catch { /* optional */ }
  }
}
