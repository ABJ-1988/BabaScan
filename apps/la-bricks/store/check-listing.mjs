/**
 * LA-BRICKS — Store-Texte gegen die Play-Console-Grenzen pruefen
 *
 * Die Play Console lehnt zu lange Felder beim Speichern ab. Dieses Skript
 * prueft jeden Textblock in listing-de.md und listing-en.md, bevor etwas
 * kopiert wird — auch die angebotenen Alternativen, damit keine davon beim
 * spaeteren Austauschen ueber die Grenze faellt.
 *
 * Aufruf (aus apps/la-bricks/):
 *   node store/check-listing.mjs
 */

import { readFile } from 'node:fs/promises';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

const HERE = path.dirname(fileURLToPath(import.meta.url));

/** Ueberschrift -> Zeichengrenze des zugehoerigen Play-Console-Feldes. */
const LIMITS = [
  { match: /App-Name|App name/i,                          limit:   30 },
  { match: /Kurzbeschreibung|Short description/i,         limit:   80 },
  { match: /Vollst.ndige Beschreibung|Full description/i, limit: 4000 },
  { match: /Was ist neu|What's new/i,                     limit:  500 },
];

let fails = 0;

for (const file of ['listing-de.md', 'listing-en.md']) {
  console.log(`\n${file}`);
  const md = await readFile(path.join(HERE, file), 'utf8');

  // In Abschnitte an den Trennlinien zerlegen.
  for (const section of md.split(/\n---\n/)) {
    const heading = section.match(/^##\s+(.+)$/m)?.[1];
    if (!heading) continue;

    const rule = LIMITS.find((r) => r.match.test(heading));
    if (!rule) continue;

    // Alle eingerueckten Codebloecke des Abschnitts einsammeln.
    const blocks = [...section.matchAll(/```\n([\s\S]*?)```/g)]
      .map((m) => m[1].replace(/\n+$/, ''));

    if (blocks.length === 0) {
      console.log(`  FAIL  ${heading} — kein Textblock gefunden`);
      fails++;
      continue;
    }

    blocks.forEach((text, i) => {
      const n = [...text].length;   // Codepoints, nicht UTF-16-Einheiten
      const label = i === 0 ? heading : `${heading} (Alternative ${i})`;
      if (n > rule.limit) {
        console.log(`  FAIL  ${label}: ${n}/${rule.limit} Zeichen — ${n - rule.limit} zu viel`);
        fails++;
      } else {
        const pct = Math.round((n / rule.limit) * 100);
        console.log(`  ok    ${label}: ${n}/${rule.limit} (${pct}%)`);
      }
    });
  }
}

console.log(fails === 0
  ? '\nERGEBNIS: PASS — alle Felder innerhalb der Grenzen\n'
  : `\nERGEBNIS: ${fails} Feld(er) ueber der Grenze\n`);

process.exit(fails === 0 ? 0 : 1);
