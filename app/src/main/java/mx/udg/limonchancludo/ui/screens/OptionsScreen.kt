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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.udg.limonchancludo.data.ControlMode
import mx.udg.limonchancludo.data.GamePreferences
import mx.udg.limonchancludo.ui.components.PixelButton
import mx.udg.limonchancludo.ui.theme.LimonChancludoTheme

@Composable
fun OptionsScreen(
    prefs: GamePreferences,
    onSoundChange: (Boolean) -> Unit,
    onMusicChange: (Boolean) -> Unit,
    onHapticsChange: (Boolean) -> Unit,
    onControlModeChange: (ControlMode) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "OPCIONES",
                color = MaterialTheme.colorScheme.primary,
                fontFamily = FontFamily.Monospace,
                fontSize = 22.sp
            )
            PixelButton(text = "ATRAS", onClick = onBack)
        }

        OptionSwitch("SONIDO", prefs.soundEnabled, onSoundChange)
        OptionSwitch("MUSICA", prefs.musicEnabled, onMusicChange)
        OptionSwitch("VIBRACION", prefs.hapticsEnabled, onHapticsChange)

        Spacer(Modifier.height(4.dp))
        Text(
            text = "CONTROLES",
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = FontFamily.Monospace,
            fontSize = 15.sp
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ControlModeButton("SWIPE", ControlMode.SWIPE, prefs.controlMode, onControlModeChange)
            ControlModeButton("DPAD", ControlMode.DPAD, prefs.controlMode, onControlModeChange)
            ControlModeButton("AMBOS", ControlMode.BOTH, prefs.controlMode, onControlModeChange)
        }

        Spacer(Modifier.height(8.dp))
        Text(
            text = "Todo se guarda en este dispositivo.\nSin cuentas, sin internet, sin anuncios.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp,
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun OptionSwitch(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = FontFamily.Monospace,
            fontSize = 15.sp
        )
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = Color(0xFFFFE93B),
                checkedThumbColor = Color.Black,
                uncheckedTrackColor = Color(0xFF2B44FF),
                uncheckedThumbColor = Color.White
            )
        )
    }
}

@Composable
private fun ControlModeButton(
    label: String,
    mode: ControlMode,
    current: ControlMode,
    onSelect: (ControlMode) -> Unit
) {
    PixelButton(
        text = label,
        onClick = { onSelect(mode) },
        faceColor = if (current == mode) Color(0xFFFFE93B) else Color(0xFF2B44FF),
        contentColor = if (current == mode) Color.Black else Color.White,
        modifier = Modifier.width(96.dp)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B2A, widthDp = 360, heightDp = 740)
@Composable
private fun OptionsScreenPreview() {
    LimonChancludoTheme {
        OptionsScreen(
            prefs = GamePreferences(),
            onSoundChange = {},
            onMusicChange = {},
            onHapticsChange = {},
            onControlModeChange = {},
            onBack = {}
        )
    }
}
