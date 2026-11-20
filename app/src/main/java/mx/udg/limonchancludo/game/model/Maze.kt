package mx.udg.limonchancludo.game.model

/**
 * Laberinto 21x23 en ASCII. El túnel envuelve horizontalmente.
 */
class Maze(rows: List<String>) {

    val width: Int
    val height: Int
    private val tiles: Array<Tile>

    val spawnCol: Int
    val spawnRow: Int

    init {
        require(rows.isNotEmpty()) { "Mapa vacío" }
        height = rows.size
        width = rows.first().length
        require(rows.all { it.length == width }) { "Filas de anchos distintos" }
        tiles = Array(width * height) { i ->
            Tile.fromChar(rows[i / width][i % width])
        }
        val spawnIndex = tiles.indexOf(Tile.SPAWN)
        require(spawnIndex >= 0) { "El mapa no tiene celda 'P'" }
        spawnCol = spawnIndex % width
        spawnRow = spawnIndex / width
    }

    fun tileAt(col: Int, row: Int): Tile {
        if (row < 0 || row >= height) return Tile.WALL
        val c = col.mod(width) // túnel: envuelve en horizontal
        return tiles[row * width + c]
    }

    fun isWalkable(col: Int, row: Int, forPlayer: Boolean = true): Boolean {
        if (row < 0 || row >= height) return false
        val tile = tileAt(col, row)
        return if (forPlayer) tile.walkableForPlayer else tile != Tile.WALL
    }

    /** Siguiente celda en [dir] aplicando el envoltura del túnel. */
    fun nextCell(col: Int, row: Int, dir: Direction): Pair<Int, Int> {
        val nc = col + dir.dx
        val nr = row + dir.dy
        if (nr < 0 || nr >= height) return col to row
        if (nc < 0 || nc >= width) {
            val onTunnelRow = tileAt(0, row) == Tile.TUNNEL &&
                tileAt(width - 1, row) == Tile.TUNNEL
            return if (onTunnelRow && dir.dx != 0) {
                (if (nc < 0) width - 1 else 0) to row
            } else {
                col to row
            }
        }
        return nc to nr
    }

    fun lemonCount(): Int = tiles.count { it.isLemon }
}
