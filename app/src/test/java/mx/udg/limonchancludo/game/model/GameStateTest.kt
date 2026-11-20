package mx.udg.limonchancludo.game.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameStateTest {

    private fun state() = GameState(Maze(MazeLayouts.LEVEL_1), 1)

    @Test
    fun `starts with 3 lives and all lemons`() {
        val s = state()
        assertEquals(3, s.lives)
        assertEquals(GameStatus.RUNNING, s.status)
        assertEquals(259, s.lemonsRemaining)
        assertEquals(0, s.score)
    }

    @Test
    fun `eating a lemon gives 10 points and decrements the counter`() {
        val s = state()
        val points = s.eatLemon(1, 3) // fila 3, columna 1: limón
        assertEquals(ScoreSystem.LEMON_POINTS, points)
        assertEquals(10, s.score)
        assertEquals(258, s.lemonsRemaining)
        assertTrue(s.isEaten(1, 3))
    }

    @Test
    fun `eating the same lemon twice gives points only once`() {
        val s = state()
        s.eatLemon(1, 3)
        assertEquals(0, s.eatLemon(1, 3))
        assertEquals(10, s.score)
        assertEquals(258, s.lemonsRemaining)
    }

    @Test
    fun `eating a non-lemon cell gives nothing`() {
        val s = state()
        assertEquals(0, s.eatLemon(0, 0)) // muro
        assertEquals(0, s.score)
        assertEquals(259, s.lemonsRemaining)
    }

    @Test
    fun `power lemon gives 50 points`() {
        val s = state()
        // Buscar la primera celda con POWER en el mapa
        var col = -1
        var row = -1
        for (r in 0 until s.maze.height) {
            for (c in 0 until s.maze.width) {
                if (s.maze.tileAt(c, r) == Tile.POWER) {
                    col = c
                    row = r
                    break
                }
            }
            if (col >= 0) break
        }
        assertTrue(col >= 0)
        assertEquals(ScoreSystem.POWER_POINTS, s.eatLemon(col, row))
    }

    @Test
    fun `eating all lemons clears the level`() {
        val s = state()
        for (r in 0 until s.maze.height) {
            for (c in 0 until s.maze.width) {
                s.eatLemon(c, r)
            }
        }
        assertEquals(0, s.lemonsRemaining)
        assertEquals(GameStatus.LEVEL_CLEARED, s.status)
    }

    @Test
    fun `losing a life respawns the player`() {
        val s = state()
        s.player.snapTo(5, 5)
        s.loseLife()
        assertEquals(2, s.lives)
        assertEquals(s.maze.spawnCol, s.player.col)
        assertEquals(s.maze.spawnRow, s.player.row)
        assertEquals(GameStatus.RUNNING, s.status)
    }

    @Test
    fun `losing the last life is a game over`() {
        val s = state()
        s.loseLife()
        s.loseLife()
        s.loseLife()
        assertEquals(0, s.lives)
        assertEquals(GameStatus.GAME_OVER, s.status)
    }
}
