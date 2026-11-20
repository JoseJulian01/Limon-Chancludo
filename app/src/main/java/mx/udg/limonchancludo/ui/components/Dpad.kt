package mx.udg.limonchancludo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.udg.limonchancludo.game.model.Direction
import mx.udg.limonchancludo.ui.theme.LimonChancludoTheme

/**
 * D-pad virtual en cruz: 4 botones de flecha estilo arcade.
 */
@Composable
fun DPad(
    onDirection: (Direction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        ArrowButton("▲", Direction.UP, onDirection)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            ArrowButton("◀", Direction.LEFT, onDirection)
            Box(Modifier.size(56.dp))
            ArrowButton("▶", Direction.RIGHT, onDirection)
        }
        ArrowButton("▼", Direction.DOWN, onDirection)
    }
}

@Composable
private fun ArrowButton(
    glyph: String,
    direction: Direction,
    onDirection: (Direction) -> Unit
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .background(Color(0xFF2B44FF))
            .clickable { onDirection(direction) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = glyph,
            color = Color.White,
            fontFamily = FontFamily.Monospace,
            fontSize = 20.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B2A)
@Composable
private fun DPadPreview() {
    LimonChancludoTheme {
        DPad(onDirection = {})
    }
}
