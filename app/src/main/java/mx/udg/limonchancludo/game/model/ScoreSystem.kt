package mx.udg.limonchancludo.game.model

import mx.udg.limonchancludo.game.model.Tile

object ScoreSystem {
    const val LEMON_POINTS = 10
    const val POWER_POINTS = 50
    const val EXTRA_LIFE_SCORE = 10_000
    const val LEVEL_BONUS_BASE = 500

    fun pointsFor(tile: Tile): Int = when (tile) {
        Tile.POWER -> POWER_POINTS
        Tile.LEMON -> LEMON_POINTS
        else -> 0
    }

    fun levelBonus(level: Int): Int = LEVEL_BONUS_BASE * level
}
