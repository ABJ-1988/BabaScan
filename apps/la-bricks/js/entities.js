/**
 * LA-BRICKS — Spielobjekte
 *
 * Alles rechnet in logischen Koordinaten. Kein Objekt kennt den Canvas,
 * die Projektion oder das Theme — gezeichnet wird ausschliesslich in
 * js/render.js.
 */

import { FIELD, PADDLE, BALL, BRICK, BRICK_W, POWERUP, LASER } from './config.js';

/* -------------------------------------------------------------------------- */
/* Paddle                                                                      */
/* -------------------------------------------------------------------------- */

export class Paddle {
  constructor() { this.reset(); }

  reset() {
    this.w = PADDLE.W;
    this.h = PADDLE.H;
    this.y = PADDLE.Y;
    this.x = FIELD.W / 2;      // Mittelpunkt
    this.targetX = this.x;
    this.vx = 0;
    this.laser = false;
    this.cooldown = 0;
  }

  get left()  { return this.x - this.w / 2; }
  get right() { return this.x + this.w / 2; }

  /** Zielposition aus der Eingabe; wird im Feld gehalten. */
  aimAt(x) {
    this.targetX = clamp(x, this.w / 2, FIELD.W - this.w / 2);
  }

  /** Breite aendern, ohne dass das Paddle aus dem Feld rutscht. */
  resize(w) {
    this.w = clamp(w, PADDLE.MIN_W, PADDLE.MAX_W);
    this.x = clamp(this.x, this.w / 2, FIELD.W - this.w / 2);
    this.targetX = clamp(this.targetX, this.w / 2, FIELD.W - this.w / 2);
  }

  update(dt) {
    const prev = this.x;
    // Exponentielle Annaeherung: folgt dem Finger praezise, ohne zu zittern.
    const k = 1 - Math.exp(-26 * dt);
    this.x += (this.targetX - this.x) * k;
    this.x = clamp(this.x, this.w / 2, FIELD.W - this.w / 2);
    this.vx = dt > 0 ? (this.x - prev) / dt : 0;

    if (this.cooldown > 0) this.cooldown -= dt;
  }

  /** @returns {boolean} true, wenn geschossen werden darf. */
  canFire() { return this.laser && this.cooldown <= 0; }

  markFired() { this.cooldown = LASER.COOLDOWN; }
}

/* -------------------------------------------------------------------------- */
/* Ball                                                                        */
/* -------------------------------------------------------------------------- */

export class Ball {
  /**
   * @param {number} x
   * @param {number} y
   * @param {number} speed
   * @param {number} [angle] Radiant; 0 = gerade nach oben.
   */
  constructor(x, y, speed, angle = 0) {
    this.x = x;
    this.y = y;
    this.r = BALL.R;
    this.speed = speed;
    this.vx = Math.sin(angle) * speed;
    this.vy = -Math.cos(angle) * speed;
    /** Liegt noch auf dem Paddle und wartet auf den Abschuss. */
    this.stuck = true;
    /** Versatz zum Paddle-Mittelpunkt, solange stuck. */
    this.stuckOffset = 0;
    /** Spur fuer den Nachzieh-Effekt. */
    this.trail = [];
  }

  /** Geschwindigkeit setzen und Richtung beibehalten. */
  setSpeed(speed) {
    const s = Math.max(60, Math.min(BALL.SPEED_MAX, speed));
    const cur = Math.hypot(this.vx, this.vy) || 1;
    this.vx = (this.vx / cur) * s;
    this.vy = (this.vy / cur) * s;
    this.speed = s;
  }

  /**
   * Verhindert, dass der Ball beinahe waagerecht zwischen den Waenden
   * festhaengt: der vertikale Anteil bekommt eine Untergrenze.
   */
  enforceAngle() {
    const s = Math.hypot(this.vx, this.vy);
    if (s < 1e-6) { this.vy = -this.speed; return; }
    const minVy = s * BALL.MIN_VY_RATIO;
    if (Math.abs(this.vy) < minVy) {
      const sign = this.vy < 0 ? -1 : 1;
      this.vy = sign * minVy;
      const rem = Math.max(0, s * s - this.vy * this.vy);
      this.vx = Math.sign(this.vx || 1) * Math.sqrt(rem);
    }
  }

  recordTrail() {
    this.trail.push(this.x, this.y);
    if (this.trail.length > 16) this.trail.splice(0, 2);
  }
}

/* -------------------------------------------------------------------------- */
/* Steine                                                                      */
/* -------------------------------------------------------------------------- */

export class Brick {
  constructor(col, row, hp, steel) {
    this.col = col;
    this.row = row;
    this.w = BRICK_W;
    this.h = BRICK.H;
    this.x = BRICK.MARGIN_X + col * (BRICK_W + BRICK.GAP);
    this.y = BRICK.TOP + row * (BRICK.H + BRICK.GAP);
    this.hp = hp;
    this.steel = steel;
    this.alive = true;
    /** Zaehlt nach einem Treffer herunter — treibt den Aufblitz-Effekt. */
    this.flash = 0;
  }

  get cx() { return this.x + this.w / 2; }
  get cy() { return this.y + this.h / 2; }

