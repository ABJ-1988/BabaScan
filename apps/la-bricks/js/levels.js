/**
 * LA-BRICKS — Levelentwuerfe
 *
 * Ein Level ist ein Raster aus Zeichen, jede Zeile exakt BRICK.COLS (9) Zeichen:
 *   '.'       leer
 *   '1'..'3'  Stein mit so vielen Treffern
 *   'S'       Stahl — unzerstoerbar, zaehlt nicht zum Levelabschluss
 *
 * Nach dem letzten handgebauten Level erzeugt generate() prozedural weiter,
 * damit "ENDLOS" tatsaechlich endlos ist. Die Erzeugung ist deterministisch
 * (Seed = Levelnummer): dasselbe Level sieht bei jedem Spieler gleich aus.
 */

import { BRICK } from './config.js';

const LAYOUTS = [
  // 1 — Einstieg
  [
    '111111111',
    '111111111',
    '111111111',
  ],
  // 2 — erste zaehere Reihe
  [
    '222222222',
    '111111111',
    '111111111',
  ],
  // 3 — Pyramide
  [
    '....1....',
    '...111...',
    '..22222..',
    '.2222222.',
    '111111111',
  ],
  // 4 — Schachbrett
  [
    '1.1.1.1.1',
    '.2.2.2.2.',
    '1.1.1.1.1',
    '.2.2.2.2.',
    '1.1.1.1.1',
  ],
  // 5 — Stahlpfeiler
  [
    'S.......S',
    '222222222',
    '2.......2',
    '111111111',
    '111111111',
  ],
  // 6 — Rahmen
  [
    '333333333',
    '3.......3',
    '3.22222.3',
    '3.2...2.3',
    '3.22222.3',
    '3.......3',
    '333333333',
  ],
  // 7 — Saeulen
  [
    '2.2.2.2.2',
    '222222222',
    '2.2.2.2.2',
    '111111111',
    '1.1.1.1.1',
  ],
  // 8 — das Kreuz
  [
    '1.......1',
    '.2.....2.',
    '..3...3..',
    '...S.S...',
    '....3....',
    '...S.S...',
    '..3...3..',
    '.2.....2.',
    '1.......1',
  ],
  // 9 — Treppe
  [
    '3........',
    '33.......',
    '222......',
    '2222.....',
    '11111....',
    '111111...',
    '1111111..',
    '11111111.',
    '111111111',
  ],
  // 10 — Tor
  [
    'SS33333SS',
    '333333333',
    '3.......3',
    '2.......2',
    '2.SSSSS.2',
    '1.......1',
    '111111111',
  ],
  // 11 — Diamant
  [
    '....3....',
    '...333...',
    '..32223..',
    '.3211123.',
    '321...123',
    '.3211123.',
    '..32223..',
    '...333...',
    '....3....',
  ],
  // 12 — Gitter mit Stahlkern
  [
    '333333333',
    '3.3.3.3.3',
    '22S2S2S22',
    '2.2.2.2.2',
    '111111111',
    '1.1.1.1.1',
  ],
  // 13 — Wellen
  [
    '..3...3..',
    '.323.323.',
    '322232223',
    '211121112',
    '111111111',
    '1.1...1.1',
  ],
  // 14 — Festung
  [
    'S3333333S',
    '3.......3',
    '3.SSSSS.3',
    '3.S222S.3',
    '3.SSSSS.3',
    '3.......3',
    'S3333333S',
    '222222222',
  ],
  // 15 — Finale
  [
    '333333333',
    '3S3S3S3S3',
    '333333333',
    '2.2.2.2.2',
    '222222222',
    '2.2.2.2.2',
    '111111111',
    '1S1S1S1S1',
    '111111111',
  ],
];

/** Anzahl der handgebauten Level. */
export const HANDMADE_COUNT = LAYOUTS.length;

/**
 * Kulisse fuer den Titelbildschirm.
 *
 * Bei SNAKEone laeuft hinter dem Schriftzug das Spielfeld mit — ein leeres
 * Brett wirkt daneben unfertig. Die mittleren Reihen sind ausgespart, damit
 * der Titel nicht auf Steinen liegt.
 */
export function getDecorLayout() {
  return [
    '333333333',
    '3.2.2.2.3',
    '322222223',
    '.2.....2.',
    '..1...1..',
    '.1111111.',
    '111111111',
  ];
}

/** Deterministischer 32-Bit-PRNG (Mulberry32) — gleicher Seed, gleiches Level. */
function rng(seed) {
  let a = seed >>> 0;
  return function () {
    a = (a + 0x6D2B79F5) >>> 0;
    let t = a;
    t = Math.imul(t ^ (t >>> 15), t | 1);
    t ^= t + Math.imul(t ^ (t >>> 7), t | 61);
    return ((t ^ (t >>> 14)) >>> 0) / 4294967296;
  };
}

/**
 * Erzeugt ein Level jenseits der handgebauten Entwuerfe.
 * Spiegelt horizontal, damit das Ergebnis immer symmetrisch und damit
 * "entworfen" statt zufaellig wirkt.
 *
 * @param {number} level 1-basiert.
 * @returns {string[]}
 */
function generate(level) {
  const rand = rng(level * 2654435761);
  const cols = BRICK.COLS;
  const half = Math.ceil(cols / 2);

  const rows = Math.min(BRICK.MAX_ROWS, 5 + Math.floor((level - HANDMADE_COUNT) / 3));
  // Je weiter das Spiel, desto mehr zaehe Steine und Stahl.
  const depth = Math.min(3, 1 + Math.floor((level - HANDMADE_COUNT) / 5));
  const steelChance = Math.min(0.18, 0.03 * Math.floor((level - HANDMADE_COUNT) / 2));

  const out = [];
  for (let r = 0; r < rows; r++) {
    const left = [];
    for (let c = 0; c < half; c++) {
      const roll = rand();
      if (roll < 0.16) { left.push('.'); continue; }
      if (roll > 1 - steelChance && r > 0 && r < rows - 1) { left.push('S'); continue; }
      // Obere Reihen sind zaeher — der Ball muss sich hocharbeiten.
      const hp = Math.max(1, Math.min(depth, depth - Math.floor((r / rows) * depth) + 1));
      left.push(String(hp));
    }
    const right = left.slice(0, cols - half).reverse();
    out.push(left.concat(right).join(''));
  }

  // Ein vollstaendig leeres Level waere sofort gewonnen — absichern.
  if (!out.some((row) => /[1-3]/.test(row))) out.push('1'.repeat(cols));
  return out;
}

/**
 * Layout fuer ein Level.
 * @param {number} level 1-basiert.
 * @returns {string[]} Zeilen mit je BRICK.COLS Zeichen.
 */
export function getLevel(level) {
  const raw = level <= HANDMADE_COUNT
    ? LAYOUTS[level - 1]
    : generate(level);

  // Defensiv: auf exakte Spaltenzahl bringen, damit ein Tippfehler im Entwurf
  // nicht zu versetzten Steinen fuehrt.
  return raw
    .slice(0, BRICK.MAX_ROWS)
    .map((row) => row.slice(0, BRICK.COLS).padEnd(BRICK.COLS, '.'));
}
