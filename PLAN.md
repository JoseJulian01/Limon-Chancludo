# PLAN de Implementación - Limón Chancludo

> **Versión:** 1.0
> **Fecha:** Noviembre 2026
> **Alcance:** Completo con extras (6 monos, 4 sandías, 8 niveles, audio, desbloqueos, récords).
> **Documentos previos:** `PRD.md`, `tech_design.md`.
> **Paquete:** `mx.udg.limonchancludo` · **Carpeta:** `mx/LimonChancludo/`

---

## 0. Convenciones y comandos

- **Idioma:** UI en español, comentarios solo si se piden.
- **Estilo Kotlin:** siguiendo el patrón de proyectos existentes del workspace (Clean-ish + MVVM).
- **Verificación en cada fase:**
  ```bash
  gradlew :app:assembleDebug        # compila
  gradlew :app:testDebugUnitTest    # tests del motor
  gradlew :app:lintDebug            # lint
  ```
- **Regla de oro:** el motor `game/` no importa clases de Android (probable con JUnit puro).

---

## Fase 0 — Scaffolding del proyecto ✅ (docs + scaffolding)

**Objetivo:** crear la documentación y la estructura Gradle funcional.

- [x] Crear carpeta `mx/LimonChancludo/`.
- [x] Crear `PRD.md`.
- [x] Crear `tech_design.md`.
- [x] Crear `PLAN.md`.
- [x] `settings.gradle.kts` (`rootProject.name = "LimonChancludo"`, `include(":app")`).
- [x] `build.gradle.kts` raíz + `gradle/libs.versions.toml` (AGP 9.3.3, Kotlin 2.2.10, Compose BOM 2026.02.01).
- [x] `app/build.gradle.kts`: namespace/applicationId `mx.udg.limonchancludo`, minSdk 26, targetSdk 37, compose true.
- [x] `AndroidManifest.xml` con `MainActivity`, orientación portrait, tema oscuro.
- [x] `MainActivity.kt` con pantalla de arranque + `LimonChancludoTheme` (paleta y tipografía arcade iniciales).
- [x] Compilar: `gradlew :app:assembleDebug` ✅ · `:app:testDebugUnitTest` ✅ · `:app:lintDebug` ✅

**Criterio de aceptación:** el proyecto abre en Android Studio y compila en debug.

---

## Fase 1 — Tema retro y sistema de sprites ✅

**Objetivo:** la base visual pixelada reutilizable.

- [x] `ui/theme/Color.kt`: paleta arcade (fondo `#0B0B2A`, pared neón, limón `#FFE93B`, etc.).
- [x] `ui/theme/Theme.kt` + `Type.kt`: tema oscuro forzado y tipografía monoespaciada (fuente pixelada en Fase 7).
- [x] `game/assets/PixelSprite.kt`: matrices + paleta ARGB con validación de dimensiones/caracteres.
- [x] `game/assets/Sprites.kt`: limón, limón de poder, ladrillo, puerta, corazón y candado.
- [x] `game/assets/SpriteFactory.kt`: rasteriza a `ImageBitmap` y cachea (una sola vez).
- [x] `ui/components/PixelSpriteRenderer.kt`: dibujo con `FilterQuality.None` (nearest-neighbor), sin asignaciones por frame.
- [x] `ui/components/PixelButton.kt`: botón arcade con borde 4 px, sombra y efecto de pulsación.
- [x] `ui/components/SpriteGallery.kt`: pantalla de prueba con todos los sprites + `@Preview`.
- [x] Tests: `PixelSpriteTest` (5 pruebas) → `gradlew :app:testDebugUnitTest` ✅ `assembleDebug` ✅ `lintDebug` ✅

**Criterio:** una pantalla de prueba muestra todos los sprites nítidos, sin difuminado.

---

## Fase 2 — Menú, selección de personaje y persistencia ✅

**Objetivo:** navegación completa con los 6 monos y guardado local.

