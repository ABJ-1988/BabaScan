/**
 * LA-BRICKS — Perspektivische Projektion
 *
 * Bildet das logische Spielfeld (FIELD.W x FIELD.H) auf ein Trapez im
 * Bildschirmraum ab — der gekippte Neon-Grid-Look von SNAKEone.
 *
 * Verwendet eine echte Homographie (Heckbert, "Fundamentals of Texture
 * Mapping and Image Warping", Einheitsquadrat -> Viereck), nicht eine
 * bilineare Naeherung: nur so bleiben Geraden gerade und die Verjuengung
 * perspektivisch korrekt.
 *
 * Die Spiellogik kennt diese Datei nicht. Sie rechnet flach in logischen
 * Koordinaten; erst Renderer (project) und Eingabe (unproject) gehen durch
 * die Transformation. Bei tilt = 0 und rotate = 0 ist das Ergebnis exakt
 * eine gewoehnliche 2D-Skalierung.
 */

/**
 * Homographie vom Einheitsquadrat auf ein beliebiges konvexes Viereck.
 * Ecken in der Reihenfolge (0,0), (1,0), (1,1), (0,1).
 *
 * @param {Array<{x:number,y:number}>} q Vier Zielecken.
 * @returns {number[]} [a,b,c,d,e,f,g,h] der Matrix [[a,b,c],[d,e,f],[g,h,1]].
 */
function squareToQuad(q) {
  const [p0, p1, p2, p3] = q;

  const dx1 = p1.x - p2.x;
  const dx2 = p3.x - p2.x;
  const dx3 = p0.x - p1.x + p2.x - p3.x;
  const dy1 = p1.y - p2.y;
  const dy2 = p3.y - p2.y;
  const dy3 = p0.y - p1.y + p2.y - p3.y;

  // Parallelogramm -> affiner Sonderfall, keine perspektivischen Terme.
  if (Math.abs(dx3) < 1e-10 && Math.abs(dy3) < 1e-10) {
    return [
      p1.x - p0.x, p3.x - p0.x, p0.x,
      p1.y - p0.y, p3.y - p0.y, p0.y,
      0, 0,
    ];
  }

  const den = dx1 * dy2 - dx2 * dy1;
  if (Math.abs(den) < 1e-12) {
    // Entartetes Viereck — auf affin zurueckfallen statt NaN zu erzeugen.
    return [
      p1.x - p0.x, p3.x - p0.x, p0.x,
      p1.y - p0.y, p3.y - p0.y, p0.y,
      0, 0,
    ];
  }

  const g = (dx3 * dy2 - dx2 * dy3) / den;
  const h = (dx1 * dy3 - dx3 * dy1) / den;

  return [
    p1.x - p0.x + g * p1.x, p3.x - p0.x + h * p3.x, p0.x,
    p1.y - p0.y + g * p1.y, p3.y - p0.y + h * p3.y, p0.y,
    g, h,
  ];
}

/** Invertiert [[a,b,c],[d,e,f],[g,h,1]]. Gibt 9 Koeffizienten zurueck. */
function invert(m) {
  const [a, b, c, d, e, f, g, h] = m;

  const A = e - f * h;
  const B = c * h - b;
  const C = b * f - c * e;
  const D = f * g - d;
  const E = a - c * g;
  const F = c * d - a * f;
  const G = d * h - e * g;
  const H = b * g - a * h;
  const I = a * e - b * d;

  const det = a * A + b * D + c * G;
  if (Math.abs(det) < 1e-12) return null;

  return [A / det, B / det, C / det,
          D / det, E / det, F / det,
          G / det, H / det, I / det];
}

/** Wendet [[a,b,c],[d,e,f],[g,h,1]] auf eine normierte Koordinate an. */
function applyM(m, u, v) {
  const [a, b, c, d, e, f, g, h] = m;
  const w = g * u + h * v + 1;
  // Jenseits des Horizonts kippt w das Vorzeichen — solche Punkte haben
  // kein sinnvolles Bild und werden vom Aufrufer verworfen.
  if (!(w > 1e-6)) return null;
  return { x: (a * u + b * v + c) / w, y: (d * u + e * v + f) / w };
}

