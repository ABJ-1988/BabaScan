/**
 * Prueft die Homographie: project() gefolgt von unproject() muss die
 * Ausgangskoordinate zurueckgeben (Rundreise-Test), und zwar fuer den
 * flachen Fall wie fuer starke Kippung/Drehung.
 */
import { Projection } from '../js/projection.js';

const W = 360, H = 600;
const WALL = 15;   // Staerke des Blockrahmens, wie in render.js
let fails = 0;
const check = (name, ok, detail = '') => {
  if (!ok) { fails++; console.log(`  FAIL  ${name} ${detail}`); }
  else console.log(`  ok    ${name}`);
};

const cases = [
  { name: 'flach',            tilt: 0,    rot: 0,      hor: 0 },
  { name: 'leichte Kippung',  tilt: 0.30, rot: -0.055, hor: 0.06, bleed: WALL },
  { name: 'starke Kippung',   tilt: 0.55, rot: 0.22,   hor: 0.12, bleed: WALL },
  { name: 'nur Drehung',      tilt: 0,    rot: 0.4,    hor: 0,    bleed: WALL },
];

for (const c of cases) {
  console.log(`\n[${c.name}]`);
  const p = new Projection(W, H);
  const bleed = c.bleed
    ? { x0: -c.bleed, y0: 44 - 2 * c.bleed, x1: W + c.bleed, y1: H }
    : null;
  p.resize(540, 960, c.tilt, c.rot, c.hor, bleed);

  // Rundreise ueber ein Raster von Punkten im Feld.
  let maxErr = 0;
  for (let x = 0; x <= W; x += 30) {
    for (let y = 0; y <= H; y += 50) {
      const s = p.project(x, y, { x: 0, y: 0 });
      const b = p.unproject(s.x, s.y, { x: 0, y: 0 });
      maxErr = Math.max(maxErr, Math.abs(b.x - x), Math.abs(b.y - y));
    }
  }
  check('Rundreise project->unproject', maxErr < 1e-6, `maxErr=${maxErr.toExponential(2)}`);

  // Alle projizierten Punkte muessen endlich sein.
  let finite = true;
  for (let x = 0; x <= W; x += 45) {
    for (let y = 0; y <= H; y += 60) {
      const s = p.project(x, y, { x: 0, y: 0 });
      if (!Number.isFinite(s.x) || !Number.isFinite(s.y)) finite = false;
    }
  }
  check('alle Punkte endlich', finite);

  // Geraden bleiben gerade: drei kollineare Punkte bleiben kollinear.
  const a = p.project(0, 100, { x: 0, y: 0 });
  const m = p.project(W / 2, 100, { x: 0, y: 0 });
  const z = p.project(W, 100, { x: 0, y: 0 });
  const cross = (m.x - a.x) * (z.y - a.y) - (m.y - a.y) * (z.x - a.x);
  check('Geraden bleiben gerade', Math.abs(cross) < 1e-6, `cross=${cross.toExponential(2)}`);

  // Die Unterkante muss breiter projizieren als die Oberkante (Kippung > 0).
  const topL = p.project(0, 0, { x: 0, y: 0 });
  const topR = p.project(W, 0, { x: 0, y: 0 });
  const botL = p.project(0, H, { x: 0, y: 0 });
  const botR = p.project(W, H, { x: 0, y: 0 });
  const wTop = Math.hypot(topR.x - topL.x, topR.y - topL.y);
  const wBot = Math.hypot(botR.x - botL.x, botR.y - botL.y);
  if (c.tilt > 0) {
    check('Ferne verjuengt', wTop < wBot, `top=${wTop.toFixed(1)} bot=${wBot.toFixed(1)}`);
  } else {
    check('flach: gleich breit', Math.abs(wTop - wBot) < 1e-6);
  }

  // scaleAt muss positiv und nach unten wachsend sein.
  const s0 = p.scaleAt(0), s1 = p.scaleAt(H);
  check('scaleAt positiv', s0 > 0 && s1 > 0, `${s0.toFixed(3)} ${s1.toFixed(3)}`);
  if (c.tilt > 0) check('scaleAt waechst nach unten', s1 > s0);
}

// Entartete Eingaben duerfen nicht in NaN enden.
console.log('\n[Robustheit]');
const deg = new Projection(360, 600);
deg.resize(0, 0, 0.3, 0, 0, null);
const d = deg.project(100, 100, { x: 0, y: 0 });
check('Nullgroesse liefert Zahlen', Number.isFinite(d.x) && Number.isFinite(d.y));

console.log('\n[Rahmen bleibt sichtbar]');
for (const c of cases.filter((x) => x.bleed)) {
  const SW = 540, SH = 960;
  const p = new Projection(W, H);
  const bleed = { x0: -WALL, y0: 44 - 2 * WALL, x1: W + WALL, y1: H };
  p.resize(SW, SH, c.tilt, c.rot, c.hor, bleed);
  let inside = true;
  for (const [x, y] of [[bleed.x0, bleed.y0], [bleed.x1, bleed.y0],
                        [bleed.x1, bleed.y1], [bleed.x0, bleed.y1]]) {
    const s2 = p.project(x, y, { x: 0, y: 0 });
    if (s2.x < -0.5 || s2.x > SW + 0.5 || s2.y < -0.5 || s2.y > SH + 0.5) inside = false;
  }
  check(`Rahmen im Bild (${c.name})`, inside);
}

console.log(fails === 0 ? '\nERGEBNIS: PASS' : `\nERGEBNIS: ${fails} FEHLER`);
process.exit(fails === 0 ? 0 : 1);
