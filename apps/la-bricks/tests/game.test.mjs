import { chromium } from 'playwright';
import { spawn } from 'node:child_process';
import { setTimeout as sleep } from 'node:timers/promises';

import path from 'node:path';
import { fileURLToPath } from 'node:url';
import { mkdirSync } from 'node:fs';

const HERE = path.dirname(fileURLToPath(import.meta.url));
const ROOT = path.resolve(HERE, '..');
// Diagnosebilder des Laufs — nicht die Store-Grafiken, die entstehen in
// store/build-assets.mjs.
const OUT = path.join(HERE, 'output');
const PORT = 8731;
mkdirSync(OUT, { recursive: true });

let fails = 0;
const check = (name, ok, detail = '') => {
  if (!ok) { fails++; console.log(`  FAIL  ${name}  ${detail}`); }
  else console.log(`  ok    ${name}${detail ? '  ' + detail : ''}`);
};

const server = spawn('python3', ['-m', 'http.server', String(PORT), '--bind', '127.0.0.1'],
  { cwd: ROOT, stdio: 'ignore' });
await sleep(900);

const browser = await chromium.launch({
  executablePath: process.env.CHROMIUM_PATH || undefined,
});
const page = await browser.newPage({
  viewport: { width: 540, height: 960 },
  deviceScaleFactor: 2,
});

const errors = [];
page.on('console', (m) => { if (m.type() === 'error') errors.push(m.text()); });
page.on('pageerror', (e) => errors.push('PAGEERROR: ' + e.message));

console.log('\n[Laden]');
await page.goto(`http://127.0.0.1:${PORT}/index.html`, { waitUntil: 'networkidle' });
await sleep(700);

// Fehlende PNG-Symbole sind zu diesem Zeitpunkt erwartet — sie werden erst
// spaeter erzeugt. Alles andere ist ein echter Fehler.
const realErrors = errors.filter((e) => !/icon-\d+\.png|icon\.svg|404|Failed to load resource/i.test(e));
check('keine Skriptfehler', realErrors.length === 0, realErrors.join(' | '));

check('Titelbildschirm sichtbar',
  await page.locator('#screen-title').isVisible());
check('Hero-Text korrekt',
  (await page.locator('.hero').first().textContent()).trim() === 'BRICKS');
check('Qualifier korrekt',
  (await page.locator('.hero-sub').first().textContent()).includes('LA'));
check('HUD auf dem Titel verborgen',
  await page.locator('#hud').isHidden());

// Canvas darf nicht leer sein.
const painted = await page.evaluate(() => {
  const c = document.querySelector('#board');
  const g = c.getContext('2d');
  const d = g.getImageData(0, 0, c.width, c.height).data;
  let nonBg = 0;
  for (let i = 0; i < d.length; i += 4 * 97) {
    if (d[i] > 20 || d[i + 1] > 30 || d[i + 2] > 20) nonBg++;
  }
  return nonBg;
});
check('Canvas zeichnet Inhalt', painted > 50, `${painted} Proben`);

await page.screenshot({ path: `${OUT}/01-titel.png` });

console.log('\n[Einstellungen]');
const modusBtn = page.locator('[data-opt="modus"]');
const before = (await modusBtn.locator('.v').textContent()).trim();
await modusBtn.click();
const after = (await modusBtn.locator('.v').textContent()).trim();
check('Modus schaltet um', before !== after, `${before} -> ${after}`);
await modusBtn.click(); await modusBtn.click();  // zurueck auf KLASSIK
check('Modus rotiert zurueck',
  (await modusBtn.locator('.v').textContent()).trim() === before);

console.log('\n[Start]');
await page.locator('#screen-title').click({ position: { x: 270, y: 300 } });
await sleep(200);
check('Zustand = playing',
  await page.evaluate(() => window.LABricks.game.state) === 'playing');
check('HUD sichtbar', await page.locator('#hud').isVisible());
check('Steine geladen',
  await page.evaluate(() => window.LABricks.game.bricks.length) > 0);
check('Ball liegt auf dem Paddle',
  await page.evaluate(() => window.LABricks.game.balls[0].stuck) === true);

console.log('\n[Physik]');
// Ball abschiessen und laufen lassen, Paddle folgt automatisch.
await page.evaluate(() => {
  const g = window.LABricks.game;
  g.launch(g.balls[0]);
  window.__autopilot = true;
  const tick = () => {
    if (!window.__autopilot) return;
    const b = g.balls[0];
    if (b) g.aim(b.x);
    requestAnimationFrame(tick);
  };
  requestAnimationFrame(tick);
});

const startBricks = await page.evaluate(() => window.LABricks.game.destructibleLeft);
await sleep(3500);
const midState = await page.evaluate(() => {
  const g = window.LABricks.game;
  const b = g.balls[0];
  return {
    left: g.destructibleLeft,
    score: g.score,
    ballFinite: b ? Number.isFinite(b.x) && Number.isFinite(b.y) : true,
    inField: b ? (b.x >= -5 && b.x <= 365 && b.y >= 0 && b.y <= 620) : true,
    lives: g.lives,
    state: g.state,
  };
});

check('Steine werden zerstoert', midState.left < startBricks,
  `${startBricks} -> ${midState.left}`);
