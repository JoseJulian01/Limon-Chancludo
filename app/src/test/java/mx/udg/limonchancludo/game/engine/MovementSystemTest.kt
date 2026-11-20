package mx.udg.limonchancludo.game.engine

import mx.udg.limonchancludo.game.model.Actor
import mx.udg.limonchancludo.game.model.Direction
import mx.udg.limonchancludo.game.model.Maze
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs

class MovementSystemTest {

    private val openMaze = Maze(
        listOf(
            "#####",
            "#...#",
            "#P..#",
            "#...#",
            "#####"
        )
    )

    private val blockedMaze = Maze(
        listOf(
            "#####",
            "#...#",
            "#P#.#",
            "#...#",
            "#####"
        )
    )

    private val tunnelMaze = Maze(
        listOf(
            "#####",
            "T.P.T",
            "#####"
        )
    )

    @Test
    fun `actor advances towards its target`() {
        val actor = Actor(openMaze.spawnCol, openMaze.spawnRow, Direction.RIGHT)
        MovementSystem.step(actor, openMaze, PLAYER_SPEED, 1f / 12f)

        assertTrue(actor.moving)
        assertEquals(2, actor.targetCol)
        assertEquals(2, actor.targetRow)
        assertTrue(abs(actor.progress - 0.5f) < 0.001f)
    }

    @Test
    fun `actor stops in front of a wall`() {
        val actor = Actor(blockedMaze.spawnCol, blockedMaze.spawnRow, Direction.RIGHT)
        MovementSystem.step(actor, blockedMaze, PLAYER_SPEED, 1f)

        assertEquals(1, actor.col)
        assertEquals(2, actor.row)
        assertEquals(0f, actor.progress, 0.001f)
        assertTrue(!actor.moving)
    }

    @Test
    fun `queued turn applies at the cell centre`() {
        val actor = Actor(openMaze.spawnCol, openMaze.spawnRow, Direction.RIGHT)
        actor.queuedDir = Direction.UP

        MovementSystem.step(actor, openMaze, PLAYER_SPEED, 1f / 12f)

        assertEquals(Direction.UP, actor.dir)
        assertEquals(1, actor.targetRow) // sube hacia la fila 1
        assertEquals(1, actor.col)
    }

    @Test
    fun `queued opposite direction reverses mid-cell`() {
        val actor = Actor(2, 2, Direction.LEFT)
        MovementSystem.step(actor, openMaze, PLAYER_SPEED, 1f / 12f)
        assertEquals(0.5f, actor.progress, 0.001f)

        actor.queuedDir = Direction.RIGHT // contrario: revierte en el acto
        MovementSystem.step(actor, openMaze, PLAYER_SPEED, 1f / 60f)

        assertEquals(Direction.RIGHT, actor.dir)
        assertEquals(1, actor.col)        // ya "está" en la celda destino anterior
        assertEquals(2, actor.targetCol)  // y vuelve hacia la original
        assertTrue(actor.progress > 0.5f)
    }

    @Test
    fun `tunnel wraps to the opposite edge`() {
        val actor = Actor(tunnelMaze.spawnCol, tunnelMaze.spawnRow, Direction.LEFT)

        // 3 celdas a 6 tiles/s = 0.5 s: col 2 -> 1 -> 0 -> (envuelve) 4
        MovementSystem.step(actor, tunnelMaze, PLAYER_SPEED, 0.5f)

        assertEquals(4, actor.col)
        assertEquals(1, actor.row)
        assertTrue(!actor.moving)
    }

    @Test
    fun `speed is distance over time in cells`() {
        val actor = Actor(openMaze.spawnCol, openMaze.spawnRow, Direction.RIGHT)
        MovementSystem.step(actor, openMaze, PLAYER_SPEED, 1f / 6f) // 1 celda
        assertEquals(2, actor.col)
        assertEquals(0f, actor.progress, 0.001f)
    }
}
