/**
 * LA-BRICKS — Canvas-Renderer
 *
 * Setzt den CRT-Neon-Arcade-Look von SNAKEone um: perspektivisch gekipptes
 * Grid, klobiger Magenta-Blockrahmen mit blauem Kantenlicht, durchgaengiger
 * Bloom, goldene Funken.
 *
 * Farben kommen ausnahmslos aus js/theme.js (und damit aus css/theme.css).
 * In dieser Datei stehen bewusst keine literalen Farbwerte.
 */

import { FIELD, BRICK, PADDLE, BALL, LASER, POWERUP } from './config.js';
import { Projection } from './projection.js';
import { palette, brickColor, glow, noGlow } from './theme.js';
import { POWERUP_TYPES } from './entities.js';

/** Wandstaerke des Blockrahmens in logischen Pixeln. */
const WALL = 15;
/** Laenge eines einzelnen Rahmenblocks. */
const BLOCK = 26;
const BLOCK_GAP = 3;

/** Rasterweite des Hintergrundgitters. */
const GRID = 40;

export class Renderer {
  /** @param {HTMLCanvasElement} canvas */
  constructor(canvas) {
    this.canvas = canvas;
    this.ctx = canvas.getContext('2d', { alpha: false });
    this.proj = new Projection(FIELD.W, FIELD.H);
    this.dpr = 1;
    this.w = 0;
    this.h = 0;
    this._pt = { x: 0, y: 0 };
    this.resize();
  }

  /** An Containergroesse und Pixeldichte anpassen. */
  resize() {
    const p = palette(true);
    const rect = this.canvas.getBoundingClientRect();
    // Auf sehr dichten Displays bringt >2 kaum noch etwas, kostet aber Fuellrate.
    this.dpr = Math.min(window.devicePixelRatio || 1, 2);

    this.w = Math.max(1, Math.round(rect.width));
    this.h = Math.max(1, Math.round(rect.height));
    this.canvas.width = Math.round(this.w * this.dpr);
    this.canvas.height = Math.round(this.h * this.dpr);

    // Der Blockrahmen liegt ausserhalb des logischen Feldes — er wird als
    // Pflichtbereich mitgegeben, damit die Projektion ihn nicht abschneidet.
    this.proj.resize(this.w, this.h, p.tilt, p.rotate, p.horizon, {
      x0: -WALL,
      y0: FIELD.TOP - 2 * WALL,
      x1: FIELD.W + WALL,
      y1: FIELD.H,
    });
  }

  /** Logische Position -> Bildschirm (Convenience, wiederverwendeter Puffer). */
  pt(x, y) { return this.proj.project(x, y, this._pt); }

  /* ---------------------------------------------------------------------- */
  /* Hauptschleife                                                           */
  /* ---------------------------------------------------------------------- */

  /**
   * @param {object} s Spielzustand aus js/game.js
   */
  draw(s) {
    const ctx = this.ctx;
    const p = palette();

    ctx.save();
    ctx.scale(this.dpr, this.dpr);

    ctx.fillStyle = p.bgDeep;
    ctx.fillRect(0, 0, this.w, this.h);

    this.drawField(p);
    this.drawGrid(p);
    this.drawDangerLine(p, s.time);
    this.drawWalls(p);

    if (s.bricks) this.drawBricks(s.bricks, p);
    if (s.powerups) this.drawPowerups(s.powerups, p);
    if (s.shots) this.drawShots(s.shots, p);
    if (s.paddle) this.drawPaddle(s.paddle, p, s.slowFactor);
    if (s.balls) this.drawBalls(s.balls, p);
    if (s.particles) this.drawParticles(s.particles);

    ctx.restore();
  }

  /* ---------------------------------------------------------------------- */
  /* Hintergrund                                                             */
  /* ---------------------------------------------------------------------- */

  drawField(p) {
    const ctx = this.ctx;
    this.proj.pathRect(ctx, 0, FIELD.TOP - WALL, FIELD.W, FIELD.H - FIELD.TOP + WALL);
    ctx.fillStyle = p.bg;
    ctx.fill();
  }

