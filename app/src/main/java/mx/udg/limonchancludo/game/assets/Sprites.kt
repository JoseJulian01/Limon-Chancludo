package mx.udg.limonchancludo.game.assets

/**
 * Sprites base de Limón Chancludo, dibujados a mano como matrices de píxeles.
 * Paleta en ARGB (Int) para mantener el motor puro sin dependencias.
 */
object Sprites {

    // Paleta global (ARGB)
    val palette: Map<Char, Int> = mapOf(
        'Y' to 0xFFFFE93B.toInt(), // limón brillante
        'y' to 0xFFC9B400.toInt(), // limón oscuro
        'o' to 0xFF8F7E00.toInt(), // contorno limón
        'W' to 0xFFFFFFFF.toInt(), // brillo blanco
        'w' to 0xFFFF8FA8.toInt(), // brillo rosa (corazón)
        'G' to 0xFF3DDC84.toInt(), // verde hoja
        'g' to 0xFF1E9E5A.toInt(), // verde hoja oscuro
        'R' to 0xFFFF4B4B.toInt(), // rojo
        'b' to 0xFF6C7BFF.toInt(), // azul neón (borde muro)
        'B' to 0xFF2B44FF.toInt(), // azul muro
        'S' to 0xFFD8DCF0.toInt(), // plata
        's' to 0xFF8A90B0.toInt(), // plata oscura
        'K' to 0xFF000000.toInt(), // negro
        'E' to 0xFF141414.toInt(), // ojos
        'M' to 0xFF4A2A16.toInt(), // boca
        'F' to 0xFF9B5E3C.toInt(), // pelaje base (lo sustituye cada personaje)
        'H' to 0xFFF4F6FF.toInt(), // blanco casco/gorro
        'V' to 0xFF1B2A5E.toInt(), // visor oscuro
        'C' to 0xFF4DE1FF.toInt()  // LED cian
    )

    /** Limón normal (dot) centrado en la celda. */
    val lemonDot = PixelSprite(
        name = "lemon_dot",
        pixels = listOf(
            "................",
            "................",
            "................",
            "................",
            ".......GG.......",
            ".......gG.......",
            ".....oYYYYo.....",
            "...oYYYYYYYYo...",
            "...oYWWYYYYYo...",
            "...oYYYYYYYYo...",
            "...oYYYYYYYYo...",
            "....oYYYYYYo....",
            ".....oYYYYo.....",
            "................",
            "................",
            "................"
        ),
        palette = palette
    )

    /** Limón de poder (Limón Chancludo) con chispas. */
    val powerLemon = PixelSprite(
        name = "power_lemon",
        pixels = listOf(
            "................",
            "..W..........W..",
            "................",
            "........GG......",
            "........gG......",
            "..oYYYYYYYYYYo..",
            ".oYYWWYYYYYYYYo.",
            ".oYYYYYYYYYYYYo.",
            ".oYYYYYYYYYYYYo.",
            ".oYYYYYYYYYYYYo.",
            "..oYYYYYYYYYYo..",
            "...oYYYYYYYYo...",
            "....oYYYYYYo....",
            "................",
            "..W..........W..",
            "................"
        ),
        palette = palette
    )

    /** Ladrillo neón (8x8, se repite por celda de muro). */
    val wallBrick = PixelSprite(
        name = "wall_brick",
        pixels = listOf(
            "bbbbbbbb",
            "bBBBBBBb",
            "bBBBBBBb",
            "bbbbbbbb",
            "bBBBBBBb",
            "bBBBBBBb",
            "bBBBBBBb",
            "bbbbbbbb"
        ),
        palette = palette
    )

    /** Puerta de la casa de sandías. */
    val gateDoor = PixelSprite(
        name = "gate_door",
        pixels = listOf(
            "................",
            "................",
            "................",
            "................",
            "................",
            "................",
            "bbbbbbbbbbbbbbbb",
            "bBBBBBBBBBBBBBBb",
            "bBBBBBBBBBBBBBBb",
            "bbbbbbbbbbbbbbbb",
            "................",
            "................",
            "................",
            "................",
            "................",
            "................"
        ),
        palette = palette
    )

    /** Corazón = vida. */
    val heart = PixelSprite(
        name = "heart",
        pixels = listOf(
            "................",
            "................",
            "................",
            "...RRR..RRR.....",
            "..RRRRR.RRRRR...",
            ".RRwwRRRRRRRRR..",
            ".RRRRRRRRRRRRR..",
            ".RRRRRRRRRRRRR..",
            "..RRRRRRRRRRR...",
            "...RRRRRRRRR....",
            "....RRRRRRR.....",
            ".....RRRRR......",
            "......RRR.......",
            ".......R........",
            "................",
            "................"
        ),
        palette = palette
    )

    /** Candado de personaje bloqueado. */
    val lock = PixelSprite(
        name = "lock",
        pixels = listOf(
            "................",
            "................",
            "................",
            ".....SSSSS......",
            "....SS...SS.....",
            "...SS.....SS....",
            "...SS.....SS....",
            "...SS.....SS....",
            "..SSSSSSSSSSSS..",
            "..sSSSSSSSSSSs..",
            "..sSSSSSSSSSSs..",
            "..sSSSSKKSSSSs..",
            "..sSSSSKKSSSSs..",
            "..ssssssssssss..",
            "................",
            "................"
        ),
        palette = palette
    )

    val all: List<PixelSprite> = listOf(lemonDot, powerLemon, wallBrick, gateDoor, heart, lock)

    /** Mono base (16x16), frente. Se combina con los accesorios de cada personaje. */
    val baseMonkey = PixelSprite(
        name = "monkey_base",
        pixels = listOf(
            "................",
            "................",
            ".....FFFFFF.....",
            "....FFFFFFFF....",
            "..FFFFFFFFFFFF..",
            "..FSFFFFFFFFSF..",
            "..FSFFFFFFFFSF..",
            "....FEEFFEEF....",
            "....FSSSSSSF....",
            "....FSSMMSSF....",
            ".....FFFFFF.....",
            "...FFFFFFFFFF...",
            "...FFFSSSSFFF...",
            "....FFFFFFFF....",
            "...FFFF..FFFF...",
            "................"
        ),
        palette = palette
    )

    /**
     * Combina el mono base con un accesorio (sombrero, casco, banda...).
     * Los píxeles '.' del accesorio dejan ver el mono base.
     */
    fun composite(base: List<String>, overlay: List<String?>?): List<String> {
        if (overlay == null) return base
        require(overlay.size == base.size) {
            "overlay con ${overlay.size} filas, base con ${base.size}"
        }
        return base.mapIndexed { y, row ->
            val o = overlay[y] ?: return@mapIndexed row
            require(o.length == row.length) { "overlay fila $y: ${o.length} != ${row.length}" }
            buildString(row.length) {
                row.forEachIndexed { x, ch ->
                    val oc = o[x]
                    append(if (oc != PixelSprite.TRANSPARENT) oc else ch)
                }
            }
        }
    }
}
