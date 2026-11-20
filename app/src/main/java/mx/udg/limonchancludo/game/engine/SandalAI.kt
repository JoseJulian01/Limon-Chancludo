package mx.udg.limonchancludo.game.engine

import mx.udg.limonchancludo.game.model.Actor
import mx.udg.limonchancludo.game.model.Direction
import mx.udg.limonchancludo.game.model.Maze
import mx.udg.limonchancludo.game.model.Sandal
import mx.udg.limonchancludo.game.model.SandalState
import mx.udg.limonchancludo.game.model.SandalType
import kotlin.math.abs
import kotlin.random.Random

object SandalAI {

    /** Velocidad base de las sandalias (casillas por segundo). */
    const val BASE_SPEED = 4.5f

    /** Velocidad cuando están asustadas (FRIGHTENED). */
    const val FRIGHTENED_SPEED = 3.0f

    /** Velocidad cuando han sido comidas y regresan a casa (EATEN). */
    const val EATEN_SPEED = 6.0f

    /**
     * Calcula la celda objetivo para una sandalia según su [type] y [state].
     *
     * @param sandal la sandalia
     * @param player el jugador (Actor)
     * @param blinky la sandalia Blinky (necesaria para el cálculo de Inky)
     * @param maze el laberinto
     */
    fun calculateTarget(
        sandal: Sandal,
        player: Actor,
        blinky: Sandal?,
        maze: Maze
    ): Pair<Int, Int> {
        return when (sandal.state) {
            SandalState.CHASE -> chaseTarget(sandal, player, blinky, maze)
            SandalState.SCATTER -> sandal.scatterTarget()
            SandalState.FRIGHTENED -> randomTarget(maze)
            SandalState.EATEN -> sandal.houseTarget()
        }
    }

    /** Objetivo de persecución según el tipo de sandalia. */
    private fun chaseTarget(
        sandal: Sandal,
        player: Actor,
        blinky: Sandal?,
        maze: Maze
    ): Pair<Int, Int> {
        return when (sandal.type) {
            SandalType.BLINKY -> {
                // Persegue directamente la posición actual del jugador
                clampToMaze(player.col, player.row, maze)
            }

            SandalType.PINKY -> {
                // Embosca 4 casillas adelante de la dirección del jugador
                val offset = 4
                val targetCol = player.col + player.dir.dx * offset
                val targetRow = player.row + player.dir.dy * offset
                clampToMaze(targetCol, targetRow, maze)
            }

            SandalType.INKY -> {
                // Trayectoria de "pinza": usa jugador y Blinky como referencia
                val blinkyPos = blinky ?: sandal
                val diffCol = player.col - blinkyPos.col
                val diffRow = player.row - blinkyPos.row
                val targetCol = player.col + diffCol * 2
                val targetRow = player.row + diffRow * 2
                clampToMaze(targetCol, targetRow, maze)
            }

            SandalType.CLYDE -> {
                // Si está lejos (>8 casillas): persigue como Blinky.
                // Si está cerca (<=8): va a su esquina (Scatter).
                val distToPlayer = abs(sandal.col - player.col) + abs(sandal.row - player.row)
                if (distToPlayer > 8) {
                    clampToMaze(player.col, player.row, maze)
                } else {
                    sandal.scatterTarget()
                }
            }
        }
    }

    /** Objetivo aleatorio caminable dentro del laberinto (para FRIGHTENED). */
    private fun randomTarget(maze: Maze): Pair<Int, Int> {
        val maxAttempts = 50
        for (attempt in 1..maxAttempts) {
            val c = Random.nextInt(maze.width)
            val r = Random.nextInt(maze.height)
            if (maze.isWalkable(c, r, forPlayer = false)) {
                return Pair(c, r)
            }
        }
        // Fallback: esquina superior izquierda
        return Pair(1, 1)
    }

    /**
     * Devuelve la mejor dirección válida hacia el [target], usando Pathing (BFS/Manhattan).
     * Si el estado es FRIGHTENED, elige una dirección válida al azar.
     */
    fun nextDirectionTowardsTarget(
        sandal: Sandal,
        target: Pair<Int, Int>,
        maze: Maze
    ): Direction {
        return when (sandal.state) {
            SandalState.FRIGHTENED -> randomValidDirection(sandal, maze)
            else -> Pathing.nextDirectionTowardsTarget(
                sandal.col, sandal.row, target.first, target.second, sandal.dir, maze
            )
        }
    }

    /** Elige una dirección válida al azar (para estado FRIGHTENED). */
    private fun randomValidDirection(sandal: Sandal, maze: Maze): Direction {
        val validDirs = Direction.entries.filter { dir ->
            val (nc, nr) = maze.nextCell(sandal.col, sandal.row, dir)
            (nc != sandal.col || nr != sandal.row) && maze.isWalkable(nc, nr, forPlayer = false)
        }
        return if (validDirs.isNotEmpty()) validDirs.random() else sandal.dir
    }

    /**
     * Clampeo de coordenadas objetivo para que queden dentro del laberinto.
     */
    private fun clampToMaze(col: Int, row: Int, maze: Maze): Pair<Int, Int> {
        return Pair(
            col.coerceIn(0, maze.width - 1),
            row.coerceIn(0, maze.height - 1)
        )
    }

    /** Velocidad de movimiento según el estado actual. */
    fun speedFor(state: SandalState): Float = when (state) {
        SandalState.FRIGHTENED -> FRIGHTENED_SPEED
        SandalState.EATEN -> EATEN_SPEED
        else -> BASE_SPEED
    }
}