  drawGrid(p) {
    const ctx = this.ctx;
    ctx.save();
    ctx.strokeStyle = p.grid;
    ctx.lineWidth = 1;
    noGlow(ctx);

    const top = FIELD.TOP - WALL;

    ctx.beginPath();
    for (let x = 0; x <= FIELD.W; x += GRID) {
      const a = this.proj.project(x, top, { x: 0, y: 0 });
      const b = this.proj.project(x, FIELD.H, { x: 0, y: 0 });
      ctx.moveTo(a.x, a.y);
      ctx.lineTo(b.x, b.y);
    }
    for (let y = top; y <= FIELD.H; y += GRID) {
      const a = this.proj.project(0, y, { x: 0, y: 0 });
      const b = this.proj.project(FIELD.W, y, { x: 0, y: 0 });
      ctx.moveTo(a.x, a.y);
      ctx.lineTo(b.x, b.y);
    }
    ctx.stroke();
    ctx.restore();
  }

  /** Die offene Unterkante — hier geht der Ball verloren. */
  drawDangerLine(p, time = 0) {
    const ctx = this.ctx;
    const a = this.proj.project(0, FIELD.FLOOR, { x: 0, y: 0 });
    const b = this.proj.project(FIELD.W, FIELD.FLOOR, { x: 0, y: 0 });

    ctx.save();
    ctx.strokeStyle = p.red;
    ctx.globalAlpha = 0.28 + 0.16 * Math.sin(time * 2.4);
    ctx.lineWidth = 2;
    ctx.setLineDash([9, 7]);
    glow(ctx, p.red, 0.8);
    ctx.beginPath();
    ctx.moveTo(a.x, a.y);
    ctx.lineTo(b.x, b.y);
    ctx.stroke();
    ctx.restore();
  }

  /* ---------------------------------------------------------------------- */
  /* Blockrahmen — die Magenta-Mauer aus dem SNAKEone-Titelbild              */
  /* ---------------------------------------------------------------------- */

  drawWalls(p) {
    const top = FIELD.TOP - WALL;

    // Oberkante, durchgehend ueber die Ecken hinweg
    for (let x = -WALL; x < FIELD.W + WALL; x += BLOCK + BLOCK_GAP) {
      const w = Math.min(BLOCK, FIELD.W + WALL - x);
      if (w > 2) this.drawWallBlock(x, top, w, WALL, p);
    }

    // Seitenwaende
    for (let y = top + WALL + BLOCK_GAP; y < FIELD.H; y += BLOCK + BLOCK_GAP) {
      const h = Math.min(BLOCK, FIELD.H - y);
      if (h <= 2) continue;
      this.drawWallBlock(-WALL, y, WALL, h, p);
      this.drawWallBlock(FIELD.W, y, WALL, h, p);
    }
  }

  drawWallBlock(x, y, w, h, p) {
    const ctx = this.ctx;
    const q = this.proj.pathRect(ctx, x, y, w, h);

    ctx.save();
    ctx.fillStyle = p.magentaDeep;
    ctx.globalAlpha = 0.55;
    ctx.fill();

    ctx.globalAlpha = 1;
    ctx.strokeStyle = p.magenta;
    ctx.lineWidth = 1.4;
    glow(ctx, p.magenta, 0.9);
    ctx.stroke();

    // Blaues Kantenlicht auf der Oberkante — gibt den Bloecken Tiefe.
    noGlow(ctx);
    ctx.strokeStyle = p.blueEdge;
    ctx.globalAlpha = 0.65;
    ctx.lineWidth = 1.2;
    ctx.beginPath();
    ctx.moveTo(q[0].x, q[0].y);
    ctx.lineTo(q[1].x, q[1].y);
    ctx.stroke();
    ctx.restore();
  }

  /* ---------------------------------------------------------------------- */
  /* Steine                                                                  */
  /* ---------------------------------------------------------------------- */

