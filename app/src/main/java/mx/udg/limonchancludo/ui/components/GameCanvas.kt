package mx.udg.limonchancludo.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import mx.udg.limonchancludo.game.assets.PixelSprite
import mx.udg.limonchancludo.game.assets.Sprites
import mx.udg.limonchancludo.game.model.GameSnapshot
import mx.udg.limonchancludo.game.model.Tile
import mx.udg.limonchancludo.ui.FloatingText

private val BoardBackground = Color(0xFF05051A)

/**
 * Dibuja el tablero (muros, limones) y el mono interpolado en un solo Canvas.
 */
@Composable
fun GameCanvas(
    snapshot: GameSnapshot,
    floatingTexts: List<FloatingText> = emptyList(),
    playerSprite: PixelSprite = Sprites.baseMonkey,
    modifier: Modifier = Modifier
) {
    val maze = snapshot.maze
    val textMeasurer = rememberTextMeasurer()
    val t = snapshot.timeSeconds

    Canvas(
        modifier = modifier.background(BoardBackground)
    ) {
        val tileSize = minOf(
            size.width / maze.width,
            size.height / maze.height
        )
        val offsetX = (size.width - tileSize * maze.width) / 2f
        val offsetY = (size.height - tileSize * maze.height) / 2f
        val cellSize = IntSize(tileSize.toInt(), tileSize.toInt())

        fun cellOffset(col: Int, row: Int): IntOffset = IntOffset(
            (offsetX + col * tileSize).toInt(),
            (offsetY + row * tileSize).toInt()
        )

        // Celdas
        val powerVisible = ((t * 4).toInt() % 2) == 0
        for (row in 0 until maze.height) {
            for (col in 0 until maze.width) {
                val tile = maze.tileAt(col, row)
                val uneaten = !snapshot.eaten[row * maze.width + col]
                when (tile) {
                    Tile.WALL -> drawPixelSprite(
                        sprite = Sprites.wallBrick,
                        dstOffset = cellOffset(col, row),
                        dstSize = cellSize
                    )

                    Tile.LEMON -> if (uneaten) {
                        val s = (tileSize * 0.62f).toInt()
                        val inset = ((tileSize - s) / 2f).toInt()
                        val base = cellOffset(col, row)
                        drawPixelSprite(Sprites.lemonDot, IntOffset(base.x + inset, base.y + inset), IntSize(s, s))
                    }

                    Tile.POWER -> if (uneaten && powerVisible) {
                        val s = (tileSize * 0.82f).toInt()
                        val inset = ((tileSize - s) / 2f).toInt()
                        val base = cellOffset(col, row)
                        drawPixelSprite(Sprites.powerLemon, IntOffset(base.x + inset, base.y + inset), IntSize(s, s))
                    }

                    else -> Unit
                }
            }
        }

        // Enemigos
        for (e in snapshot.enemies) {
            val px = offsetX + e.x * tileSize
            val py = offsetY + e.y * tileSize
            drawCircle(Color(0xFFFF4B4B), radius = tileSize * 0.35f, center = Offset(px, py))
        }

        // Mono interpolado
        val playerVisible = snapshot.invulnerable <= 0f || ((t * 10).toInt() % 2) == 0
        if (playerVisible) {
            val spriteSize = tileSize * 0.94f
            val px = offsetX + snapshot.playerX * tileSize + (tileSize - spriteSize) / 2f
            val py = offsetY + snapshot.playerY * tileSize + (tileSize - spriteSize) / 2f
            drawPixelSprite(
                sprite = playerSprite,
                dstOffset = IntOffset(px.toInt(), py.toInt()),
                dstSize = IntSize(spriteSize.toInt(), spriteSize.toInt())
            )
        }

        // Floating texts
        for (ft in floatingTexts) {
            val age = t - ft.bornAt
            val px = offsetX + (ft.col + 0.5f) * tileSize
            val py = offsetY + (ft.row + 0.5f) * tileSize - (age * 40f)
            drawText(
                textMeasurer = textMeasurer,
                text = ft.text,
                style = TextStyle(color = Color(0xFFFFE93B), fontSize = 13.sp, fontFamily = FontFamily.Monospace),
                topLeft = Offset(px, py)
            )
        }
    }
}
