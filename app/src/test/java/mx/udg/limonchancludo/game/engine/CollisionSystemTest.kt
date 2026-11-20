package mx.udg.limonchancludo.game.engine

import mx.udg.limonchancludo.game.model.Actor
import mx.udg.limonchancludo.game.model.Direction
import mx.udg.limonchancludo.game.model.GameState
import mx.udg.limonchancludo.game.model.Maze
import mx.udg.limonchancludo.game.model.MazeLayouts
import mx.udg.limonchancludo.game.model.Tile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CollisionSystemTest {

    @Test
    fun `eatUnderFeet detects lemons and awards points`() {
        val s = GameState(Maze(MazeLayouts.LEVEL_1), 1)
        s.player.snapTo(1, 3) // fila 3, col 1: limón
        val events = CollisionSystem.eatUnderFeet(s)
        assertTrue(events.any { it is mx.udg.limonchancludo.game.model.GameEvent.LemonEaten })
        assertEquals(10, s.score)
    }

    @Test
    fun `checkImpact detects collision`() {
        val s = GameState(Maze(MazeLayouts.LEVEL_1), 1)
        val enemies = listOf(Actor(s.player.col, s.player.row, Direction.LEFT))
        s.setInvulnerable(0f)
        assertTrue(SpawnSystem.checkImpact(s, enemies))
    }
}
