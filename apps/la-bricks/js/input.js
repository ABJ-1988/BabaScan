/**
 * LA-BRICKS — Eingabe
 *
 * Zeiger (Finger/Maus) werden ueber die inverse Homographie zurueck ins
 * logische Feld gerechnet — das Paddle steht damit exakt unter dem Finger,
 * obwohl das Spielfeld perspektivisch gekippt ist.
 *
 * Steuerung, analog zur Hinweiszeile bei SNAKEone:
 *   Wischen        — Paddle bewegen
 *   Tippen         — Ball abschiessen / Laser feuern
 *   Pfeiltasten    — Paddle bewegen (Desktop)
 *   Leertaste      — abschiessen / feuern
 *   P / Escape     — Pause
 */

import { FIELD } from './config.js';

export class Input {
  /**
   * @param {HTMLElement} target Element, das die Zeigerereignisse erhaelt.
   * @param {import('./projection.js').Projection} proj
   * @param {{onAim:(x:number)=>void, onTap:()=>void, onPause:()=>void}} handlers
   */
  constructor(target, proj, handlers) {
    this.target = target;
    this.proj = proj;
    this.on = handlers;

    this.pointerDown = false;
    this.pointerId = null;
    this.keys = new Set();
    /** Startposition, um Tippen von Wischen zu unterscheiden. */
    this._start = { x: 0, y: 0, t: 0 };
    this._moved = false;
    this._buf = { x: 0, y: 0 };

    this._bind();
  }

  /**
   * Zeigerereignisse auf Bedienelementen gehoeren nicht dem Spielfeld.
   *
   * Die Ereignisse werden am Rahmen abgegriffen, und Bildschirme wie Buttons
   * liegen darin — ohne diese Sperre wuerde ein Tippen auf "MODUS: ..." das
   * Spiel starten (pointerdown/-up sind bereits durchgelaufen, bevor der
   * Click-Handler stopPropagation aufrufen kann) und der Pause-Knopf wuerde
   * zugleich den Laser ausloesen.
   *
   * Sichtbare Bildschirme bringen ihre eigenen Click-Handler mit; waehrend
   * des Spiels sind sie ausgeblendet und das Spielfeld bekommt alles.
   */
  _isControl(e) {
    const t = e.target;
    return !!(t && t.closest && t.closest('button, .screen'));
  }

  _bind() {
    const t = this.target;
    const opts = { passive: false };

    t.addEventListener('pointerdown', this._onDown = (e) => {
      if (this.pointerId !== null || this._isControl(e)) return;
      this.pointerId = e.pointerId;
      this.pointerDown = true;
      this._moved = false;
      this._start = { x: e.clientX, y: e.clientY, t: performance.now() };
      try { t.setPointerCapture(e.pointerId); } catch { /* optional */ }
      this._aim(e);
      e.preventDefault();
    }, opts);

    t.addEventListener('pointermove', this._onMove = (e) => {
      if (!this.pointerDown || e.pointerId !== this.pointerId) return;
      if (Math.hypot(e.clientX - this._start.x, e.clientY - this._start.y) > 9) {
        this._moved = true;
      }
      this._aim(e);
      e.preventDefault();
    }, opts);

    const end = (e) => {
      if (e.pointerId !== this.pointerId) return;
      this.pointerDown = false;
      this.pointerId = null;
      // Kurzer Kontakt ohne nennenswerte Bewegung = Tippen.
      const dur = performance.now() - this._start.t;
      if (!this._moved && dur < 400) this.on.onTap?.();
      e.preventDefault();
    };

    t.addEventListener('pointerup', this._onUp = end, opts);
    t.addEventListener('pointercancel', this._onCancel = end, opts);

    // Kontextmenue bei langem Druck unterdruecken.
    t.addEventListener('contextmenu', this._onCtx = (e) => e.preventDefault());

    window.addEventListener('keydown', this._onKeyDown = (e) => {
      if (e.repeat) return;
      const k = e.key;
      this.keys.add(k);
      if (k === ' ' || k === 'Enter') { this.on.onTap?.(); e.preventDefault(); }
      if (k === 'p' || k === 'P' || k === 'Escape') { this.on.onPause?.(); e.preventDefault(); }
      if (k === 'ArrowLeft' || k === 'ArrowRight') e.preventDefault();
    });

    window.addEventListener('keyup', this._onKeyUp = (e) => this.keys.delete(e.key));
  }

  /** Zeigerposition -> logische X-Koordinate -> Handler. */
  _aim(e) {
    const rect = this.target.getBoundingClientRect();
    const sx = e.clientX - rect.left;
    const sy = e.clientY - rect.top;
    const p = this.proj.unproject(sx, sy, this._buf);

    // Jenseits des Horizonts kippt das Vorzeichen der homogenen Koordinate,
    // die Ruecktransformation liefert dort unbrauchbare Werte — verwerfen.
    if (!Number.isFinite(p.x) || p.y < -FIELD.H || p.y > FIELD.H * 2) return;

    this.on.onAim?.(p.x);
  }

  /** @returns {-1|0|1} Tastaturachse fuer das Paddle. */
  axis() {
    const l = this.keys.has('ArrowLeft') || this.keys.has('a') || this.keys.has('A');
    const r = this.keys.has('ArrowRight') || this.keys.has('d') || this.keys.has('D');
    return (r ? 1 : 0) - (l ? 1 : 0);
  }

  /** Gedrueckt-Halten — fuer Dauerfeuer mit dem Laser. */
  get held() { return this.pointerDown || this.keys.has(' '); }

  destroy() {
    const t = this.target;
    t.removeEventListener('pointerdown', this._onDown);
    t.removeEventListener('pointermove', this._onMove);
    t.removeEventListener('pointerup', this._onUp);
    t.removeEventListener('pointercancel', this._onCancel);
    t.removeEventListener('contextmenu', this._onCtx);
    window.removeEventListener('keydown', this._onKeyDown);
    window.removeEventListener('keyup', this._onKeyUp);
  }
}
