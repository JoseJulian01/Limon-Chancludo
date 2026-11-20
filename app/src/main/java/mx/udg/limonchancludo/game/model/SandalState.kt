package mx.udg.limonchancludo.game.model

/**
 * Estado de una sandalia (enemigo) durante la partida.
 */
enum class SandalState {
    /** Persecución activa hacia su objetivo personal. */
    CHASE,
    /** Dispersión hacia su esquina asignada (casa). */
    SCATTER,
    /** Asustada tras comer un Limón de poder; se mueve lento y puede ser comida. */
    FRIGHTENED,
    /** Ha sido comida; regresa a la casa para reaparecer. */
    EATEN
}