  drawBricks(bricks, p) {
    const ctx = this.ctx;

    for (const b of bricks) {
      if (!b.alive) continue;
      const col = brickColor(b.hp, b.steel);
      const q = this.proj.pathRect(ctx, b.x, b.y, b.w, b.h);

      ctx.save();

      // Fuellung bleibt dunkel — die Farbe kommt aus Kontur und Glow,
      // genau wie bei den Neon-Bloecken in SNAKEone.
      ctx.fillStyle = col;
      ctx.globalAlpha = b.steel ? 0.22 : 0.16 + 0.07 * Math.min(b.hp, 3);
      ctx.fill();

      ctx.globalAlpha = 1;
      ctx.strokeStyle = col;
      ctx.lineWidth = b.steel ? 1.2 : 1.6;
      glow(ctx, col, b.steel ? 0.5 : 1);
      ctx.stroke();

      // Kantenlicht oben
      noGlow(ctx);
      ctx.strokeStyle = b.steel ? p.blueEdge : p.greenCore;
      ctx.globalAlpha = b.steel ? 0.5 : 0.35;
      ctx.lineWidth = 1;
      ctx.beginPath();
      ctx.moveTo(q[0].x, q[0].y);
      ctx.lineTo(q[1].x, q[1].y);
      ctx.stroke();

      // Aufblitzen direkt nach einem Treffer
      if (b.flash > 0) {
        ctx.globalAlpha = b.flash * 0.85;
        ctx.fillStyle = p.greenCore;
        ctx.beginPath();
        ctx.moveTo(q[0].x, q[0].y);
        ctx.lineTo(q[1].x, q[1].y);
        ctx.lineTo(q[2].x, q[2].y);
        ctx.lineTo(q[3].x, q[3].y);
        ctx.closePath();
        ctx.fill();
      }

      ctx.restore();
    }
  }

  /* ---------------------------------------------------------------------- */
  /* Paddle                                                                  */
  /* ---------------------------------------------------------------------- */

  drawPaddle(pad, p, slowFactor = 1) {
    const ctx = this.ctx;
    const col = slowFactor < 1 ? p.cyan : p.green;
    const q = this.proj.pathRect(ctx, pad.left, pad.y, pad.w, pad.h);

    ctx.save();
    ctx.fillStyle = col;
    ctx.globalAlpha = 0.3;
    ctx.fill();

    ctx.globalAlpha = 1;
    ctx.strokeStyle = col;
    ctx.lineWidth = 2;
    glow(ctx, col, 1.3);
    ctx.stroke();

    noGlow(ctx);
    ctx.strokeStyle = p.greenCore;
    ctx.globalAlpha = 0.6;
    ctx.lineWidth = 1.2;
    ctx.beginPath();
    ctx.moveTo(q[0].x, q[0].y);
    ctx.lineTo(q[1].x, q[1].y);
    ctx.stroke();

    // Laserkanonen an den Enden
    if (pad.laser) {
      ctx.globalAlpha = 1;
      for (const cx of [pad.left + 4, pad.right - 4]) {
        this.proj.pathRect(ctx, cx - 1.6, pad.y - 5, 3.2, 5);
        ctx.fillStyle = p.gold;
        glow(ctx, p.gold, 1);
        ctx.fill();
      }
    }
    ctx.restore();
  }

  /* ---------------------------------------------------------------------- */
  /* Baelle                                                                  */
  /* ---------------------------------------------------------------------- */

