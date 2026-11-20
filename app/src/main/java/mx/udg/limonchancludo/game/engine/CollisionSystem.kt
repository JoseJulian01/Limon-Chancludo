package mx.udg.limonchancludo.game.engine

import mx.udg.limonchancludo.game.model.Direction
import mx.udg.limonchancludo.game.model.GameEvent
import mx.udg.limonchancludo.game.model.GameState
import mx.udg.limonchancludo.game.model.Tile

object CollisionSystem {
    fun eatUnderFeet(state: GameState): List<GameEvent> {
        val player = state.player
        val (cellCol, cellRow) = if (player.moving && player.progress >= 0.5f) {
            player.targetCol to player.targetRow
        } else {
            player.col to player.row
        }

        val points = state.eatLemon(cellCol, cellRow)
        if (points == 0) return emptyList()

        val tile = state.lemonTile(cellCol, cellRow)
        val events = mutableListOf<GameEvent>()
        if (tile == Tile.POWER) {
            events += GameEvent.PowerEaten(cellCol, cellRow, points)
        } else {
            events += GameEvent.LemonEaten(cellCol, cellRow, points)
        }

        if (state.status == mx.udg.limonchancludo.game.model.GameStatus.LEVEL_CLEARED) {
            events += GameEvent.LevelCleared
        }
        return events
    }
}
