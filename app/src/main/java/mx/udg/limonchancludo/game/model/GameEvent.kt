package mx.udg.limonchancludo.game.model

enum class GameStatus {
    RUNNING,
    LEVEL_CLEARED,
    GAME_OVER
}

sealed interface GameEvent {
    data class LemonEaten(val col: Int, val row: Int, val points: Int) : GameEvent
    data class PowerEaten(val col: Int, val row: Int, val points: Int) : GameEvent
    data object LevelCleared : GameEvent
    data object PlayerDied : GameEvent
    data class SandalEaten(val points: Int) : GameEvent
}
