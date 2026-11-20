package mx.udg.limonchancludo.game.assets

/**
 * Sprite de píxeles puro (sin dependencias de Android ni Compose).
 * Cada fila es una cadena de [width] caracteres; '.' es transparente.
 */
class PixelSprite(
    val name: String,
    val pixels: List<String>,
    val palette: Map<Char, Int>
) {
    val width: Int
    val height: Int

    init {
        require(pixels.isNotEmpty()) { "Sprite '$name' sin filas" }
        width = pixels.first().length
        require(width > 0) { "Sprite '$name' con filas vacías" }
        height = pixels.size
        pixels.forEachIndexed { i, row ->
            require(row.length == width) {
                "Sprite '$name': fila $i tiene ${row.length} px, se esperaban $width"
            }
            row.forEach { ch ->
                require(ch == TRANSPARENT || ch in palette) {
                    "Sprite '$name': carácter '$ch' fuera de la paleta"
                }
            }
        }
    }

    fun charAt(x: Int, y: Int): Char = pixels[y][x]

    companion object {
        const val TRANSPARENT = '.'
    }
}
