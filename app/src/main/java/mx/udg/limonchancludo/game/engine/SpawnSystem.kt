package mx.udg.limonchancludo.game.engine

import mx.udg.limonchancludo.game.model.Actor
import mx.udg.limonchancludo.game.model.GameState
import mx.udg.limonchancludo.game.model.Maze
import kotlin.math.abs
import kotlin.math.min

object SpawnSystem {
    const val INITIAL_INVULNERABILITY = 1.5f
    const val RESPAWN_INVULNERABILITY = 1.5f
    const val ENEMY_SPEED = 4.0f
    const val HIT_RADIUS = 0.6f

    fun stepEnemies(enemies: List<Actor>, maze: Maze, dt: Float) {
        for (enemy in enemies) {
            MovementSystem.step(enemy, maze, ENEMY_SPEED, dt)
            if (!enemy.moving) {
                enemy.dir = enemy.dir.opposite
            }
        }
    }

    fun checkImpact(state: GameState, enemies: List<Actor>): Boolean {
        if (state.invulnerable > 0f) return false
        val p = state.player
        val width = state.maze.width
        return enemies.any { e ->
            val dx = abs(e.x - p.x)
            val dxWrap = min(dx, width - dx)
            dxWrap < HIT_RADIUS && abs(e.y - p.y) < HIT_RADIUS
        }
    }
}
