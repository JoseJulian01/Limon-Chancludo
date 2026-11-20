package mx.udg.limonchancludo.game.model

import androidx.compose.ui.graphics.Color
import mx.udg.limonchancludo.game.assets.PixelSprite
import mx.udg.limonchancludo.game.assets.SandalSprites

enum class SandalType {
    BLINKY,   // Rojo: persegue directamente la posición del jugador
    PINKY,    // Rosa: intenta emboscar 4 casillas adelante del jugador
    INKY,     // Azul: cálculo basado en jugador y Blinky
    CLYDE     // Naranja: Blinky si lejos, Scatter si cerca (< 8 casillas)
}

/**
 * Sandalia (enemigo) como Actor en movimiento por el mapa.
 * Cada tipo tiene su propio color, comportamiento y esquina de hogar (Scatter).
 */
class Sandal(
    startCol: Int,
    startRow: Int,
    val type: SandalType,
    val color: Color
) : Actor(startCol, startRow, Direction.UP) {

    /** Estado actual de la sandalia. */
    var state: SandalState = SandalState.CHASE
        internal set

    /** Esquina de dispersión (Scatter target) asignada a cada tipo. */
    fun scatterTarget(): Pair<Int, Int> = when (type) {
        SandalType.BLINKY -> Pair(20, 1)      // Esquina superior derecha
        SandalType.PINKY -> Pair(0, 1)        // Esquina superior izquierda
        SandalType.INKY -> Pair(20, 22)       // Esquina inferior derecha
        SandalType.CLYDE -> Pair(0, 22)       // Esquina inferior izquierda
    }

    /** Casilla de la casa (centro del laberinto) a donde regresan al ser comidos (EATEN). */
    fun houseTarget(): Pair<Int, Int> = Pair(10, 11)

    /** Casilla de hogar (Scatter) por compatibilidad. */
    fun homeTarget(): Pair<Int, Int> = scatterTarget()

    /** Devuelve el sprite correspondiente al estado actual. */
    fun currentSprite(): PixelSprite = SandalSprites.spriteFor(type, state)
}

object Sandals {
    val ALL: List<Sandal> = listOf(
        Sandal(10, 11, SandalType.BLINKY,   Color(0xFFFF4B4B)), // Rojo
        Sandal(9, 11,  SandalType.PINKY,    Color(0xFFFFB7C5)), // Rosa
        Sandal(11, 11, SandalType.INKY,     Color(0xFF4FB3FF)), // Azul
        Sandal(12, 11, SandalType.CLYDE,    Color(0xFFFF9B3B))  // Naranja
    )

    fun byType(type: SandalType): Sandal = ALL.first { it.type == type }
}
