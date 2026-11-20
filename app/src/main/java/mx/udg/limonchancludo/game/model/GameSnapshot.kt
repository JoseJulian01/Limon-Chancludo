package mx.udg.limonchancludo.game.model

data class EnemySnapshot(val x: Float, val y: Float, val dir: Direction)

/**
 * Instantánea inmutable para la UI (se crea en cada frame).
 */
data class GameSnapshot(
    val maze: Maze,
    val level: Int,
    val playerCol: Int,
    val playerRow: Int,
    val playerTargetCol: Int,
    val playerTargetRow: Int,
    val playerProgress: Float,
    val playerWraps: Boolean,
    val playerDir: Direction,
    val score: Int,
    val lives: Int,
    val lemonsRemaining: Int,
    val status: GameStatus,
    val eaten: BooleanArray,
    val timeSeconds: Float,
    val invulnerable: Float,
    val enemies: List<EnemySnapshot>
) {
    /** Coordenada continua del mono en celdas (con salto del túnel). */
    val playerX: Float
        get() = if (playerWraps) {
            playerTargetCol.toFloat()
        } else {
            playerCol + (playerTargetCol - playerCol) * playerProgress
        }

    val playerY: Float
        get() = playerRow + (playerTargetRow - playerRow) * playerProgress
}
