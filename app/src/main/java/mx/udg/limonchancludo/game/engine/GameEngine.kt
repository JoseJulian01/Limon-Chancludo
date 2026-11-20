package mx.udg.limonchancludo.game.engine

import mx.udg.limonchancludo.game.model.Direction
import mx.udg.limonchancludo.game.model.EnemySnapshot
import mx.udg.limonchancludo.game.model.GameEvent
import mx.udg.limonchancludo.game.model.GameSnapshot
import mx.udg.limonchancludo.game.model.GameState
import mx.udg.limonchancludo.game.model.GameStatus
import mx.udg.limonchancludo.game.model.Maze
import mx.udg.limonchancludo.game.model.MazeLayouts
import mx.udg.limonchancludo.game.model.Sandal
import mx.udg.limonchancludo.game.model.SandalState
import mx.udg.limonchancludo.game.model.SandalType
import mx.udg.limonchancludo.game.model.Sandals
import kotlin.math.abs
import kotlin.math.min

const val PLAYER_SPEED = 6.0f
const val FIXED_STEP = 1f / 60f

/** Duración del modo CHASE en segundos. */
const val CHASE_DURATION = 20.0f
/** Duración del modo SCATTER en segundos. */
const val SCATTER_DURATION = 7.0f
/** Duración del modo FRIGHTENED en segundos (tras comer limón de poder). */
const val FRIGHTENED_DURATION = 8.0f

/** Puntos al comer una sandalia asustada (acumulativo: 200, 400, 800, 1600). */
const val SANDAL_EATEN_BASE_POINTS = 200

class GameEngine(private val levelIndex: Int = 0) {

    val maze: Maze = Maze(MazeLayouts.level(levelIndex))
    var state: GameState = GameState(maze, levelIndex + 1)
        private set

    var elapsed: Float = 0f
        private set

    /** Tiempo acumulado en el modo actual (CHASE/SCATTER). */
    private var modeTimer: Float = 0f

    /** Sandalias del nivel (4 con personalidades distintas). */
    private val sandals = Sandals.ALL.toMutableList()

    /** Contador de sandalias comidas en modo FRIGHTENED (para puntos acumulativos). */
    private var sandalsEatenInFrightened: Int = 0

    init {
        state.setInvulnerable(SpawnSystem.INITIAL_INVULNERABILITY)
        resetSandals()
    }

    fun restart() {
        state = GameState(maze, levelIndex + 1)
        state.setInvulnerable(SpawnSystem.INITIAL_INVULNERABILITY)
        elapsed = 0f
        modeTimer = 0f
        state.currentMode = GameMode.CHASE
        sandalsEatenInFrightened = 0
        resetSandals()
    }

    private fun resetSandals() {
        val spawnPositions = listOf(
            Pair(9, 13),
            Pair(10, 13),
            Pair(11, 13),
            Pair(12, 13)
        )
        sandals.forEachIndexed { i, s ->
            val pos = spawnPositions[i % spawnPositions.size]
            s.snapTo(pos.first, pos.second)
            s.dir = Direction.entries[i % 4]
            s.state = SandalState.CHASE
        }
    }

    fun queueDirection(dir: Direction) {
        if (state.status == GameStatus.RUNNING) {
            state.player.queuedDir = dir
        }
    }

