package mx.udg.limonchancludo.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.udg.limonchancludo.ui.theme.LimonChancludoTheme

/**
 * Botón arcade: cara con borde de 4px, sombra desplazada y efecto de
 * "presionado" que hunde la cara sobre la sombra.
 */
@Composable
fun PixelButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    faceColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    borderColor: Color = Color.White,
    shadowColor: Color = Color.Black
) {
    val border = 4.dp
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val pressOffset by animateDpAsState(
        targetValue = if (pressed && enabled) border else 0.dp,
        label = "press"
    )

    Box(
        modifier = Modifier.padding(end = border, bottom = border)
    ) {
        // Sombra (siempre debajo, desplazada 4px)
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = border, y = border)
                .background(if (enabled) shadowColor else shadowColor.copy(alpha = 0.4f))
        )
        // Cara del botón (aplica el modifier de tamaño/posición a la cara principal)
        Box(
            modifier = modifier
                .offset(x = pressOffset, y = pressOffset)
                .background(if (enabled) faceColor else faceColor.copy(alpha = 0.4f))
                .border(width = border, color = borderColor, shape = RectangleShape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    onClick = onClick
                )
                .padding(horizontal = 24.dp, vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = if (enabled) contentColor else contentColor.copy(alpha = 0.5f),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B2A)
@Composable
private fun PixelButtonPreview() {
    LimonChancludoTheme {
        Box(Modifier.padding(24.dp)) {
            PixelButton(text = "JUGAR", onClick = {})
        }
    }
}
