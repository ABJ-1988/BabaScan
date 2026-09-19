/**
 * LA-BRICKS — Ballphysik und Trefferauswertung
 *
 * Freie Funktionen statt Methoden: sie bekommen den Spielzustand als erstes
 * Argument. Dadurch bleibt js/game.js auf Ablauf und Zustandswechsel
 * beschraenkt, und die Kollisionsrechnung laesst sich einzeln pruefen.
 *
 * Aufgerufen wird ausschliesslich aus dem festen Simulationsschritt in
 * game.step() — die Zeitschrittweite ist hier immer STEP, nie eine
 * Framedauer.
 */

import { FIELD, PADDLE, BALL, RULES } from './config.js';
import { circleRect, clamp } from './entities.js';
import { palette } from './theme.js';
import * as audio from './audio.js';

/**
 * Bewegt alle Baelle einen Schritt weit und wertet Kollisionen aus.
 * Faellt der letzte Ball heraus, wird game.loseLife() ausgeloest.
 *
 * @param {import('./game.js').Game} game
 * @param {number} dt
 * @param {number} slow Faktor auf die Ballgeschwindigkeit (Zeitlupe).
 */
export function updateBalls(game, dt, slow) {
  const p = palette();

  for (let i = game.balls.length - 1; i >= 0; i--) {
    const ball = game.balls[i];

    if (ball.stuck) {
      ball.x = clamp(game.paddle.x + ball.stuckOffset, ball.r, FIELD.W - ball.r);
      ball.y = game.paddle.y - ball.r - 1;
      ball.recordTrail();
      continue;
    }

    ball.x += ball.vx * dt * slow;
    ball.y += ball.vy * dt * slow;
    ball.recordTrail();

    collideWalls(game, ball, p);
    collidePaddle(game, ball);
    collideBricks(game, ball, p);

    // Unten heraus — Ball verloren.
    if (ball.y - ball.r > FIELD.FLOOR) {
      game.balls.splice(i, 1);
      game.particles.burst(ball.x, FIELD.FLOOR, p.red, 14, 0.8);
    }
  }

  if (game.balls.length === 0) game.loseLife();
}

function collideWalls(game, ball, p) {
  let hit = null;

  if (ball.x - ball.r < 0) {
    ball.x = ball.r;
    ball.vx = Math.abs(ball.vx);
    hit = { nx: 1, ny: 0 };
  } else if (ball.x + ball.r > FIELD.W) {
    ball.x = FIELD.W - ball.r;
    ball.vx = -Math.abs(ball.vx);
    hit = { nx: -1, ny: 0 };
  }

  if (ball.y - ball.r < FIELD.TOP) {
    ball.y = FIELD.TOP + ball.r;
    ball.vy = Math.abs(ball.vy);
    hit = { nx: 0, ny: 1 };
  }

  if (!hit) return;

  ball.enforceAngle();
  game.particles.spray(ball.x, ball.y, p.magenta, hit.nx, hit.ny, 5);
  audio.sfxWall();
  // Wandkontakt bricht die Kombo nicht — nur das Paddle tut das.
}

function collidePaddle(game, ball) {
  const pad = game.paddle;
  if (ball.vy <= 0) return;

  const c = circleRect(ball.x, ball.y, ball.r, pad.left, pad.y, pad.w, pad.h);
  if (!c.hit) return;

  // Immer nach oben abprallen: ein seitlicher Treffer soll den Ball nicht
  // unter das Paddle schicken.
  ball.y = pad.y - ball.r - 0.5;

  const off = clamp((ball.x - pad.x) / (pad.w / 2), -1, 1);
  const angle = off * PADDLE.MAX_BOUNCE_ANGLE;
  const speed = Math.hypot(ball.vx, ball.vy) || ball.speed;

  ball.vx = Math.sin(angle) * speed + pad.vx * PADDLE.SPIN;
  ball.vy = -Math.abs(Math.cos(angle) * speed);
  ball.setSpeed(speed);
  ball.enforceAngle();

  game.combo = 0;
  audio.sfxPaddle();
  game.vibrate(10);
}

function collideBricks(game, ball, p) {
  for (const b of game.bricks) {
    if (!b.alive) continue;

    // Billiger Vorabtest, bevor die genaue Pruefung laeuft.
    if (ball.x + ball.r < b.x || ball.x - ball.r > b.x + b.w ||
        ball.y + ball.r < b.y || ball.y - ball.r > b.y + b.h) continue;

    const c = circleRect(ball.x, ball.y, ball.r, b.x, b.y, b.w, b.h);
    if (!c.hit) continue;

    // Aus dem Stein herausschieben, dann spiegeln.
    if (c.axis === 'x') {
      ball.x += (ball.x < b.cx ? -1 : 1) * c.push;
      ball.vx = -ball.vx;
    } else {
      ball.y += (ball.y < b.cy ? -1 : 1) * c.push;
      ball.vy = -ball.vy;
    }
    ball.enforceAngle();

    hitBrick(game, b, ball.x, ball.y, p);
    // Pro Schritt nur ein Stein — sonst prallt der Ball in einer Fuge
    // mehrfach ab und kehrt seine Richtung wieder um.
    return;
  }
}

/**
 * Wertet einen Treffer auf einen Stein aus — von Ball wie von Laserschuss.
 *
 * @param {import('./game.js').Game} game
 * @param {import('./entities.js').Brick} brick
 * @param {number} hx Trefferpunkt, fuer die Funken.
 * @param {number} hy
 * @param {Record<string, any>} p Palette.
 */
export function hitBrick(game, brick, hx, hy, p) {
  const result = brick.damage();

  if (brick.steel) {
    audio.sfxSteel();
    game.particles.burst(hx, hy, p.blueEdge, 5, 0.6);
    game.vibrate(6);
    return;
  }

  if (result === 'hit') {
    game.addScore(RULES.SCORE_PER_HIT);
    audio.sfxBrick(game.combo);
    game.particles.burst(hx, hy, p.gold, 6, 0.7);
    game.vibrate(8);
    return;
  }

  if (result !== 'destroyed') return;

  game.combo++;
  game.destructibleLeft--;
  game.addScore(RULES.SCORE_PER_DESTROY + game.combo * 5);
  audio.sfxDestroy();
  game.particles.burst(brick.cx, brick.cy, p.gold, 16, 1.2);
  game.particles.burst(brick.cx, brick.cy, p.goldCore, 6, 0.6);
  game.vibrate(14);

  // Ball wird mit jedem Stein minimal schneller.
  for (const b of game.balls) {
    b.setSpeed(Math.hypot(b.vx, b.vy) + BALL.SPEED_PER_BRICK);
  }

  game.maybeDropPowerup(brick.cx, brick.cy);
}
