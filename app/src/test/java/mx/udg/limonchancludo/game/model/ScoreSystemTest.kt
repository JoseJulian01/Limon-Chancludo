package mx.udg.limonchancludo.game.model

import org.junit.Assert.assertEquals
import org.junit.Test

class ScoreSystemTest {

    @Test
    fun `points for tiles are correct`() {
        assertEquals(ScoreSystem.LEMON_POINTS, ScoreSystem.pointsFor(Tile.LEMON))
        assertEquals(ScoreSystem.POWER_POINTS, ScoreSystem.pointsFor(Tile.POWER))
        assertEquals(0, ScoreSystem.pointsFor(Tile.WALL))
    }

    @Test
    fun `level bonus scales with level`() {
        assertEquals(500, ScoreSystem.levelBonus(1))
        assertEquals(1500, ScoreSystem.levelBonus(3))
    }

    @Test
    fun `extra life threshold works`() {
        val s = GameState(Maze(MazeLayouts.LEVEL_1), 1)
        s.addScore(9_990)
        assertEquals(3, s.lives)
        s.addScore(10)
        assertEquals(4, s.lives)
    }
}
