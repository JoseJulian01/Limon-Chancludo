package mx.udg.limonchancludo.game.model

/**
 * Celdas del laberinto.
 * '#' muro · '.' limón · 'o' limón de poder · '_' vacío
 * 'T' túnel · 'P' inicio del mono · '+' casa · '-' puerta
 */
enum class Tile(val char: Char, val walkableForPlayer: Boolean) {
    WALL('#', false),
    LEMON('.', true),
    POWER('o', true),
    FLOOR('_', true),
    TUNNEL('T', true),
    SPAWN('P', true),
    HOUSE('+', false),
    DOOR('-', false);

    val isLemon: Boolean get() = this == LEMON || this == POWER

    companion object {
        fun fromChar(c: Char): Tile = entries.firstOrNull { it.char == c }
            ?: error("Carácter desconocido en el mapa: '$c'")
    }
}
