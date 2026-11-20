package mx.udg.limonchancludo.game.engine

import mx.udg.limonchancludo.game.model.Direction
import mx.udg.limonchancludo.game.model.GameEvent
import mx.udg.limonchancludo.game.model.GameStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameEngineTest {

    @Test
    fun `engine starts running with level 1 data`() {
        val engine = GameEngine(0)
        assertEquals(GameStatus.RUNNING, engine.state.status)
        assertEquals(1, engine.state.level)
        assertEquals(259, engine.state.lemonsRemaining)
    }

    @Test
    fun `queued direction is applied on the next step`() {
        val engine = GameEngine(0)
        engine.queueDirection(Direction.RIGHT)
        engine.step(1f / 60f)
        assertEquals(Direction.RIGHT, engine.state.player.dir)
    }

    @Test
    fun `step moves the player towards the queued direction`() {
        val engine = GameEngine(0)
        val beforeCol = engine.state.player.col
        engine.queueDirection(Direction.RIGHT)
        engine.step(1f / 12f) // media celda
        val p = engine.state.player
        assertTrue(p.moving)
        assertEquals(beforeCol + 1, p.targetCol)
        assertTrue(p.progress > 0f)
    }

    @Test
    fun `eating a lemon emits LemonEaten and scores`() {
        val engine = GameEngine(0)
        // El mono empieza en (10,18) mirando a la izquierda; hay limón en (9,18)
        val events = engine.step(1f / 12f)
        val lemon = events.filterIsInstance<GameEvent.LemonEaten>().singleOrNull()
        assertEquals(1, events.filterIsInstance<GameEvent.LemonEaten>().size)
        assertEquals(10, lemon?.points)
        assertEquals(10, engine.state.score)
        assertEquals(258, engine.state.lemonsRemaining)
    }

    @Test
    fun `power lemon emits PowerEaten`() {
        val engine = GameEngine(0)
        // Buscar una celda POWER cercana al inicio y colocar al mono en su vecina
        var powerCol = -1
        var powerRow = -1
        outer@ for (r in 0 until engine.maze.height) {
            for (c in 0 until engine.maze.width) {
                if (engine.maze.tileAt(c, r) == mx.udg.limonchancludo.game.model.Tile.POWER) {
                    powerCol = c
                    powerRow = r
                    break@outer
                }
            }
        }
        assertTrue(powerCol >= 0)

        val p = engine.state.player
        p.snapTo(powerCol, powerRow)
        val events = engine.step(1f / 60f)
        val power = events.filterIsInstance<GameEvent.PowerEaten>().singleOrNull()
        assertEquals(50, power?.points)
        assertEquals(50, engine.state.score)
    }

    @Test
    fun `no events while not running`() {
        val engine = GameEngine(0)
        // Vaciar todos los limones para forzar LEVEL_CLEARED
        for (r in 0 until engine.maze.height) {
            for (c in 0 until engine.maze.width) {
                engine.state.eatLemon(c, r)
            }
        }
        assertEquals(GameStatus.LEVEL_CLEARED, engine.state.status)
        val events = engine.step(1f / 60f)
        assertTrue(events.isEmpty())
    }

    @Test
    fun `snapshot copies eaten array safely`() {
        val engine = GameEngine(0)
        val snap = engine.snapshot()
        engine.state.eatLemon(0, 1)
        assertTrue(snap.eaten[1 * engine.maze.width + 0].not())
    }

    @Test
    fun `restart resets the score but keeps the level`() {
        val engine = GameEngine(0)
        engine.step(1f / 12f)
        assertTrue(engine.state.score > 0)
        engine.restart()
        assertEquals(0, engine.state.score)
        assertEquals(259, engine.state.lemonsRemaining)
        assertEquals(GameStatus.RUNNING, engine.state.status)
    }
}
