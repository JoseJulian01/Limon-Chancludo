package mx.udg.limonchancludo.game.assets

import androidx.compose.ui.graphics.Color

object SandalSprites {

    private val W = 16
    private val H = 16

    private fun row16(s: String): String {
        return if (s.length >= W) s.substring(0, W) else s + ".".repeat(W - s.length)
    }

    /** Forma base de la sandalia (16x16) vista desde arriba. */
    private val SANDAL_PIXELS = listOf(
        row16("................"),
        row16("....SSSS...."),
        row16("...SSSSSS..."),
        row16("..SSSSSSSS.."),
        row16(".SSS.T.SSS.."),
        row16(".SS.TTT.SS.."),
        row16(".S.TTTT.SS.."),
        row16("..TTTTT.S..."),
        row16("..SSSSSS.."),
        row16("..SSS.SS.."),
        row16("..SS..SS.."),
        row16("..S..S..S.."),
        row16("................"),
        row16("................"),
        row16("................"),
        row16("................")
    )

    /** Sandalia asustada (cuerpo oscuro con ojos grandes). */
    private val FRIGHTENED_PIXELS = listOf(
        row16("................"),
        row16("....EEEE...."),
        row16("...EEEEEE..."),
        row16("..EEEEEEEE.."),
        row16(".EEE.FF.EEE."),
        row16(".EE.FFFF.EE."),
        row16(".E.FFFF.F.E."),
        row16("..FFFFF....."),
        row16("..FFFFF....."),
        row16("..EEEE......"),
        row16("..EE.EE....."),
        row16("..E..E......"),
        row16("................"),
        row16("................"),
        row16("................"),
        row16("................")
    )

    /** Sandalia comida (solo los ojos, regresa a casa). */
    private val EATEN_PIXELS = listOf(
        row16("................"),
        row16(".............."),
        row16("....EE........"),
        row16("...EEEE......."),
        row16("..EE.EE......."),
        row16("..E..E........"),
        row16("................"),
        row16("................"),
        row16("................"),
        row16("................"),
        row16("................"),
        row16("................"),
        row16("................"),
        row16("................"),
        row16("................"),
        row16("................")
    )

    /** Paleta base para el cuerpo de la sandalia (sole + strap). */
    private fun palette(soleLight: Int, soleDark: Int, strapMain: Int, strapDark: Int): Map<Char, Int> =
        mapOf(
            '.' to 0xFF000000.toInt(),
            'S' to soleLight,
            's' to soleDark,
            'T' to strapMain,
            't' to strapDark,
            'E' to 0xFFFFFFFF.toInt(),
            'e' to 0xFF000000.toInt()
        )

    /** Paleta para estado asustado (cuerpo azul oscuro, ojos blancos). */
    private val frightenedPalette: Map<Char, Int> = mapOf(
        '.' to 0xFF000000.toInt(),
        'E' to 0xFFFFFFFF.toInt(),
        'e' to 0xFF000000.toInt(),
        'F' to 0xFF2B2B5E.toInt(),
        'f' to 0xFF1A1A4E.toInt()
    )

    /** Paleta para estado comido (solo ojos). */
    private val eatenPalette: Map<Char, Int> = mapOf(
        '.' to 0xFF000000.toInt(),
        'E' to 0xFFFFFFFF.toInt(),
        'e' to 0xFF000000.toInt()
    )

    /** Blinky - Sandalia roja. */
    val blinky = PixelSprite(
        name = "sandal_blinky",
        pixels = SANDAL_PIXELS,
        palette = palette(
            soleLight = 0xFFFF8A8A.toInt(),
            soleDark = 0xFFFF4B4B.toInt(),
            strapMain = 0xFFFF6B6B.toInt(),
            strapDark = 0xFFE43B3B.toInt()
        )
    )

    /** Pinky - Sandalia rosa. */
    val pinky = PixelSprite(
        name = "sandal_pinky",
        pixels = SANDAL_PIXELS,
        palette = palette(
            soleLight = 0xFFFFC9E0.toInt(),
            soleDark = 0xFFFFB7C5.toInt(),
            strapMain = 0xFFFFC8E8.toInt(),
            strapDark = 0xFFE8A8C0.toInt()
        )
    )

    /** Inky - Sandalia azul. */
    val inky = PixelSprite(
        name = "sandal_inky",
        pixels = SANDAL_PIXELS,
        palette = palette(
            soleLight = 0xFF8CCCFF.toInt(),
            soleDark = 0xFF4FB3FF.toInt(),
            strapMain = 0xFF6CC4FF.toInt(),
            strapDark = 0xFF3B9BE8.toInt()
        )
    )

    /** Clyde - Sandalia naranja. */
    val clyde = PixelSprite(
        name = "sandal_clyde",
        pixels = SANDAL_PIXELS,
        palette = palette(
            soleLight = 0xFFFFCC8A.toInt(),
            soleDark = 0xFFFF9B3B.toInt(),
            strapMain = 0xFFFFB56B.toInt(),
            strapDark = 0xFFF07B1B.toInt()
        )
    )

    /** Sandalia asustada (compartida por todas las sandalias). */
    val frightened = PixelSprite(
        name = "sandal_frightened",
        pixels = FRIGHTENED_PIXELS,
        palette = frightenedPalette
    )

    /** Sandalia comida (solo ojos). */
    val eaten = PixelSprite(
        name = "sandal_eaten",
        pixels = EATEN_PIXELS,
        palette = eatenPalette
    )

    /** Devuelve el sprite correspondiente según el tipo y estado. */
    fun spriteFor(sandalType: mx.udg.limonchancludo.game.model.SandalType,
                  state: mx.udg.limonchancludo.game.model.SandalState): PixelSprite {
        return when (state) {
            mx.udg.limonchancludo.game.model.SandalState.EATEN -> eaten
            mx.udg.limonchancludo.game.model.SandalState.FRIGHTENED -> frightened
            mx.udg.limonchancludo.game.model.SandalState.CHASE, mx.udg.limonchancludo.game.model.SandalState.SCATTER -> when (sandalType) {
                mx.udg.limonchancludo.game.model.SandalType.BLINKY -> blinky
                mx.udg.limonchancludo.game.model.SandalType.PINKY -> pinky
                mx.udg.limonchancludo.game.model.SandalType.INKY -> inky
                mx.udg.limonchancludo.game.model.SandalType.CLYDE -> clyde
            }
        }
    }
}
