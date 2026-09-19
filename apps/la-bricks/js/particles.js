/**
 * LA-BRICKS — Partikel
 *
 * Der Funkenausbruch beim Treffer ist bei SNAKEone das auffaelligste
 * Bewegungselement (der goldene Blitz um das Futter). Hier dieselbe Rolle:
 * er quittiert jeden zerstoerten Stein.
 *
 * Feste Obergrenze und ein Ringpuffer statt Array-Wachstum — auf schwachen
 * Geraeten darf eine Kettenreaktion die Framerate nicht einbrechen lassen.
 */

const MAX = 320;

export class Particles {
  constructor() {
    this.x = new Float32Array(MAX);
    this.y = new Float32Array(MAX);
    this.vx = new Float32Array(MAX);
    this.vy = new Float32Array(MAX);
    this.life = new Float32Array(MAX);   // verbleibend, Sekunden
    this.max = new Float32Array(MAX);    // Ausgangswert, fuer die Blende
    this.size = new Float32Array(MAX);
    this.color = new Array(MAX).fill('#fff');
    this.head = 0;
  }

  /**
   * Funkenausbruch.
   * @param {number} x
   * @param {number} y
   * @param {string} color
   * @param {number} [count]
   * @param {number} [power] Geschwindigkeitsfaktor.
   */
  burst(x, y, color, count = 12, power = 1) {
    for (let i = 0; i < count; i++) {
      const idx = this.head;
      this.head = (this.head + 1) % MAX;

      const a = Math.random() * Math.PI * 2;
      const sp = (40 + Math.random() * 150) * power;

      this.x[idx] = x;
      this.y[idx] = y;
      this.vx[idx] = Math.cos(a) * sp;
      this.vy[idx] = Math.sin(a) * sp;
      const l = 0.28 + Math.random() * 0.42;
      this.life[idx] = l;
      this.max[idx] = l;
      this.size[idx] = 1 + Math.random() * 2.2;
      this.color[idx] = color;
    }
  }

  /**
   * Gerichteter Schauer — etwa wenn der Ball von einer Wand abprallt.
   */
  spray(x, y, color, nx, ny, count = 6) {
    for (let i = 0; i < count; i++) {
      const idx = this.head;
      this.head = (this.head + 1) % MAX;

      const spread = (Math.random() - 0.5) * 1.4;
      const cos = Math.cos(spread);
      const sin = Math.sin(spread);
      const sp = 50 + Math.random() * 110;

      this.x[idx] = x;
      this.y[idx] = y;
      this.vx[idx] = (nx * cos - ny * sin) * sp;
      this.vy[idx] = (nx * sin + ny * cos) * sp;
      const l = 0.16 + Math.random() * 0.24;
      this.life[idx] = l;
      this.max[idx] = l;
      this.size[idx] = 0.9 + Math.random() * 1.4;
      this.color[idx] = color;
    }
  }

  update(dt) {
    for (let i = 0; i < MAX; i++) {
      if (this.life[i] <= 0) continue;
      this.life[i] -= dt;
      if (this.life[i] <= 0) continue;
      this.x[i] += this.vx[i] * dt;
      this.y[i] += this.vy[i] * dt;
      this.vy[i] += 210 * dt;        // leichte Schwerkraft
      this.vx[i] *= 1 - 1.6 * dt;    // Luftwiderstand
      this.vy[i] *= 1 - 0.6 * dt;
    }
  }

  clear() { this.life.fill(0); }

  /** Iteriert ueber die lebenden Partikel. */
  forEach(fn) {
    for (let i = 0; i < MAX; i++) {
      if (this.life[i] <= 0) continue;
      fn(this.x[i], this.y[i], this.size[i], this.color[i],
         this.life[i] / this.max[i]);
    }
  }
}
