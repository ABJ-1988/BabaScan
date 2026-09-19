/**
 * LA-BRICKS — Theme-Bridge
 *
 * Liest die Design-Tokens aus css/theme.css zur Laufzeit aus und stellt sie
 * dem Canvas-Renderer zur Verfuegung. Dadurch gibt es genau EINE Quelle fuer
 * Farben und Glow — DOM und Canvas koennen nicht auseinanderlaufen.
 *
 * Folge: Ein Re-Theming von LA-Bricks ist ein Austausch von theme.css.
 */

/** Token-Name -> Schluessel im zurueckgegebenen Palettenobjekt. */
const COLOR_TOKENS = {
  '--c-bg':          'bg',
  '--c-bg-deep':     'bgDeep',
  '--c-green':       'green',
  '--c-green-core':  'greenCore',
  '--c-green-dim':   'greenDim',
  '--c-green-faint': 'greenFaint',
  '--c-green-grid':  'grid',
  '--c-magenta':     'magenta',
  '--c-magenta-core':'magentaCore',
  '--c-magenta-dim': 'magentaDim',
  '--c-magenta-deep':'magentaDeep',
  '--c-blue-edge':   'blueEdge',
  '--c-gold':        'gold',
  '--c-gold-core':   'goldCore',
  '--c-red':         'red',
  '--c-cyan':        'cyan',
  '--c-brick-1':     'brick1',
  '--c-brick-2':     'brick2',
  '--c-brick-3':     'brick3',
  '--c-brick-steel': 'brickSteel',
};

const NUMBER_TOKENS = {
  '--blur-bloom':      'bloom',
  '--blur-bloom-soft': 'bloomSoft',
  '--proj-tilt':       'tilt',
  '--proj-rotate':     'rotate',
  '--proj-horizon':    'horizon',
};

/** Fallbacks, falls getComputedStyle nichts liefert (z.B. sehr alte WebViews). */
const FALLBACK = {
  bg: '#070b07', bgDeep: '#030503',
  green: '#4dff6a', greenCore: '#c8ffd2',
  greenDim: 'rgba(77,255,106,0.45)', greenFaint: 'rgba(77,255,106,0.16)',
  grid: 'rgba(77,255,106,0.20)',
  magenta: '#ff3ce0', magentaCore: '#ffd6f8',
  magentaDim: 'rgba(255,60,224,0.45)', magentaDeep: '#c92fb0',
  blueEdge: '#6a8dff', gold: '#ffe066', goldCore: '#fffbe6',
  red: '#ff4d6a', cyan: '#4de8ff',
  brick1: '#4dff6a', brick2: '#ffe066', brick3: '#ff3ce0', brickSteel: '#7d8f9a',
  bloom: 14, bloomSoft: 26, tilt: 0.30, rotate: -0.055, horizon: 0.06,
};

let cache = null;

/**
 * Liest die Palette aus dem :root-Element.
 * @param {boolean} [force] Cache umgehen (nach einem Theme-Wechsel).
 * @returns {Record<string, string|number>}
 */
export function palette(force = false) {
  if (cache && !force) return cache;

  const out = { ...FALLBACK };

  try {
    const cs = getComputedStyle(document.documentElement);

    for (const [token, key] of Object.entries(COLOR_TOKENS)) {
      const v = cs.getPropertyValue(token).trim();
      if (v) out[key] = v;
    }
    for (const [token, key] of Object.entries(NUMBER_TOKENS)) {
      const v = parseFloat(cs.getPropertyValue(token));
      if (Number.isFinite(v)) out[key] = v;
    }
  } catch {
    /* Fallback-Palette bleibt bestehen. */
  }

  cache = out;
  return out;
}

/** Cache verwerfen — nach dem Einspielen eines anderen theme.css aufrufen. */
export function invalidate() { cache = null; }

/**
 * Farbe fuer einen Stein nach verbleibenden Treffern.
 * @param {number} hp
 * @param {boolean} steel
 */
export function brickColor(hp, steel) {
  const p = palette();
  if (steel) return p.brickSteel;
  if (hp >= 3) return p.brick3;
  if (hp === 2) return p.brick2;
  return p.brick1;
}

/**
 * Setzt Glow fuer den naechsten Zeichenvorgang.
 * @param {CanvasRenderingContext2D} ctx
 * @param {string} color
 * @param {number} [strength] Multiplikator auf --blur-bloom
 */
export function glow(ctx, color, strength = 1) {
  ctx.shadowColor = color;
  ctx.shadowBlur = palette().bloom * strength;
}

/** Glow abschalten. */
export function noGlow(ctx) {
  ctx.shadowColor = 'transparent';
  ctx.shadowBlur = 0;
}
