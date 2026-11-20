package mx.udg.limonchancludo.game.model

import mx.udg.limonchancludo.game.assets.PixelSprite
import mx.udg.limonchancludo.game.assets.Sprites

/**
 * Personaje seleccionable. Cosmético: no altera velocidad ni hitbox.
 * El sprite se compone del mono base + accesorio y la paleta del personaje.
 */
data class PlayableCharacter(
    val id: String,
    val name: String,
    val unlockScore: Int,
    val furColor: Int,
    val faceColor: Int,
    val overlay: List<String>? = null
) {
    val sprite: PixelSprite by lazy {
        PixelSprite(
            name = "monkey_$id",
            pixels = Sprites.composite(Sprites.baseMonkey.pixels, overlay),
            palette = Sprites.palette +
                mapOf('F' to furColor, 'S' to faceColor)
        )
    }

    fun isUnlocked(bestScore: Int): Boolean = bestScore >= unlockScore
}

object Characters {

    private val EMPTY = List(16) { "................" }

    private fun rows(vararg defined: Pair<Int, String>): List<String> {
        val result = EMPTY.toMutableList()
        defined.forEach { (index, row) -> result[index] = row }
        return result
    }

    // Banda roja con colas a un lado
    private val NINJA_OVERLAY = rows(
        3 to "...RRRRRRRRRR...",
        4 to "..RRRRRRRRRRRR..",
        5 to "..........RRRR..",
        6 to "............RR.."
    )

    // Sombrero de tres picos con calavera + parche en el ojo izquierdo
    private val PIRATA_OVERLAY = rows(
        1 to "...KKKKKKKKKK...",
        2 to ".KKKKKKWWKKKKKK.",
        3 to "..KKKKKKKKKKKK..",
        6 to "....KKKKKKKKKK..",
        7 to ".....KK........."
    )

    // Casco espacial con visor
    private val ASTRONAUTA_OVERLAY = rows(
        1 to "....HHHHHHHH....",
        2 to ".HHHHHHHHHHHHHH.",
        3 to ".HHHHHHHHHHHHHH.",
        4 to ".HHHHHHHHHHHHHH.",
        5 to ".HHVVVVVVVVVVHH.",
        6 to ".HHVVVVVVVVVVHH.",
        7 to ".HHVVEEVVEEVVHH.",
        8 to ".HHVVVVVVVVVVHH.",
        9 to "..HHVVVVVVVVHH..",
        10 to "...HHHHHHHHHH..."
    )

    // Gorro de chef
    private val CHEF_OVERLAY = rows(
        0 to "....HHHHHHHH....",
        1 to "..HHHHHHHHHHHH..",
        2 to ".HHHHHHHHHHHHHH.",
        3 to ".HHHHHHHHHHHHHH.",
        4 to "..HHHHHHHHHHHH.."
    )

    // Antena + visor LED + rejilla
    private val ROBOT_OVERLAY = rows(
        0 to ".......RR.......",
        1 to ".......SS.......",
        7 to "....CCCCCCCC....",
        9 to ".....KSKSKS....."
    )

    val ALL: List<PlayableCharacter> = listOf(
        PlayableCharacter(
            id = "clasico",
            name = "Mono Clásico",
            unlockScore = 0,
            furColor = 0xFF9B5E3C.toInt(),
            faceColor = 0xFFE8C39E.toInt()
        ),
        PlayableCharacter(
            id = "ninja",
            name = "Mono Ninja",
            unlockScore = 5_000,
            furColor = 0xFF3A3A4A.toInt(),
            faceColor = 0xFFE8C39E.toInt(),
            overlay = NINJA_OVERLAY
        ),
        PlayableCharacter(
            id = "pirata",
            name = "Mono Pirata",
            unlockScore = 10_000,
            furColor = 0xFF8B5A2B.toInt(),
            faceColor = 0xFFE8C39E.toInt(),
            overlay = PIRATA_OVERLAY
        ),
        PlayableCharacter(
            id = "astronauta",
            name = "Mono Astronauta",
            unlockScore = 20_000,
            furColor = 0xFFE6EAF5.toInt(),
            faceColor = 0xFFE8C39E.toInt(),
            overlay = ASTRONAUTA_OVERLAY
        ),
        PlayableCharacter(
            id = "chef",
            name = "Mono Chef",
            unlockScore = 35_000,
            furColor = 0xFF9B5E3C.toInt(),
            faceColor = 0xFFE8C39E.toInt(),
            overlay = CHEF_OVERLAY
        ),
        PlayableCharacter(
            id = "robot",
            name = "Mono Robot",
            unlockScore = 50_000,
            furColor = 0xFF9AA4C0.toInt(),
            faceColor = 0xFFC7CEDF.toInt(),
            overlay = ROBOT_OVERLAY
        )
    )

    fun byId(id: String): PlayableCharacter = ALL.firstOrNull { it.id == id } ?: ALL.first()
}
