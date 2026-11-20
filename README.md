# 🍋 Limón Chancludo

Juego arcade tipo **Pac-Man** para Android desarrollado con **Jetpack Compose** y **Kotlin**.  
Controlas a un mono que debe comer limones por un laberinto mientras evitas ser atrapado por 4 sandías con IA propia.

---

## 📱 Características

- **Motor de juego puro** sin dependencias de Android (testeable con JUnit).
- **Bucle de juego a 60 FPS** con paso fijo e interpolación.
- **Laberinto 21×23** en ASCII con túnel que envuelve horizontalmente.
- **6 monos seleccionables** (Clásico, Ninja, Pirata, Astronauta, Chef, Robot) con desbloqueos por puntaje.
- **4 sandías con personalidades tipo Pac-Man**:
  - **Blinky (Rojo):** Persigue directamente al jugador.
  - **Pinky (Rosa):** Intenta emboscar 4 casillas adelante.
  - **Inky (Azul):** Trayectoria de "pinza" usando la posición del jugador y Blinky.
  - **Clyde (Naranja):** Persigue si está lejos; huye a su esquina si está cerca.
- **Sistema de puntuación**: 10 pts por limón, 50 por limón de poder, vida extra cada 10 000 pts.
- **Persistencia local** con DataStore (mejor puntaje, personaje, controles, sonido/vibración).
- **Audio** con SoundPool y feedback háptico.
- **Controles táctiles**: swipe o D-pad virtual.
- **Pantallas**: Splash, Menú, Selección de personaje, Juego, Pausa, Récords, Opciones, Créditos.
- **Interfaz en español**, tema oscuro forzado, retrato bloqueado.

---

## 🛠️ Stack tecnológico

| Componente | Versión |
|------------|---------|
| Lenguaje | Kotlin 2.2.10 |
| Gradle | 9.5 |
| Android Gradle Plugin | 9.3.3 |
| Compile SDK / Target SDK | 37 |
| Min SDK | 26 |
| Jetpack Compose BOM | 2026.02.01 |
| DataStore Preferences | 1.x |
| Pruebas | JUnit 4 + Compose Test |

---

## 🚀 Cómo compilar

### Requisitos previos

1. Instalar [Android Studio](https://developer.android.com/studio) o el SDK de Android.
2. Crear el archivo `local.properties` en la raíz del proyecto con la ruta del SDK:

```properties
sdk.dir=C:/ruta/al/Android/Sdk
```

### Comandos de verificación

Desde la carpeta del proyecto (`C:\ProyectosAndroids\app\src\main\java\mx\LimonChancludo`):

```bash
# Compilar APK de depuración
./gradlew :app:assembleDebug

# Ejecutar pruebas unitarias
./gradlew :app:testDebugUnitTest

# Ejecutar lint
./gradlew :app:lintDebug
```

El APK generado se encuentra en:
```
app/build/outputs/apk/debug/app-debug.apk
```

---

## 📂 Estructura del proyecto

```
LimonChancludo/
├── build.gradle.kts
├── settings.gradle.kts
├── app/
│   └── src/
│       └── main/
│           └── java/
│               └── mx/
│                   └── udg/
│                       └── limonchancludo/
│                           ├── data/
│                           │   ├── PreferencesRepository.kt
│                           │   └── RecordsRepository.kt
│                           ├── game/
│                           │   ├── assets/          (Sprites, SpriteFactory)
│                           │   ├── audio/           (GameAudio)
│                           │   ├── engine/          (GameEngine, MovementSystem, CollisionSystem, SpawnSystem, SandalAI)
│                           │   └── model/           (Actor, GameState, Maze, Tile, Sandal, ScoreSystem, etc.)
│                           └── ui/
│                               ├── components/      (GameCanvas, HudBar, DPad, SwipeArea, PixelButton)
│                               ├── screens/         (Home, Game, Options, Records, etc.)
│                               ├── state/           (Screens)
│                               ├── theme/           (Color, Type, Theme)
│                               └── GameViewModel.kt
```

---

## 🎮 Controles

| Acción | Control |
|--------|---------|
| Mover mono | Deslizar en pantalla o usar el D-pad virtual |
| Pausar | Botón "II" en la HUD o botón Atrás del sistema |
| Reiniciar nivel | Botón en pantallas de fin de nivel / Game Over |

---

## 📊 Reglas de juego

- Come todos los limones del nivel para limpiarlo.
- Los **limones de poder** otorgan 50 pts y activan efectos especiales.
- Si una sandía te atrapa, pierdes una vida (empiezas con 3).
- Al llegar a 0 vidas → **Game Over**.
- Cada 10 000 pts ganas una vida extra (máximo 9).
- El mejor puntaje se guarda automáticamente al terminar la partida.

---

## 🧪 Pruebas

El proyecto incluye más de 40 pruebas unitarias que cubren:

- `MazeTest`: dimensiones, spawn, túnel, conteo de limones.
- `MovementSystemTest`: movimiento por tiles, giros, reversa, túnel.
- `GameStateTest`: puntuación, vidas, extra life, game over.
- `ScoreSystemTest`: puntos por limón, bonus de nivel.
- `CollisionSystemTest`: detección de comida e impactos.
- `CharacterTest`: 6 personajes y desbloqueos.
- `PixelSpriteTest`: rasterizado de sprites pixelados.
- `GameEngineTest`: bucle del motor, eventos, reinicio.

---

## 📄 Licencia

Este proyecto es de carácter educativo y de aprendizaje en desarrollo de videojuegos para Android con Jetpack Compose.

---

**Desarrollado con ❤️ y Jetpack Compose.**  
*Limón Chancludo — Come limones, esquiva las sandías.*
