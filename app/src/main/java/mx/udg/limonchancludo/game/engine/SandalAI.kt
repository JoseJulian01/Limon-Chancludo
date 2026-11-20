package mx.udg.limonchancludo.game.engine

import mx.udg.limonchancludo.game.model.Actor
import mx.udg.limonchancludo.game.model.Direction
import mx.udg.limonchancludo.game.model.Maze
import mx.udg.limonchancludo.game.model.Sandal
import mx.udg.limonchancludo.game.model.SandalType
import kotlin.math.abs

object SandalAI {

    /** Calcula la celda objetivo para cada tipo de sandía (estilo Pac-Man). */
    fun calculateTarget(sandal: Sandal, player: Actor, blinky: Sandal?, maze: Maze): Pair<Int, Int> {
        return when (sandal.type) {
            SandalType.BLINKY -> {
                // Persegue directamente la posición actual del jugador
                Pair(player.col, player.row)
            }

            SandalType.PINKY -> {
                // Embosca 4 casillas adelante de la dirección del jugador
                val offset = 4
                val targetCol = player.col + player.dir.dx * offset
                val targetRow = player.row + player.dir.dy * offset
                Pair(targetCol.coerceIn(0, maze.width - 1), targetRow.coerceIn(0, maze.height - 1))
            }

            SandalType.INKY -> {
                // Calcula objetivo usando posición del jugador y Blinky como referencia (trayectoria de pinza)
                val blinkyPos = blinky ?: sandal
                val diffCol = player.col - blinkyPos.col
                val diffRow = player.row - blinkyPos.row
                val targetCol = player.col + diffCol * 2
                val targetRow = player.row + diffRow * 2
                Pair(targetCol.coerceIn(0, maze.width - 1), targetRow.coerceIn(0, maze.height - 1))
            }

            SandalType.CLYDE -> {
                // Se comporta como Blinky si está lejos; si está a menos de 8 casillas, va a su hogar (Scatter)
                val distToPlayer = abs(sandal.col - player.col) + abs(sandal.row - player.row)
                if (distToPlayer > 8) {
                    Pair(player.col, player.row)
                } else {
                    sandal.homeTarget()
                }
            }
        }
    }

    /** Devuelve la mejor dirección válida hacia el objetivo usando distancia de Manhattan. */
    fun nextDirectionTowardsTarget(
        sandal: Sandal,
        target: Pair<Int, Int>,
        maze: Maze
    ): Direction {
        val (tc, tr) = target
        val validDirs = Direction.entries.filter { dir ->
            val (nc, nr) = maze.nextCell(sandal.col, sandal.row, dir)
            (nc != sandal.col || nr != sandal.row) && maze.isWalkable(nc, nr, forPlayer = false)
        }

        if (validDirs.isEmpty()) return sandal.dir

        // Evitar giros de 180° inmediatos a menos que sea la única opción
        val nonReverseDirs = validDirs.filter { it != sandal.dir.opposite }
        val choices = if (nonReverseDirs.isNotEmpty()) nonReverseDirs else validDirs

        return choices.minByOrNull { dir ->
            val (nc, nr) = maze.nextCell(sandal.col, sandal.row, dir)
            abs(nc - tc) + abs(nr - tr)
        } ?: choices.random()
    }
}
