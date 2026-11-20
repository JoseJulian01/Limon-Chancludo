package mx.udg.limonchancludo.game.engine

import mx.udg.limonchancludo.game.model.Direction
import mx.udg.limonchancludo.game.model.EnemySnapshot
import mx.udg.limonchancludo.game.model.GameStatus
import mx.udg.limonchancludo.game.model.GameEvent
import mx.udg.limonchancludo.game.model.GameSnapshot
import mx.udg.limonchancludo.game.model.GameState
import mx.udg.limonchancludo.game.model.Maze
import mx.udg.limonchancludo.game.model.MazeLayouts
import mx.udg.limonchancludo.game.model.Sandal
import mx.udg.limonchancludo.game.model.SandalType
import mx.udg.limonchancludo.game.model.Sandals

const val PLAYER_SPEED = 6.0f
const val FIXED_STEP = 1f / 60f

class GameEngine(private val levelIndex: Int = 0) {

    val maze: Maze = Maze(MazeLayouts.level(levelIndex))
    var state: GameState = GameState(maze, levelIndex + 1)
        private set

    var elapsed: Float = 0f
        private set

    private val sandals = Sandals.ALL.toMutableList()

    init {
        state.setInvulnerable(SpawnSystem.INITIAL_INVULNERABILITY)
        resetSandals()
    }

    fun restart() {
        state = GameState(maze, levelIndex + 1)
        state.setInvulnerable(SpawnSystem.INITIAL_INVULNERABILITY)
        elapsed = 0f
        resetSandals()
    }

    private fun resetSandals() {
        sandals.forEachIndexed { i, s ->
            s.snapTo(s.col, 11)
            s.dir = Direction.UP
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
        MovementSystem.step(state.player, maze, PLAYER_SPEED, dt)

        events += CollisionSystem.eatUnderFeet(state)

        if (state.status == GameStatus.RUNNING) {
            val blinky = sandals.firstOrNull { it.type == SandalType.BLINKY }

            for (s in sandals) {
                val target = SandalAI.calculateTarget(s, state.player, blinky, maze)
                MovementSystem.step(s, maze, SpawnSystem.ENEMY_SPEED, dt)
                if (!s.moving) {
                    s.dir = SandalAI.nextDirectionTowardsTarget(s, target, maze)
                    MovementSystem.step(s, maze, SpawnSystem.ENEMY_SPEED, dt)
                }
            }

            state.tickInvulnerable(dt)
            if (SpawnSystem.checkImpact(state, sandals)) {
                state.loseLife()
                state.setInvulnerable(SpawnSystem.RESPAWN_INVULNERABILITY)
                resetSandals()
                events += GameEvent.PlayerDied
            }
        }
        return events
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
            enemies = sandals.map { EnemySnapshot(it.x, it.y, it.dir) }
        )
    }
}