/**
 * Umschliessender Kasten aus Feldecken und — falls angegeben — den durch
 * dieselbe Transformation abgebildeten Ecken des Rahmenbereichs.
 */
function boundsOf(m, fieldW, fieldH, corners, bleed) {
  const pts = corners.slice();

  if (bleed) {
    const { x0, y0, x1, y1 } = bleed;
    for (const [x, y] of [[x0, y0], [x1, y0], [x1, y1], [x0, y1]]) {
      const p = applyM(m, x / fieldW, y / fieldH);
      if (p) pts.push(p);
    }
  }

  let minX = Infinity, minY = Infinity, maxX = -Infinity, maxY = -Infinity;
  for (const p of pts) {
    if (!Number.isFinite(p.x) || !Number.isFinite(p.y)) continue;
    if (p.x < minX) minX = p.x;
    if (p.x > maxX) maxX = p.x;
    if (p.y < minY) minY = p.y;
    if (p.y > maxY) maxY = p.y;
  }

  if (!Number.isFinite(minX)) return { minX: 0, minY: 0, w: 0, h: 0 };
  return { minX, minY, w: maxX - minX, h: maxY - minY };
}

export class Projection {
  /**
   * @param {number} fieldW Logische Feldbreite.
   * @param {number} fieldH Logische Feldhoehe.
   */
  constructor(fieldW, fieldH) {
    this.fieldW = fieldW;
    this.fieldH = fieldH;
    this.m = null;
    this.inv = null;
    this.resize(fieldW, fieldH, 0, 0, 0);
  }

  /**
   * Neu berechnen — bei Groessenaenderung oder Theme-Wechsel aufrufen.
   *
   * @param {number} screenW  Canvas-Breite in CSS-Pixeln.
   * @param {number} screenH  Canvas-Hoehe in CSS-Pixeln.
   * @param {number} tilt     0..0.6 — Verjuengung der Ferne.
   * @param {number} rotate   Drehung in der Ebene (Radiant).
   * @param {number} horizon  Anteil der Hoehe, um den die Oberkante weicht.
   * @param {{x0:number,y0:number,x1:number,y1:number}} [bleed]
   *        Logischer Bereich, der zusaetzlich zum Feld sichtbar bleiben muss —
   *        der Blockrahmen liegt ausserhalb des Feldes. Wird mit eingemessen,
   *        statt ihn ueber einen geschaetzten Randfaktor abzudecken: bei
   *        Drehung schwenken die Ecken unterschiedlich weit aus, ein fester
   *        Faktor schneidet dann eine Seite ab.
   */
  resize(screenW, screenH, tilt = 0, rotate = 0, horizon = 0, bleed = null) {
    this.screenW = screenW;
    this.screenH = screenH;

    const t = Math.max(0, Math.min(0.6, tilt));
    const cos = Math.cos(rotate);
    const sin = Math.sin(rotate);

    /** Baut die vier Feldecken zu einem gegebenen Massstab, zentriert um (0,0). */
    const cornersFor = (scale) => {
      const fullW = this.fieldW * scale;
      const fullH = this.fieldH * scale * (1 - horizon);
      const topW = fullW * (1 - t);
      const halfH = fullH / 2;

      const raw = [
        { x: -topW / 2, y: -halfH }, // logisch (0,0)  oben links
        { x:  topW / 2, y: -halfH }, // logisch (1,0)  oben rechts
        { x:  fullW / 2, y: halfH }, // logisch (1,1)  unten rechts
        { x: -fullW / 2, y: halfH }, // logisch (0,1)  unten links
      ];
      if (!rotate) return raw;
      return raw.map(({ x, y }) => ({ x: x * cos - y * sin, y: x * sin + y * cos }));
    };

    // Erster Durchgang: Massstab, der allein das Feld einpasst.
    let scale = Math.min(screenW / this.fieldW, screenH / this.fieldH);
    let corners = cornersFor(scale);

    // Zweiter Durchgang: tatsaechlich benoetigten Platz messen — inklusive
    // Rahmen — und den Massstab so nachziehen, dass nichts abgeschnitten wird.
    let m = squareToQuad(corners);
    const box = boundsOf(m, this.fieldW, this.fieldH, corners, bleed);
    const need = Math.max(box.w / screenW, box.h / screenH);
    if (need > 0) {
      scale /= need;
      corners = cornersFor(scale);
      m = squareToQuad(corners);
    }

    // Auf den Bildschirm zentrieren — der gemessene Kasten, nicht das Feld,
    // damit der Rahmen auf beiden Seiten gleich viel Luft bekommt.
    const final = boundsOf(m, this.fieldW, this.fieldH, corners, bleed);
    const dx = screenW / 2 - (final.minX + final.w / 2);
    const dy = screenH / 2 - (final.minY + final.h / 2);
    corners = corners.map(({ x, y }) => ({ x: x + dx, y: y + dy }));

    this.corners = corners;
    this.scale = scale;
    this.m = squareToQuad(corners);
    this.inv = invert(this.m);
  }

