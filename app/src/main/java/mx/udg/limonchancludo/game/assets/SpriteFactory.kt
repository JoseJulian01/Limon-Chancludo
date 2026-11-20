package mx.udg.limonchancludo.game.assets

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

/**
 * Rasteriza [PixelSprite] a [ImageBitmap] una sola vez y lo cachea.
 * El escalado se hace con filtro None (nearest-neighbor) al dibujar.
 */
object SpriteFactory {

    private val cache = HashMap<String, ImageBitmap>()

    fun bitmap(sprite: PixelSprite): ImageBitmap =
        cache.getOrPut(sprite.name) { rasterize(sprite) }

    fun invalidate() = cache.clear()

    private fun rasterize(sprite: PixelSprite): ImageBitmap {
        val w = sprite.width
        val h = sprite.height
        val argb = IntArray(w * h)
        for (y in 0 until h) {
            val row = sprite.pixels[y]
            for (x in 0 until w) {
                val ch = row[x]
                argb[y * w + x] =
                    if (ch == PixelSprite.TRANSPARENT) 0 else sprite.palette.getValue(ch)
            }
        }
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(argb, 0, w, 0, 0, w, h)
        return bitmap.asImageBitmap()
    }
}