  drawBalls(balls, p) {
    const ctx = this.ctx;

    ctx.save();
    ctx.globalCompositeOperation = 'lighter';

    for (const ball of balls) {
      // Nachzieh-Spur
      if (ball.trail.length >= 4) {
        ctx.strokeStyle = p.green;
        ctx.lineCap = 'round';
        for (let i = 0; i < ball.trail.length - 2; i += 2) {
          const t = (i / 2) / (ball.trail.length / 2);
          const a = this.proj.project(ball.trail[i], ball.trail[i + 1], { x: 0, y: 0 });
          const b = this.proj.project(ball.trail[i + 2], ball.trail[i + 3], { x: 0, y: 0 });
          ctx.globalAlpha = t * 0.34;
          ctx.lineWidth = 1 + t * 2.4;
          ctx.beginPath();
          ctx.moveTo(a.x, a.y);
          ctx.lineTo(b.x, b.y);
          ctx.stroke();
        }
      }

      const c = this.proj.project(ball.x, ball.y, { x: 0, y: 0 });
      const r = Math.max(1.5, ball.r * this.proj.scaleAt(ball.y));

      ctx.globalAlpha = 1;
      glow(ctx, p.green, 1.6);
      ctx.fillStyle = p.green;
      ctx.beginPath();
      ctx.arc(c.x, c.y, r, 0, Math.PI * 2);
      ctx.fill();

      noGlow(ctx);
      ctx.fillStyle = p.greenCore;
      ctx.beginPath();
      ctx.arc(c.x, c.y, r * 0.45, 0, Math.PI * 2);
      ctx.fill();
    }

    ctx.restore();
  }

  /* ---------------------------------------------------------------------- */
  /* Power-Ups und Schuesse                                                  */
  /* ---------------------------------------------------------------------- */

  drawPowerups(list, p) {
    const ctx = this.ctx;

    for (const pu of list) {
      if (!pu.alive) continue;
      const col = pu.type.good ? p.gold : p.red;
      // Pulsierende Breite statt echter Rotation — bleibt in der Projektion
      // stabil und liest sich auf kleinen Displays besser.
      const k = 0.55 + 0.45 * Math.abs(Math.cos(pu.spin));
      const w = pu.w * k;

      ctx.save();
      this.proj.pathRect(ctx, pu.x - w / 2, pu.y - pu.h / 2, w, pu.h);
      ctx.fillStyle = col;
      ctx.globalAlpha = 0.26;
      ctx.fill();
      ctx.globalAlpha = 1;
      ctx.strokeStyle = col;
      ctx.lineWidth = 1.6;
      glow(ctx, col, 1.2);
      ctx.stroke();

      // Kennbuchstabe
      const c = this.proj.project(pu.x, pu.y, { x: 0, y: 0 });
      const size = Math.max(7, 10 * this.proj.scaleAt(pu.y));
      noGlow(ctx);
      ctx.fillStyle = pu.type.good ? p.goldCore : p.bgDeep;
      ctx.font = `700 ${size}px ui-monospace, monospace`;
      ctx.textAlign = 'center';
      ctx.textBaseline = 'middle';
      ctx.fillText(pu.type.code, c.x, c.y);
      ctx.restore();
    }
  }

  drawShots(shots, p) {
    const ctx = this.ctx;
    ctx.save();
    ctx.globalCompositeOperation = 'lighter';
    glow(ctx, p.gold, 1.2);
    ctx.fillStyle = p.gold;

    for (const s of shots) {
      if (!s.alive) continue;
      this.proj.pathRect(ctx, s.x - LASER.W / 2, s.y, LASER.W, LASER.H);
      ctx.fill();
    }
    ctx.restore();
  }

  /* ---------------------------------------------------------------------- */
  /* Partikel                                                                */
  /* ---------------------------------------------------------------------- */

  drawParticles(parts) {
    const ctx = this.ctx;
    ctx.save();
    // Additiv statt shadowBlur: bei bis zu 320 Partikeln ist ein Glow je
    // Partikel zu teuer, ueberlagerte Helligkeit sieht hier genauso aus.
    ctx.globalCompositeOperation = 'lighter';
    noGlow(ctx);

    parts.forEach((x, y, size, color, t) => {
      const c = this.proj.project(x, y, { x: 0, y: 0 });
      const r = Math.max(0.5, size * this.proj.scaleAt(y));
      ctx.globalAlpha = Math.min(1, t * 1.2);
      ctx.fillStyle = color;
      ctx.beginPath();
      ctx.arc(c.x, c.y, r, 0, Math.PI * 2);
      ctx.fill();
    });

    ctx.restore();
  }
}

export { WALL, BLOCK };
