/**
 * LA-BRICKS — Zentrale Konfiguration
 *
 * Das Spiel rechnet durchgaengig in LOGISCHEN Koordinaten (FIELD.W x FIELD.H).
 * Skalierung auf die Geraeteaufloesung und die perspektivische Kippung
 * passieren ausschliesslich im Renderer (js/render.js + js/projection.js).
 * Spiellogik ist dadurch aufloesungs- und stilunabhaengig.
 */

export const FIELD = Object.freeze({
  W: 360,
  H: 600,
  /** Oberkante des Spielraums — darueber liegt das HUD. */
  TOP: 44,
  /** Unterhalb dieser Linie ist der Ball verloren. */
  FLOOR: 600,
});

export const PADDLE = Object.freeze({
  W: 74,
  H: 11,
  Y: 556,
  MIN_W: 46,
  MAX_W: 128,
  /** Wie stark der Auftreffpunkt den Abprallwinkel dreht (Radiant, max). */
  MAX_BOUNCE_ANGLE: 1.05,
  /** Anteil der Paddle-Eigenbewegung, der auf den Ball uebergeht. */
  SPIN: 0.22,
});

export const BALL = Object.freeze({
  R: 5.2,
  SPEED_START: 300,
  SPEED_MAX: 585,
  /** Zusatzgeschwindigkeit pro Level. */
  SPEED_PER_LEVEL: 15,
  /** Beschleunigung pro zerstoertem Stein. */
  SPEED_PER_BRICK: 0.7,
  /** Minimaler |vy|-Anteil, damit der Ball nicht horizontal "haengt". */
  MIN_VY_RATIO: 0.22,
});

export const BRICK = Object.freeze({
  COLS: 9,
  MARGIN_X: 10,
  GAP: 4,
  H: 17,
  TOP: 78,
  MAX_ROWS: 13,
});

/** Aus COLS/MARGIN/GAP abgeleitete Steinbreite. */
export const BRICK_W =
  (FIELD.W - 2 * BRICK.MARGIN_X - (BRICK.COLS - 1) * BRICK.GAP) / BRICK.COLS;

export const POWERUP = Object.freeze({
  W: 22,
  H: 12,
  FALL_SPEED: 115,
  /** Grundwahrscheinlichkeit, dass ein zerstoerter Stein eines fallen laesst. */
  DROP_CHANCE: 0.14,
  /** Dauer der zeitlich begrenzten Effekte in Sekunden. */
  DURATION: 11,
});

export const LASER = Object.freeze({
  W: 2.5,
  H: 13,
  SPEED: 520,
  /** Sekunden zwischen zwei Schuessen. */
  COOLDOWN: 0.34,
});

export const RULES = Object.freeze({
  LIVES_START: 3,
  LIVES_MAX: 6,
  SCORE_PER_HIT: 10,
  SCORE_PER_DESTROY: 50,
  SCORE_PER_POWERUP: 75,
  /** Bonus pro verbleibendem Leben beim Levelabschluss. */
  SCORE_LEVEL_CLEAR: 250,
  SCORE_LIFE_BONUS: 100,
  /** Punkte-Schwelle fuer ein Extraleben. */
  EXTRA_LIFE_EVERY: 5000,
});

/** Feste Simulationsschrittweite (s) — entkoppelt Physik von der Framerate. */
export const STEP = 1 / 120;
/** Obergrenze, damit ein Tab-Wechsel keinen Riesensprung erzeugt. */
export const MAX_FRAME_DT = 0.25;

/**
 * Einstellbare Optionen. Die Reihenfolge der Werte ist zugleich die
 * Durchschalt-Reihenfolge der "KEY: WERT"-Buttons (wie bei SNAKEone).
 */
export const OPTIONS = Object.freeze({
  modus:     { label: 'MODUS',     values: ['KLASSIK', 'ENDLOS', 'PROFI'] },
  sound:     { label: 'SOUND',     values: ['AN', 'AUS'] },
  klang:     { label: 'KLANG',     values: ['LASER', 'RETRO', 'PUR'] },
  vibration: { label: 'VIBRATION', values: ['STARK', 'SANFT', 'AUS'] },
});

export const DEFAULT_SETTINGS = Object.freeze({
  modus: 'KLASSIK',
  sound: 'AN',
  klang: 'LASER',
  vibration: 'STARK',
});

/** Modus-abhaengige Parameter. */
export const MODES = Object.freeze({
  KLASSIK: { speedMul: 1.00, lives: 3, dropMul: 1.00 },
  ENDLOS:  { speedMul: 0.92, lives: 5, dropMul: 1.35 },
  PROFI:   { speedMul: 1.18, lives: 1, dropMul: 0.70 },
});
