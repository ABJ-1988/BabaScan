/**
 * LA-BRICKS — Power-Ups, Zeiteffekte und Laserschuesse
 *
 * Wie js/physics.js freie Funktionen mit dem Spielzustand als erstem
 * Argument. Zusammengefasst ist hier alles, was auf Zeit laeuft oder vom
 * Paddle eingesammelt wird.
 */

import { PADDLE, POWERUP, LASER, RULES, MODES } from './config.js';
import { Ball, PowerUp, rollPowerupType, rectRect } from './entities.js';
import { hitBrick } from './physics.js';
import { palette } from './theme.js';
import * as audio from './audio.js';

/** Obergrenze gleichzeitiger Baelle. */
export const MAX_BALLS = 7;
/** Faktor auf die Ballgeschwindigkeit waehrend "ZEITLUPE". */
export const SLOW_FACTOR = 0.62;

/** Alle befristeten Effekte herunterzaehlen und auslaufen lassen. */
export function tickEffects(game, dt) {
  const e = game.effects;
  let resize = false;

  for (const key of ['wide', 'narrow', 'laser', 'slow']) {
    if (e[key] <= 0) continue;
    e[key] -= dt;
    if (e[key] > 0) continue;
    e[key] = 0;
    if (key === 'wide' || key === 'narrow') resize = true;
    if (key === 'laser') game.paddle.laser = false;
  }

  if (resize) game.paddle.resize(PADDLE.W);
}

/** Nach einem zerstoerten Stein mit Modus-abhaengiger Chance etwas fallen lassen. */
export function maybeDropPowerup(game, x, y) {
  const mul = MODES[game.settings.modus]?.dropMul ?? 1;
  if (Math.random() > POWERUP.DROP_CHANCE * mul) return;
  game.powerups.push(new PowerUp(x, y, rollPowerupType()));
}

export function updatePowerups(game, dt) {
  for (let i = game.powerups.length - 1; i >= 0; i--) {
    const pu = game.powerups[i];
    pu.update(dt);

    if (pu.alive && pu.caughtBy(game.paddle)) {
      pu.alive = false;
      collect(game, pu.type);
    }
    if (!pu.alive) game.powerups.splice(i, 1);
  }
}

/**
 * Wendet ein eingesammeltes Power-Up an.
 * @param {import('./game.js').Game} game
 * @param {{code:string,label:string,good:boolean}} type
 */
export function collect(game, type) {
  const e = game.effects;
  const p = palette();

  switch (type.code) {
    case 'M': splitBalls(game); break;

    case 'W':
      e.narrow = 0;
      e.wide = POWERUP.DURATION;
      game.paddle.resize(PADDLE.MAX_W);
      break;

    case 'N':
      e.wide = 0;
      e.narrow = POWERUP.DURATION;
      game.paddle.resize(PADDLE.MIN_W);
      break;

    case 'L':
      e.laser = POWERUP.DURATION;
      game.paddle.laser = true;
      break;

    case 'S':
      e.slow = POWERUP.DURATION;
      break;

    case 'X':
      if (game.lives < RULES.LIVES_MAX) game.lives++;
      break;
  }

  game.addScore(RULES.SCORE_PER_POWERUP);
  audio.sfxPowerup();
  game.particles.burst(game.paddle.x, game.paddle.y,
                       type.good ? p.gold : p.red, 14, 1);
  game.vibrate(type.good ? 18 : 30);
  game.ui.onToast?.(type.label);
  game.pushHud();
}

/** Jeden fliegenden Ball einmal aufteilen, bis zur Obergrenze. */
function splitBalls(game) {
  const add = [];

  for (const b of game.balls) {
    if (game.balls.length + add.length >= MAX_BALLS) break;
    if (b.stuck) continue;

    const speed = Math.hypot(b.vx, b.vy);
    const a = Math.atan2(b.vy, b.vx);

    for (const d of [-0.42, 0.42]) {
      if (game.balls.length + add.length >= MAX_BALLS) break;
      const nb = new Ball(b.x, b.y, speed);
      nb.stuck = false;
      nb.vx = Math.cos(a + d) * speed;
      nb.vy = Math.sin(a + d) * speed;
      nb.speed = speed;
      nb.enforceAngle();
      add.push(nb);
    }
  }

  game.balls.push(...add);
}

export function updateShots(game, dt) {
  for (let i = game.shots.length - 1; i >= 0; i--) {
    const s = game.shots[i];
    s.update(dt);
    if (!s.alive) { game.shots.splice(i, 1); continue; }

    for (const b of game.bricks) {
      if (!b.alive) continue;
      if (!rectRect(s.x - LASER.W / 2, s.y, LASER.W, LASER.H,
                    b.x, b.y, b.w, b.h)) continue;

      hitBrick(game, b, s.x, s.y, palette());
      s.alive = false;
      game.shots.splice(i, 1);
      break;
    }
  }
}
