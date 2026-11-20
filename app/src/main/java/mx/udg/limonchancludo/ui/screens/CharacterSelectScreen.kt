package mx.udg.limonchancludo.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.udg.limonchancludo.game.model.Characters
import mx.udg.limonchancludo.game.model.PlayableCharacter
import mx.udg.limonchancludo.ui.components.PixelButton
import mx.udg.limonchancludo.ui.components.drawPixelSprite
import mx.udg.limonchancludo.ui.theme.LimonChancludoTheme

@Composable
fun CharacterSelectScreen(
    selectedCharacterId: String,
    bestScore: Int,
    onSelect: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "PERSONAJES",
                color = MaterialTheme.colorScheme.primary,
                fontFamily = FontFamily.Monospace,
                fontSize = 22.sp
            )
            PixelButton(text = "ATRAS", onClick = onBack)
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Récord: $bestScore pts",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontFamily = FontFamily.Monospace,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(Characters.ALL, key = { it.id }) { character ->
                CharacterCard(
                    character = character,
                    selected = character.id == selectedCharacterId,
                    bestScore = bestScore,
                    onSelect = onSelect
                )
            }
        }
    }
}

@Composable
private fun CharacterCard(
    character: PlayableCharacter,
    selected: Boolean,
    bestScore: Int,
    onSelect: (String) -> Unit
) {
    val unlocked = character.isUnlocked(bestScore)
    val borderColor =
        if (selected) Color(0xFFFFE93B) else Color(0xFF2B44FF)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF14143A))
            .border(width = if (selected) 3.dp else 2.dp, color = borderColor)
            .clickable(enabled = unlocked) { onSelect(character.id) }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.size(96.dp)) {
            Canvas(Modifier.fillMaxSize()) {
                val cell = minOf(size.width, size.height)
                val dx = ((size.width - cell) / 2f).toInt()
                val dy = ((size.height - cell) / 2f).toInt()
                drawPixelSprite(
                    sprite = character.sprite,
                    dstOffset = IntOffset(dx, dy),
                    dstSize = IntSize(cell.toInt(), cell.toInt())
                )
                if (!unlocked) {
                    val lockSize = (cell / 3f).toInt()
                    drawPixelSprite(
                        sprite = mx.udg.limonchancludo.game.assets.Sprites.lock,
                        dstOffset = IntOffset(
                            dx + cell.toInt() * 2 / 3,
                            dy + cell.toInt() * 2 / 3
                        ),
                        dstSize = IntSize(lockSize, lockSize)
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = character.name,
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = FontFamily.Monospace,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = when {
                selected -> "ELEGIDO"
                unlocked -> "ELEGIR"
                else -> "LOCK ${character.unlockScore} pts"
            },
            color = when {
                selected -> Color(0xFFFFE93B)
                unlocked -> Color(0xFF6C7BFF)
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            },
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B2A, widthDp = 360, heightDp = 740)
@Composable
private fun CharacterSelectPreview() {
    LimonChancludoTheme {
        CharacterSelectScreen(
            selectedCharacterId = "clasico",
            bestScore = 0,
            onSelect = {},
            onBack = {}
        )
    }
}
