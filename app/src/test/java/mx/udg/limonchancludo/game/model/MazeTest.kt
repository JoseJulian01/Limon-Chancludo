package mx.udg.limonchancludo.game.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MazeTest {

    private val maze = Maze(MazeLayouts.LEVEL_1)

    @Test
    fun `level 1 has the expected dimensions`() {
        assertEquals(21, maze.width)
        assertEquals(23, maze.height)
    }

    @Test
    fun `player spawn is at row 18 col 10`() {
        assertEquals(10, maze.spawnCol)
        assertEquals(18, maze.spawnRow)
        assertEquals(Tile.SPAWN, maze.tileAt(10, 18))
    }

    @Test
    fun `level 1 has 259 lemons`() {
        assertEquals(259, maze.lemonCount())
    }

    @Test
    fun `walls and door block the player but not the enemies`() {
        assertFalse(maze.isWalkable(0, 0))          // muro
        assertFalse(maze.isWalkable(10, 7))         // puerta '-'
        assertTrue(maze.isWalkable(10, 7, forPlayer = false)) // enemigos sí
        assertTrue(maze.isWalkable(10, 18))         // inicio
    }

    @Test
    fun `tunnel wraps horizontally on its row`() {
        assertEquals(Tile.TUNNEL, maze.tileAt(-1, 11))
        assertEquals(Tile.TUNNEL, maze.tileAt(21, 11))
        assertEquals(20 to 11, maze.nextCell(0, 11, Direction.LEFT))
        assertEquals(0 to 11, maze.nextCell(20, 11, Direction.RIGHT))
        // En filas sin túnel no hay envoltura
        assertEquals(0 to 1, maze.nextCell(0, 1, Direction.LEFT))
    }

    @Test
    fun `vertical exits are blocked at the borders`() {
        assertEquals(0 to 0, maze.nextCell(0, 0, Direction.UP))
    }
}
