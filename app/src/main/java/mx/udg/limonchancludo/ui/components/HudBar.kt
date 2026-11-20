package mx.udg.limonchancludo.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.udg.limonchancludo.game.assets.Sprites
import mx.udg.limonchancludo.game.model.GameSnapshot

/**
 * Barra superior: puntaje, récord, nivel, vidas, limones y pausa.
 */
@Composable
fun HudBar(
    snapshot: GameSnapshot,
    bestScore: Int,
    onPause: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF14143A))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Label("PTS ${snapshot.score}", Color(0xFFFFE93B), 16.sp)
            Label("MAX $bestScore", Color(0xFF9BA0C0), 11.sp)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Vidas (corazones)
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(snapshot.lives) {
                    Box(Modifier.size(16.dp)) {
                        Canvas(Modifier.fillMaxSize()) {
                            drawPixelSprite(
                                sprite = Sprites.heart,
                                dstOffset = IntOffset.Zero,
                                dstSize = IntSize(size.width.toInt(), size.height.toInt())
                            )
                        }
                    }
                    Spacer(Modifier.width(3.dp))
                }
            }
            Spacer(Modifier.width(12.dp))
            Label("NIV ${snapshot.level}", Color(0xFF6C7BFF), 14.sp)
            Spacer(Modifier.width(10.dp))
            Label("LIM ${snapshot.lemonsRemaining}", Color(0xFFFFE93B), 14.sp)
            Spacer(Modifier.width(10.dp))
            PixelButton(text = "II", onClick = onPause)
        }
    }
}

@Composable
private fun Label(text: String, color: Color, size: androidx.compose.ui.unit.TextUnit) {
    Text(
        text = text,
        color = color,
        fontFamily = FontFamily.Monospace,
        fontSize = size
    )
}
