package mx.udg.limonchancludo.ui.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import mx.udg.limonchancludo.game.assets.PixelSprite
import mx.udg.limonchancludo.game.assets.SpriteFactory

// Paint reutilizable: sin asignaciones por frame y sin suavizado de píxeles.
private val pixelPaint = Paint().apply { filterQuality = FilterQuality.None }

/**
 * Dibuja un [PixelSprite] escalado con vecino más cercano (píxeles nítidos).
 */
fun DrawScope.drawPixelSprite(
    sprite: PixelSprite,
    dstOffset: IntOffset,
    dstSize: IntSize
) {
    drawContext.canvas.drawImageRect(
        image = SpriteFactory.bitmap(sprite),
        srcOffset = IntOffset.Zero,
        srcSize = IntSize(sprite.width, sprite.height),
        dstOffset = dstOffset,
        dstSize = dstSize,
        paint = pixelPaint
    )
}

/** Dibuja un sprite centrado en un cuadrado de [size] px en [topLeft]. */
fun DrawScope.drawPixelSpriteAt(
    sprite: PixelSprite,
    topLeft: Offset,
    size: Float
) {
    drawPixelSprite(
        sprite = sprite,
        dstOffset = IntOffset(topLeft.x.toInt(), topLeft.y.toInt()),
        dstSize = IntSize(size.toInt(), size.toInt())
    )
}