  /**
   * Logisch -> Bildschirm.
   * @param {number} x
   * @param {number} y
   * @param {{x:number,y:number}} [out] Optionales Zielobjekt (spart Allokation).
   */
  project(x, y, out = { x: 0, y: 0 }) {
    const [a, b, c, d, e, f, g, h] = this.m;
    const u = x / this.fieldW;
    const v = y / this.fieldH;
    let w = g * u + h * v + 1;
    if (Math.abs(w) < 1e-9) w = 1e-9;
    out.x = (a * u + b * v + c) / w;
    out.y = (d * u + e * v + f) / w;
    return out;
  }

  /**
   * Bildschirm -> logisch. Fuer die Eingabe (Finger-/Mausposition).
   * @param {number} sx
   * @param {number} sy
   */
  unproject(sx, sy, out = { x: 0, y: 0 }) {
    if (!this.inv) { out.x = sx; out.y = sy; return out; }
    const [A, B, C, D, E, F, G, H, I] = this.inv;
    let w = G * sx + H * sy + I;
    if (Math.abs(w) < 1e-9) w = 1e-9;
    out.x = ((A * sx + B * sy + C) / w) * this.fieldW;
    out.y = ((D * sx + E * sy + F) / w) * this.fieldH;
    return out;
  }

  /**
   * Lokaler Massstab an einer logischen Position — fuer Radien, Strichstaerken
   * und Partikelgroessen, die perspektivisch mitwachsen sollen.
   * @param {number} y Logische Y-Position.
   * @returns {number} Bildschirmpixel pro logischem Pixel.
   */
  scaleAt(y) {
    const a = this.project(this.fieldW / 2, y, { x: 0, y: 0 });
    const b = this.project(this.fieldW / 2 + 10, y, { x: 0, y: 0 });
    return Math.hypot(b.x - a.x, b.y - a.y) / 10;
  }

  /**
   * Vier projizierte Ecken eines logischen Rechtecks — Bricks, Paddle, Power-Ups
   * werden als Vierecke gezeichnet, nicht als achsenparallele Rechtecke.
   * @returns {Array<{x:number,y:number}>} im Uhrzeigersinn ab oben links.
   */
  quad(x, y, w, h) {
    return [
      this.project(x,     y,     { x: 0, y: 0 }),
      this.project(x + w, y,     { x: 0, y: 0 }),
      this.project(x + w, y + h, { x: 0, y: 0 }),
      this.project(x,     y + h, { x: 0, y: 0 }),
    ];
  }

  /** Zeichnet den Pfad eines logischen Rechtecks in den Kontext. */
  pathRect(ctx, x, y, w, h) {
    const q = this.quad(x, y, w, h);
    ctx.beginPath();
    ctx.moveTo(q[0].x, q[0].y);
    ctx.lineTo(q[1].x, q[1].y);
    ctx.lineTo(q[2].x, q[2].y);
    ctx.lineTo(q[3].x, q[3].y);
    ctx.closePath();
    return q;
  }
}
