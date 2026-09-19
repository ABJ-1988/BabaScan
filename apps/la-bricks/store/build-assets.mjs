/**
 * LA-BRICKS — Store-Grafiken erzeugen
 *
 * Rendert die SVG-Vorlagen zu PNG und nimmt echte Bildschirmfotos aus dem
 * laufenden Spiel auf. Reproduzierbar: nach einer Aenderung an css/theme.css
 * oder an den SVGs erneut ausfuehren, dann stimmen Symbole, Feature-Grafik
 * und Bildschirmfotos wieder mit dem Spiel ueberein.
 *
 * Voraussetzung:
 *   npm i playwright
 *
 * Aufruf (aus apps/la-bricks/):
 *   node store/build-assets.mjs
 *
 * Erzeugt in store/assets/:
 *   icon-192.png, icon-512.png, icon-maskable-512.png   (PWA + Play Console)
 *   feature-graphic.png            1024x500   (Play Console, Pflicht)
 *   screenshot-*.png               1080x1920  (Play Console, mind. 2 noetig)
 */

import { chromium } from 'playwright';
import { spawn } from 'node:child_process';
import { readFile, mkdir } from 'node:fs/promises';
import { setTimeout as sleep } from 'node:timers/promises';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

const HERE = path.dirname(fileURLToPath(import.meta.url));
const APP = path.resolve(HERE, '..');
const ASSETS = path.join(HERE, 'assets');
const PORT = 8742;

await mkdir(ASSETS, { recursive: true });

const browser = await chromium.launch({
  executablePath: process.env.CHROMIUM_PATH || undefined,
});

/* -------------------------------------------------------------------------- */
/* SVG -> PNG                                                                  */
/* -------------------------------------------------------------------------- */

async function renderSvg(svgFile, outFile, width, height) {
  const svg = await readFile(path.join(ASSETS, svgFile), 'utf8');
  const page = await browser.newPage({
    viewport: { width, height },
    deviceScaleFactor: 1,
  });
  await page.setContent(
    `<!doctype html><html><head><meta charset="utf-8"><style>
       html,body{margin:0;padding:0;background:transparent}
       svg{display:block;width:${width}px;height:${height}px}
     </style></head><body>${svg}</body></html>`,
    { waitUntil: 'load' },
  );
  // Filter und Schriften sicher fertig rendern lassen.
  await page.evaluate(() => document.fonts?.ready);
  await sleep(180);
  await page.screenshot({ path: path.join(ASSETS, outFile), omitBackground: false });
  await page.close();
  console.log(`  ${outFile}  ${width}x${height}`);
}

console.log('\nSymbole und Feature-Grafik:');
await renderSvg('icon.svg',          'icon-192.png',           192,  192);
await renderSvg('icon.svg',          'icon-512.png',           512,  512);
await renderSvg('icon-maskable.svg', 'icon-maskable-512.png',  512,  512);
await renderSvg('feature-graphic.svg', 'feature-graphic.png', 1024,  500);

/* -------------------------------------------------------------------------- */
/* Bildschirmfotos aus dem laufenden Spiel                                     */
/* -------------------------------------------------------------------------- */

console.log('\nBildschirmfotos:');
const server = spawn('python3', ['-m', 'http.server', String(PORT), '--bind', '127.0.0.1'],
  { cwd: APP, stdio: 'ignore' });
await sleep(900);

// 540x960 bei doppelter Pixeldichte ergibt 1080x1920 — das Format, das die
// Play Console fuer Telefon-Bildschirmfotos erwartet.
const page = await browser.newPage({
  viewport: { width: 540, height: 960 },
  deviceScaleFactor: 2,
});
await page.goto(`http://127.0.0.1:${PORT}/index.html`, { waitUntil: 'networkidle' });
await sleep(800);

const shot = async (name) => {
  await page.screenshot({ path: path.join(ASSETS, `screenshot-${name}.png`) });
  console.log(`  screenshot-${name}.png  1080x1920`);
};

await shot('title');

// Partie starten und automatisch spielen lassen, damit die Aufnahmen
// tatsaechliches Spielgeschehen zeigen statt eines gestellten Standbilds.
await page.evaluate(() => {
  const g = window.LABricks.game;
  g.start();
  g.launch(g.balls[0]);
  window.__auto = true;
  const tick = () => {
    if (!window.__auto) return;
    const b = g.balls[0];
    if (b) g.aim(b.x);
    requestAnimationFrame(tick);
  };
  requestAnimationFrame(tick);
});
await sleep(4200);
await shot('play');

// Power-Ups aktiv: breites Paddle, Laser, Multiball.
await page.evaluate(() => {
  const g = window.LABricks.game;
  g.collect({ code: 'W', label: 'BREITES PADDLE', good: true });
  g.collect({ code: 'L', label: 'LASER', good: true });
  g.collect({ code: 'M', label: 'MULTIBALL', good: true });
});
await sleep(900);
await shot('powerups');

// Levelabschluss.
await page.evaluate(() => {
  window.__auto = false;
  const g = window.LABricks.game;
  for (const b of g.bricks) if (!b.steel) b.alive = false;
  g.destructibleLeft = 0;
});
await sleep(600);
await shot('levelclear');

// Spielende mit Rekord.
await page.evaluate(() => {
  const g = window.LABricks.game;
  g.state = 'playing';
  g.score = 18750;
  g.level = 7;
  g.lives = 1;
  g.balls.length = 0;
});
await sleep(700);
await shot('gameover');

await browser.close();
server.kill();
console.log('\nFertig.\n');
