package mx.udg.limonchancludo.game.engine

import mx.udg.limonchancludo.game.model.Direction
import mx.udg.limonchancludo.game.model.Maze
import kotlin.math.abs

/**
 * Pathfinding simple basado en BFS para el laberinto.
 * Devuelve la siguiente dirección que el enemigo debe tomar para acercarse al objetivo.
 */
object Pathing {

    /**
     * Calcula la mejor dirección válida hacia [targetCol, targetRow] desde [col, row],
     * evitando paredes y sin hacer giro 180° innecesario a menos que sea la única opción.
     *
     * @param currentDir dirección actual del enemigo (para evitar reversa inmediata)
     */
    fun nextDirectionTowardsTarget(
        col: Int,
        row: Int,
        targetCol: Int,
        targetRow: Int,
        currentDir: Direction,
        maze: Maze,
        forPlayer: Boolean = false
    ): Direction {
        val validDirs = Direction.entries.filter { dir ->
            val (nc, nr) = maze.nextCell(col, row, dir)
            (nc != col || nr != row) && maze.isWalkable(nc, nr, forPlayer = forPlayer)
        }

        if (validDirs.isEmpty()) return currentDir

        // Evitar giros de 180° inmediatos a menos que sea la única opción
        val nonReverseDirs = validDirs.filter { it != currentDir.opposite }
        val choices = if (nonReverseDirs.isNotEmpty()) nonReverseDirs else validDirs

        return choices.minByOrNull { dir ->
            val (nc, nr) = maze.nextCell(col, row, dir)
            abs(nc - targetCol) + abs(nr - targetRow)
        } ?: choices.random()
    }

    /**
     * Ejecuta BFS para saber si [target] es alcanzable desde [start] en el laberinto.
     * Útil para validar objetivos de IA.
     */
    fun isReachable(
        startCol: Int,
        startRow: Int,
        targetCol: Int,
        targetRow: Int,
        maze: Maze,
        forPlayer: Boolean = false
    ): Boolean {
        if (startCol == targetCol && startRow == targetRow) return true

        val visited = BooleanArray(maze.width * maze.height)
        val queue = ArrayDeque<Pair<Int, Int>>()
        queue.add(Pair(startCol, startRow))
        visited[startRow * maze.width + startCol] = true

        while (queue.isNotEmpty()) {
            val (c, r) = queue.removeFirst()
            for (dir in Direction.entries) {
                val (nc, nr) = maze.nextCell(c, r, dir)
                if (nr < 0 || nr >= maze.height) continue
                if (!maze.isWalkable(nc, nr, forPlayer = forPlayer)) continue
                val idx = nr * maze.width + nc
                if (visited[idx]) continue
                if (nc == targetCol && nr == targetRow) return true
                visited[idx] = true
                queue.add(Pair(nc, nr))
            }
        }
        return false
    }
}
