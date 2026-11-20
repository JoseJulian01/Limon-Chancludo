package mx.udg.limonchancludo.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.udg.limonchancludo.game.model.Characters
import mx.udg.limonchancludo.game.model.PlayableCharacter
import mx.udg.limonchancludo.ui.components.PixelButton
import mx.udg.limonchancludo.ui.components.drawPixelSprite
import mx.udg.limonchancludo.ui.state.Screen
import mx.udg.limonchancludo.ui.theme.LimonChancludoTheme

@Composable
fun HomeScreen(
    selectedCharacter: PlayableCharacter,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "LIMON CHANCLU DO",
                color = Color(0xFFFFE93B),
                fontFamily = FontFamily.Monospace,
                fontSize = 26.sp,
                letterSpacing = 2.sp
            )
            Text(
                text = "come limones, esquiva las sandias",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 6.dp)
            )
            Spacer(Modifier.height(20.dp))
            CharacterPreview(selectedCharacter)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PixelButton(text = "JUGAR", onClick = { onNavigate(Screen.GAME) }, modifier = Modifier.fillMaxWidth(0.8f))
            PixelButton(text = "PERSONAJES", onClick = { onNavigate(Screen.CHARACTERS) }, modifier = Modifier.fillMaxWidth(0.8f))
            PixelButton(text = "RECORDS", onClick = { onNavigate(Screen.RECORDS) }, modifier = Modifier.fillMaxWidth(0.8f))
            PixelButton(text = "OPCIONES", onClick = { onNavigate(Screen.OPTIONS) }, modifier = Modifier.fillMaxWidth(0.8f))
            PixelButton(text = "CREDITOS", onClick = { onNavigate(Screen.CREDITS) }, modifier = Modifier.fillMaxWidth(0.8f))
        }
    }
}

@Composable
private fun CharacterPreview(character: PlayableCharacter) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .size(140.dp)
                .background(Color(0xFF14143A))
        ) {
            Canvas(Modifier.fillMaxSize()) {
                val cell = minOf(size.width, size.height)
                val dx = ((size.width - cell) / 2f).toInt()
                val dy = ((size.height - cell) / 2f).toInt()
                drawPixelSprite(
                    sprite = character.sprite,
                    dstOffset = IntOffset(dx, dy),
                    dstSize = IntSize(cell.toInt(), cell.toInt())
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = character.name,
            color = MaterialTheme.colorScheme.primary,
            fontFamily = FontFamily.Monospace,
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B2A, widthDp = 360, heightDp = 740)
@Composable
private fun HomeScreenPreview() {
    LimonChancludoTheme {
        HomeScreen(selectedCharacter = Characters.ALL.first(), onNavigate = {})
    }
}
