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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import mx.udg.limonchancludo.game.assets.PixelSprite
import mx.udg.limonchancludo.game.assets.Sprites
import mx.udg.limonchancludo.ui.theme.LimonChancludoTheme

/**
 * Pantalla de prueba: todos los sprites escalados con vecino más cercano.
 * Sirve para verificar visualmente que el pixel art se ve nítido.
 */
@Composable
fun SpriteGallery(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "GALERIA DE SPRITES",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium
            )
            PixelButton(text = "ATRAS", onClick = onBack)
        }

        Sprites.all.forEach { sprite ->
            SpriteRow(sprite)
        }

        Spacer(Modifier.height(8.dp))
        Text(
            text = "Escalado nearest-neighbor: los pixeles deben verse nitidos.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun SpriteRow(sprite: PixelSprite) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(96.dp)
                .background(Color(0xFF14143A))
        ) {
            Canvas(Modifier.fillMaxSize()) {
                val cell = minOf(size.width, size.height)
                val dx = ((size.width - cell) / 2f).toInt()
                val dy = ((size.height - cell) / 2f).toInt()
                drawPixelSprite(
                    sprite = sprite,
                    dstOffset = IntOffset(dx, dy),
                    dstSize = IntSize(cell.toInt(), cell.toInt())
                )
            }
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(
                text = sprite.name,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "${sprite.width}x${sprite.height} px",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B2A, widthDp = 360, heightDp = 800)
@Composable
private fun SpriteGalleryPreview() {
    LimonChancludoTheme {
        SpriteGallery()
    }
}