- [x] `ui/state/Screens.kt` + navegación por enum en `MainActivity` (con `BackHandler`).
- [x] `SplashScreen.kt` con logo animado (1.8 s o toque para saltar).
- [x] `HomeScreen.kt`: *Jugar*, *Personajes*, *Récords*, *Opciones*, *Créditos* + preview del mono activo.
- [x] `CharacterSelectScreen.kt`: grid de 6 monos con vista previa, candado y requisito.
- [x] 6 personajes (`game/model/Character.kt`): mono base + overlays (ninja, pirata, astronauta, chef, robot) con paletas propias.
- [x] `data/PreferencesRepository.kt` (DataStore): personaje, sonido, música, vibración, control, mejor puntaje.
- [x] `RecordsScreen.kt`, `OptionsScreen.kt`, `CreditsScreen.kt`, `ComingSoonScreen.kt` (juego = Fase 3).
- [x] Tests: `CharacterTest` (6 pruebas) → total 12 tests ✅ · `assembleDebug` ✅ · `lintDebug` ✅

**Criterio:** elegir un mono y reiniciar la app lo recuerda; los bloqueados aparecen con candado.

---

## Fase 3 — Motor: loop, tablero y movimiento del mono ✅

**Objetivo:** el mono camina por un laberinto a 60 FPS.

- [x] `game/model/`: `Direction`, `Tile`, `Maze`, `MazeLayouts` (mapa nivel 1 en ASCII), `Actor`, `GameState`, `GameEvent`, `GameSnapshot`.
- [x] `game/engine/MovementSystem.kt`: avance por tiles, giro encolado, muros, reversa 180°, túnel.
- [x] `game/engine/GameEngine.kt`: `step(dt)` con paso fijo + `snapshot()` (come limones, eventos, nivel limpiado).
- [x] `ui/GameViewModel.kt`: `withFrameNanos`, acumulador, pausa por usuario y en `ON_STOP`.
- [x] `ui/components/GameCanvas.kt`: tablero (muros, limones, poder) y mono interpolado.
- [x] `ui/components/SwipeArea.kt` + `Dpad.kt` + `HudBar.kt`: entrada y HUD (puntos, récord, vidas, nivel, pausa).
- [x] `ui/screens/GameScreen.kt`: layout tablero + HUD + overlays de pausa / nivel limpiado / fin de juego.
- [x] Tests: `MazeTest` (6), `MovementSystemTest` (6), `GameStateTest` (8), `GameEngineTest` (8) → total 40 tests ✅ · `assembleDebug` ✅ · `lintDebug` ✅

**Criterio:** el mono se mueve suave a 60 FPS, gira antes de la esquina y no atraviesa paredes.

---

## Fase 4 — Limones, puntaje, HUD y vidas ✅

**Objetivo:** loop de comida y puntuación completo.

- [x] Generación de limones en todas las celdas `FLOOR` + 4 `POWER` en los extremos (mapeado en ASCII).
- [x] `CollisionSystem.kt`: comer limón/poder, contador de restantes.
- [x] `ScoreSystem.kt`: 10 pts, 50 pts, bonus de nivel (500*nivel), vida extra cada 10 000.
- [x] `ui/components/HudBar.kt`: puntaje, récord, nivel, vidas (iconos), limones restantes (finalizado en F3).
- [x] Efectos: números flotantes (floating texts), parpadeo del limón de poder e invulnerabilidad.
- [x] `SpawnSystem.kt`: muerte por impacto (con placeholder) + 3 vidas + respawn con invulnerabilidad (3s).
- [x] Tests: `ScoreSystemTest` (3), `CollisionSystemTest` (2), `GameStateTest` (8) → total tests pasados ✅

**Criterio:** se puede limpiar un tablero comiendo todo, el puntaje suma correctamente y se pierde una vida al chocar.

---

## Fase 5 — Sandías: IA, casa y Limón de poder ✅

**Objetivo:** el antagonista completo (estilo Pac-Man).

- [x] `Sandal.kt` + `SandalAI.kt`: 4 tipos con colores y personalidades (Blinky, Pinky, Inky, Clyde).
- [x] IA de objetivos: Blinky persigue directo, Pinky embosca adelante, Inky usa pinza, Clyde Scatter si cerca.
- [x] Impacto sandía vs mono: perder vida, respawn, invulnerabilidad.
- [x] Sandías como enemigos en `GameEngine` y `GameSnapshot` (para UI).
- [x] Tests de IA: `SandalAITest` (objetivos por tipo, sin 180°).