    fun step(dt: Float): List<GameEvent> {
        val events = mutableListOf<GameEvent>()
        if (state.status != GameStatus.RUNNING) return events

        elapsed += dt
        modeTimer += dt

        // --- Ciclo de modos CHASE / SCATTER ---
        when (state.currentMode) {
            GameMode.CHASE -> {
                if (modeTimer >= CHASE_DURATION) {
                    state.currentMode = GameMode.SCATTER
                    modeTimer = 0f
                    for (s in sandals) {
                        if (s.state != SandalState.EATEN && s.state != SandalState.FRIGHTENED) {
                            s.state = SandalState.SCATTER
                        }
                    }
                }
            }
            GameMode.SCATTER -> {
                if (modeTimer >= SCATTER_DURATION) {
                    state.currentMode = GameMode.CHASE
                    modeTimer = 0f
                    for (s in sandals) {
                        if (s.state != SandalState.EATEN && s.state != SandalState.FRIGHTENED) {
                            s.state = SandalState.CHASE
                        }
                    }
                }
            }
            GameMode.FRIGHTENED -> {
                // FRIGHTENED se gestiona mediante frightenedTimer, no por modeTimer
            }
        }

        // Movimiento del jugador
        MovementSystem.step(state.player, maze, PLAYER_SPEED, dt)

        // Actualizar temporizador de invulnerabilidad del jugador
        state.tickInvulnerable(dt)

        // Comer limones / poder
        events += CollisionSystem.eatUnderFeet(state)

        // Si se comió un limón de poder, activar FRIGHTENED
        if (events.any { it is GameEvent.PowerEaten }) {
            activateFrightened()
        }

        if (state.status == GameStatus.RUNNING) {
            // Buscar Blinky para el cálculo de Inky
            val blinky = sandals.firstOrNull { it.type == SandalType.BLINKY }

            // Mover sandalias con IA de objetivos (estilo Pac-Man)
            for (s in sandals) {
                // No sobrescribir estados FRIGHTENED o EATEN
                if (s.state != SandalState.FRIGHTENED && s.state != SandalState.EATEN) {
                    if (state.currentMode == GameMode.SCATTER) {
                        s.state = SandalState.SCATTER
                    } else {
                        s.state = SandalState.CHASE
                    }
                }

                val target = SandalAI.calculateTarget(s, state.player, blinky, maze)
                val speed = SandalAI.speedFor(s.state)
                MovementSystem.step(s, maze, speed, dt)

                if (!s.moving) {
                    s.dir = SandalAI.nextDirectionTowardsTarget(s, target, maze)
                    MovementSystem.step(s, maze, speed, dt)
                }

                // Si una sandalia EATEN llega a casa, se reinicia a CHASE/SCATTER según el modo global
                if (s.state == SandalState.EATEN && s.col == s.houseTarget().first && s.row == s.houseTarget().second) {
                    s.state = if (state.currentMode == GameMode.SCATTER) SandalState.SCATTER else SandalState.CHASE
                }
            }

            // Temporizador de FRIGHTENED
            state.tickFrightenedTimer(dt)
            if (state.frightenedTimer <= 0f) {
                for (s in sandals) {
                    if (s.state == SandalState.FRIGHTENED) {
                        s.state = SandalState.CHASE
                    }
                }
            }

            // Impacto jugador vs sandalias (usar coordenadas continuas)
            for (s in sandals) {
                val dx = abs(s.x - state.player.x)
                val dy = abs(s.y - state.player.y)
                val width = state.maze.width
                val dxWrap = min(dx, width - dx)
                if (dxWrap < SpawnSystem.HIT_RADIUS && dy < SpawnSystem.HIT_RADIUS) {
                    handleSandalImpact(s, events)
                }
            }
        }
        return events
    }

    /** Activa el modo FRIGHTENED en todas las sandalias. */
    private fun activateFrightened() {
        state.frightenedTimer = FRIGHTENED_DURATION
        for (s in sandals) {
            if (s.state != SandalState.EATEN) {
                s.state = SandalState.FRIGHTENED
            }
        }
        sandalsEatenInFrightened = 0
    }

    /** Maneja el impacto entre el jugador y una sandalia. */
    private fun handleSandalImpact(sandal: Sandal, events: MutableList<GameEvent>) {
        if (state.invulnerable > 0f) return

        when (sandal.state) {
            SandalState.FRIGHTENED -> {
                // El jugador se comió la sandalia
                sandal.state = SandalState.EATEN
                sandalsEatenInFrightened++
                val points = SANDAL_EATEN_BASE_POINTS * Math.pow(2.0, (sandalsEatenInFrightened - 1).toDouble()).toInt()
                state.addScore(points)
                events += GameEvent.SandalEaten(points)
                // La sandalia regresa a casa (EATEN -> houseTarget)
            }

            else -> {
                // El jugador muere
                state.loseLife()
                state.setInvulnerable(SpawnSystem.RESPAWN_INVULNERABILITY)
                resetSandals()
                events += GameEvent.PlayerDied
            }
        }
    }

    fun snapshot(): GameSnapshot {
        val s = state
        val p = s.player
        return GameSnapshot(
            maze = maze,
            level = s.level,
            playerCol = p.col,
            playerRow = p.row,
            playerTargetCol = p.targetCol,
            playerTargetRow = p.targetRow,
            playerProgress = p.progress,
            playerWraps = p.wrapsHorizontally,
            playerDir = p.dir,
            score = s.score,
            lives = s.lives,
            lemonsRemaining = s.lemonsRemaining,
            status = s.status,
            eaten = s.eaten.copyOf(),
            timeSeconds = elapsed,
            invulnerable = s.invulnerable,
            frightenedTimer = s.frightenedTimer,
            currentMode = s.currentMode,
            enemies = sandals.map { EnemySnapshot(it.x, it.y, it.dir, it.state, it.type) }
        )
    }
}

/** Modo de juego actual (alterna CHASE/SCATTER). */
enum class GameMode {
    CHASE,
    SCATTER,
    FRIGHTENED
}
