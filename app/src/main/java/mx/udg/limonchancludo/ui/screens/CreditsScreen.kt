package mx.udg.limonchancludo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.udg.limonchancludo.ui.components.PixelButton
import mx.udg.limonchancludo.ui.theme.LimonChancludoTheme

@Composable
fun CreditsScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CREDITOS",
                color = MaterialTheme.colorScheme.primary,
                fontFamily = FontFamily.Monospace,
                fontSize = 22.sp
            )
            PixelButton(text = "ATRAS", onClick = onBack)
        }
        Spacer(Modifier.height(32.dp))
        Text(
            text = "LIMON CHANCLU DO",
            color = androidx.compose.ui.graphics.Color(0xFFFFE93B),
            fontFamily = FontFamily.Monospace,
            fontSize = 20.sp,
            letterSpacing = 2.sp
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Version 1.0\nJuego arcade estilo laberinto\nhecho con Jetpack Compose",
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = "Arte 100% generado por codigo:\nsprites, tablero y personajes\nson originales de este proyecto.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontFamily = FontFamily.Monospace,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = "Sin assets de terceros.\nSin compras ni anuncios.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B2A, widthDp = 360, heightDp = 740)
@Composable
private fun CreditsScreenPreview() {
    LimonChancludoTheme {
        CreditsScreen(onBack = {})
    }
}