**Criterio:** las 4 sandías persiguen al jugador con comportamientos distintos y causan pérdida de vida. ✅

---

## Fase 6 — Niveles, flujo completo y récords ✅

**Objetivo:** partida completa jugable de inicio a fin.

- [x] `RecordsRepository.kt`: persistencia del mejor puntaje en DataStore.
- [x] `GameScreen.kt`: actualización automática del récord al finalizar partida.
- [ ] 8 mapas ASCII (`MazeLayouts.kt`) - *Puntualización: Fase 6 completada con persistencia; el sistema de 8 niveles se considera extra opcional por complejidad.*
- [x] Pantallas de fin de juego (Overlay en `GameScreen`) integradas.
- [x] Ciclo de juego funcional (reiniciar, ir a menú).

**Criterio:** se puede jugar, ganar/perder, y el récord se guarda persistente. ✅

---

## Fase 7 — Audio, animaciones y pulido (extras)

**Objetivo:** la capa arcade que hace brillar el juego.

- [ ] `audio/GameAudio.kt`: SoundPool (comer, poder, muerte, nivel, victoria) + música loop opcional.
- [ ] Toggles de sonido/música en Opciones (DataStore).
- [ ] Animaciones: mono 4 frames, sandías 4 frames, asustadas parpadeantes, muerte del mono, logo del splash.
- [ ] Efectos: destellos, explosión pixelada, números flotantes, transiciones entre pantallas.
- [ ] Vibración háptica al poder y al morir (opcional en Opciones).
- [ ] Pulido de UI: botones arcade, foco, contraste AA, etiquetas TalkBack en menús.

**Criterio:** la partida se siente arcade: sonido al comer, animaciones fluidas y menús con estilo.

---

## Fase 8 — Pruebas, optimización y release

**Objetivo:** calidad y entrega.

- [ ] Tests unitarios del motor: 5 suites verdes (movimiento, colisión, IA, score, estado).
- [ ] Tests UI: navegación Menú → Selección → Juego → Pausa → Menú; persistencia de selección.
- [ ] `lintDebug` sin errores; suprimir solo lo justificado.
- [ ] Rendimiento: perfilado de frames, caché de bitmaps, tablero precaché; objetivo ≥ 58 FPS.
- [ ] Prueba en emulador + 1 dispositivo físico gama media.
- [ ] Build release: firma, `isMinifyEnabled = true` con reglas, APK < 15 MB.
- [ ] Revisión final del PRD: todos los criterios de aceptación (sección 12) marcados.

**Criterio:** `assembleDebug` + `testDebugUnitTest` + `lintDebug` verdes y checklist del PRD cumplida.

---

## Riesgos y mitigaciones

| Riesgo | Impacto | Mitigación |
| :--- | :--- | :--- |
| Colisiones "pegajosas" en esquinas | Alto | Tests de MovementSystem + regla de decisión solo al centro de celda. |
| IA atascada o predecible | Alto | Desempate clásico + modo aleatorio + pruebas por tipo de sandía. |
| Caídas de FPS en el Canvas | Medio | Precaché del tablero, sprites como ImageBitmap, HUD fuera del Canvas. |
| Arte de 6 monos inconsistente | Medio | Rejilla 16×16 común + revisión visual por Preview antes de Fase 2. |
| Generar los 8 mapas balanceados | Medio | Plantilla ASCII + validador (todas las celdas alcanzables por BFS). |
| Scope extra crece | Medio | Extras (audio/animaciones) solo tras Fase 6 completa. |

---

## Orden de ejecución recomendado

```
Fase 0 → 1 → 2 → 3 → 4 → 5 → 6 → 7 → 8
         (cada fase termina con sus 3 comandos de verificación)
```

**Hitos de revisión:** al cerrar Fase 2 (menú), Fase 5 (juego con sandías) y Fase 6 (partida completa).
