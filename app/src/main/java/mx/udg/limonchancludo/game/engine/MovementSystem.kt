package mx.udg.limonchancludo.game.engine

import mx.udg.limonchancludo.game.model.Actor
import mx.udg.limonchancludo.game.model.Direction
import mx.udg.limonchancludo.game.model.Maze

/**
 * Movimiento por celdas con giro encolado, reversa a mitre de celda
 * y envoltura del túnel. Velocidad en celdas por segundo.
 */
object MovementSystem {

    fun step(actor: Actor, maze: Maze, speedTilesPerSecond: Float, dt: Float) {
        var distance = speedTilesPerSecond * dt

        // Reversa inmediata (180°) a mitad de celda
        val queued = actor.queuedDir
        if (queued != null && queued == actor.dir.opposite && actor.moving && actor.progress > 0f) {
            actor.dir = queued
            actor.reverseInPlace()
            actor.queuedDir = null
        }

        while (distance > 0f) {
            if (!actor.moving) {
                // Centro de la celda: decidir hacia dónde seguir
                val wanted = actor.queuedDir
                if (wanted != null) {
                    if (wanted == actor.dir.opposite) {
                        actor.dir = wanted
                        actor.queuedDir = null
                    } else {
                        val (tc, tr) = maze.nextCell(actor.col, actor.row, wanted)
                        val opensNewCell = (tc != actor.col || tr != actor.row)
                        if (opensNewCell && maze.isWalkable(tc, tr)) {
                            actor.dir = wanted
                            actor.queuedDir = null
                        }
                        // Si no abre celda nueva se mantiene en cola para el próximo cruce
                    }
                }

                val (tc, tr) = maze.nextCell(actor.col, actor.row, actor.dir)
                val blocked = (tc == actor.col && tr == actor.row) || !maze.isWalkable(tc, tr)
                if (blocked) break
                actor.beginMove(tc, tr)
            }

            val remaining = 1f - actor.progress
            if (distance >= remaining) {
                distance -= remaining
                actor.arrive()
            } else {
                actor.advance(actor.progress + distance)
                distance = 0f
            }
        }
    }
}
