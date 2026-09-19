/**
 * LA-BRICKS — Service Worker
 *
 * App-Shell-Caching: nach dem ersten Start laeuft das Spiel vollstaendig
 * offline. Das ist fuer die Play-Store-Fassung wesentlich — eine TWA ohne
 * Netz darf keine Fehlerseite zeigen.
 *
 * Strategie:
 *   Navigation  — Netz zuerst, Cache als Rueckfall (frische Fassung gewinnt)
 *   Statisches  — Cache zuerst, im Hintergrund nachladen
 *
 * CACHE beim Ausliefern einer neuen Fassung hochzaehlen.
 */

const CACHE = 'labricks-v1';

const SHELL = [
  './',
  './index.html',
  './manifest.json',
  './css/theme.css',
  './css/game.css',
  './js/main.js',
  './js/game.js',
  './js/render.js',
  './js/projection.js',
  './js/entities.js',
  './js/physics.js',
  './js/powerups.js',
  './js/particles.js',
  './js/levels.js',
  './js/input.js',
  './js/audio.js',
  './js/storage.js',
  './js/theme.js',
  './js/config.js',
  './store/assets/icon.svg',
  './store/assets/icon-192.png',
  './store/assets/icon-512.png',
];

self.addEventListener('install', (e) => {
  e.waitUntil((async () => {
    const cache = await caches.open(CACHE);
    // Einzeln statt addAll: eine fehlende Datei soll nicht die gesamte
    // Installation scheitern lassen.
    await Promise.all(SHELL.map((url) =>
      cache.add(new Request(url, { cache: 'reload' })).catch(() => {})));
    self.skipWaiting();
  })());
});

self.addEventListener('activate', (e) => {
  e.waitUntil((async () => {
    const keys = await caches.keys();
    await Promise.all(keys.filter((k) => k !== CACHE).map((k) => caches.delete(k)));
    await self.clients.claim();
  })());
});

self.addEventListener('fetch', (e) => {
  const req = e.request;
  if (req.method !== 'GET') return;

  const url = new URL(req.url);
  if (url.origin !== self.location.origin) return;

  if (req.mode === 'navigate') {
    e.respondWith((async () => {
      try {
        const fresh = await fetch(req);
        const cache = await caches.open(CACHE);
        cache.put(req, fresh.clone());
        return fresh;
      } catch {
        return (await caches.match(req))
            ?? (await caches.match('./index.html'))
            ?? Response.error();
      }
    })());
    return;
  }

  e.respondWith((async () => {
    const hit = await caches.match(req);
    if (hit) {
      // Im Hintergrund auffrischen, ohne die Antwort zu verzoegern.
      fetch(req).then((res) => {
        if (res && res.ok) caches.open(CACHE).then((c) => c.put(req, res));
      }).catch(() => {});
      return hit;
    }
    try {
      const res = await fetch(req);
      if (res && res.ok) {
        const cache = await caches.open(CACHE);
        cache.put(req, res.clone());
      }
      return res;
    } catch {
      return Response.error();
    }
  })());
});