check('Punkte steigen', midState.score > 0, `${midState.score}`);
check('Ball bleibt im Feld', midState.inField && midState.ballFinite);
check('kein ungewollter Zustandswechsel',
  midState.state === 'playing' || midState.state === 'levelclear', midState.state);

await page.screenshot({ path: `${OUT}/02-spiel.png` });

console.log('\n[Power-Ups]');
const puResult = await page.evaluate(async () => {
  const g = window.LABricks.game;
  const out = {};
  const before = g.paddle.w;
  g.collect({ code: 'W', label: 'BREITES PADDLE', good: true });
  out.wide = g.paddle.w > before;
  g.collect({ code: 'N', label: 'SCHMALES PADDLE', good: false });
  out.narrow = g.paddle.w < before;
  g.collect({ code: 'L', label: 'LASER', good: true });
  out.laser = g.paddle.laser === true;
  const nBefore = g.balls.length;
  g.collect({ code: 'M', label: 'MULTIBALL', good: true });
  out.multi = g.balls.length > nBefore;
  out.capped = g.balls.length <= 7;
  return out;
});
check('breites Paddle wirkt', puResult.wide);
check('schmales Paddle wirkt', puResult.narrow);
check('Laser aktiviert', puResult.laser);
check('Multiball teilt auf', puResult.multi);
check('Ballzahl begrenzt', puResult.capped);

console.log('\n[Pause]');
await page.evaluate(() => { window.__autopilot = false; });
await page.locator('#btn-pause').click();
await sleep(150);
check('Pause aktiv',
  await page.evaluate(() => window.LABricks.game.state) === 'paused');
check('Pausenbildschirm sichtbar', await page.locator('#screen-pause').isVisible());
await page.locator('#btn-resume').click();
await sleep(150);
check('Fortsetzen funktioniert',
  await page.evaluate(() => window.LABricks.game.state) === 'playing');

console.log('\n[Levelabschluss]');
await page.evaluate(() => {
  const g = window.LABricks.game;
  for (const b of g.bricks) if (!b.steel) b.alive = false;
  g.destructibleLeft = 0;
});
await sleep(400);
check('Level geschafft erkannt',
  await page.evaluate(() => window.LABricks.game.state) === 'levelclear');
check('Abschlussbildschirm sichtbar', await page.locator('#screen-level').isVisible());

const lvlBefore = await page.evaluate(() => window.LABricks.game.level);
await page.locator('#screen-level').click({ position: { x: 270, y: 700 } });
await sleep(300);
check('naechstes Level geladen',
  await page.evaluate(() => window.LABricks.game.level) === lvlBefore + 1);
check('neue Steine vorhanden',
  await page.evaluate(() => window.LABricks.game.destructibleLeft) > 0);

console.log('\n[Spielende]');
await page.evaluate(() => {
  const g = window.LABricks.game;
  g.lives = 1;
  g.balls.length = 0;
});
await sleep(400);
check('Spielende erkannt',
  await page.evaluate(() => window.LABricks.game.state) === 'gameover');
check('Endbildschirm sichtbar', await page.locator('#screen-over').isVisible());
check('Rekord gespeichert',
  await page.evaluate(() => {
    try { return parseInt(localStorage.getItem('labricks.highscore') || '0', 10) > 0; }
    catch { return false; }
  }));

await page.screenshot({ path: `${OUT}/03-ende.png` });

console.log('\n[Alle Level durchlaufen]');
// Jedes handgebaute Level plus einige erzeugte muss ein gueltiges,
// nicht-leeres Raster liefern.
const levelCheck = await page.evaluate(async () => {
  const { getLevel, HANDMADE_COUNT } = await import('./js/levels.js');
  const { buildBricks } = await import('./js/entities.js');
  const bad = [];
  for (let n = 1; n <= HANDMADE_COUNT + 12; n++) {
    const rows = getLevel(n);
    if (rows.some((r) => r.length !== 9)) bad.push(`${n}: Breite`);
    const { destructible } = buildBricks(rows);
    if (destructible === 0) bad.push(`${n}: leer`);
  }
  return { bad, total: HANDMADE_COUNT };
});
check('alle Level gueltig', levelCheck.bad.length === 0, levelCheck.bad.join(', '));
console.log(`        (${levelCheck.total} handgebaut + 12 erzeugt geprueft)`);

console.log('\n[Groessenwechsel]');
for (const vp of [{ width: 360, height: 640 }, { width: 412, height: 915 }, { width: 768, height: 1024 }]) {
  await page.setViewportSize(vp);
  await sleep(250);
  const ok = await page.evaluate(() => {
    const r = window.LABricks.renderer;
    const p = r.proj.project(180, 300, { x: 0, y: 0 });
    return r.canvas.width > 0 && Number.isFinite(p.x) && Number.isFinite(p.y);
  });
  check(`${vp.width}x${vp.height}`, ok);
}

const finalErrors = errors.filter((e) => !/icon-\d+\.png|icon\.svg|404|Failed to load resource/i.test(e));
console.log('\n[Konsole]');
check('keine Fehler ueber den gesamten Lauf', finalErrors.length === 0,
  finalErrors.slice(0, 5).join(' | '));

await browser.close();
server.kill();

console.log(fails === 0 ? '\nERGEBNIS: PASS' : `\nERGEBNIS: ${fails} FEHLER`);
process.exit(fails === 0 ? 0 : 1);