  /** @returns {'none'|'hit'|'destroyed'} */
  damage() {
    if (!this.alive) return 'none';
    this.flash = 1;
    if (this.steel) return 'hit';
    this.hp -= 1;
    if (this.hp <= 0) { this.alive = false; return 'destroyed'; }
    return 'hit';
  }
}

/**
 * Baut das Steinfeld aus einem Levellayout.
 * @param {string[]} layout
 * @returns {{bricks: Brick[], destructible: number}}
 */
export function buildBricks(layout) {
  const bricks = [];
  let destructible = 0;

  layout.forEach((row, r) => {
    for (let c = 0; c < BRICK.COLS; c++) {
      const ch = row[c];
      if (!ch || ch === '.') continue;
      if (ch === 'S') { bricks.push(new Brick(c, r, Infinity, true)); continue; }
      const hp = parseInt(ch, 10);
      if (!Number.isFinite(hp) || hp < 1) continue;
      bricks.push(new Brick(c, r, hp, false));
      destructible++;
    }
  });

  return { bricks, destructible };
}

/* -------------------------------------------------------------------------- */
/* Power-Ups                                                                   */
/* -------------------------------------------------------------------------- */

/**
 * weight steuert die Ziehhaeufigkeit, nicht die Reihenfolge.
 * NARROW ist der einzige Nachteil — er haelt die Sammelentscheidung spannend.
 */
export const POWERUP_TYPES = Object.freeze({
  MULTI:  { code: 'M', label: 'MULTIBALL',    weight: 20, good: true },
  WIDE:   { code: 'W', label: 'BREITES PADDLE', weight: 20, good: true },
  LASER:  { code: 'L', label: 'LASER',        weight: 16, good: true },
  SLOW:   { code: 'S', label: 'ZEITLUPE',     weight: 16, good: true },
  LIFE:   { code: 'X', label: 'EXTRALEBEN',   weight: 6,  good: true },
  NARROW: { code: 'N', label: 'SCHMALES PADDLE', weight: 10, good: false },
});

const TYPE_LIST = Object.values(POWERUP_TYPES);
const TOTAL_WEIGHT = TYPE_LIST.reduce((s, t) => s + t.weight, 0);

/** Gewichteter Zufallstyp. */
export function rollPowerupType() {
  let r = Math.random() * TOTAL_WEIGHT;
  for (const t of TYPE_LIST) {
    r -= t.weight;
    if (r <= 0) return t;
  }
  return TYPE_LIST[0];
}

export class PowerUp {
  constructor(x, y, type) {
    this.x = x;              // Mittelpunkt
    this.y = y;
    this.w = POWERUP.W;
    this.h = POWERUP.H;
    this.type = type;
    this.alive = true;
    this.spin = 0;
  }

  update(dt) {
    this.y += POWERUP.FALL_SPEED * dt;
    this.spin += dt * 3;
    if (this.y - this.h / 2 > FIELD.FLOOR) this.alive = false;
  }

  /** Trifft das Power-Up das Paddle? */
  caughtBy(paddle) {
    return this.x + this.w / 2 > paddle.left
        && this.x - this.w / 2 < paddle.right
        && this.y + this.h / 2 > paddle.y
        && this.y - this.h / 2 < paddle.y + paddle.h;
  }
}

/* -------------------------------------------------------------------------- */
/* Laserschuesse                                                               */
/* -------------------------------------------------------------------------- */

export class Shot {
  constructor(x, y) {
    this.x = x;
    this.y = y;
    this.alive = true;
  }

  update(dt) {
    this.y -= LASER.SPEED * dt;
    if (this.y + LASER.H < FIELD.TOP) this.alive = false;
  }
}

/* -------------------------------------------------------------------------- */
/* Kollision                                                                   */
/* -------------------------------------------------------------------------- */

export function clamp(v, lo, hi) {
  return v < lo ? lo : v > hi ? hi : v;
}

/**
 * Kreis gegen achsenparalleles Rechteck.
 *
 * Gibt zusaetzlich zurueck, welche Achse gespiegelt werden muss: verglichen
 * werden die Eindringtiefen, die kleinere bestimmt die Trennachse. Das ist
 * bei Steinreihen deutlich stabiler als eine Auswertung ueber den Normalen-
 * vektor, weil ein Ball, der in eine Fuge laeuft, sonst zwischen zwei Steinen
 * hin- und herspiegeln kann.
 *
 * @returns {{hit:boolean, axis:'x'|'y', push:number}}
 */
export function circleRect(cx, cy, r, rx, ry, rw, rh) {
  const nearestX = clamp(cx, rx, rx + rw);
  const nearestY = clamp(cy, ry, ry + rh);
  const dx = cx - nearestX;
  const dy = cy - nearestY;

  if (dx * dx + dy * dy > r * r) return { hit: false, axis: 'y', push: 0 };

  const overlapX = (r + rw / 2) - Math.abs(cx - (rx + rw / 2));
  const overlapY = (r + rh / 2) - Math.abs(cy - (ry + rh / 2));

  if (overlapX < overlapY) {
    return { hit: true, axis: 'x', push: overlapX };
  }
  return { hit: true, axis: 'y', push: overlapY };
}

/** Rechteck gegen Rechteck — fuer Laserschuss gegen Stein. */
export function rectRect(ax, ay, aw, ah, bx, by, bw, bh) {
  return ax < bx + bw && ax + aw > bx && ay < by + bh && ay + ah > by;
}
