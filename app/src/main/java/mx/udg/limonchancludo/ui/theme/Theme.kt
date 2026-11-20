package mx.udg.limonchancludo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ArcadeColorScheme = darkColorScheme(
    primary = LemonYellow,
    onPrimary = PixelBlack,
    secondary = NeonBlueBright,
    onSecondary = PixelBlack,
    tertiary = SandalPink,
    onTertiary = PixelBlack,
    background = NightBackground,
    onBackground = TextWhite,
    surface = Color(0xFF14143A),
    onSurface = TextWhite,
    surfaceVariant = Color(0xFF1D1D4A),
    onSurfaceVariant = TextGrey,
    error = SandalRed
)

@Composable
fun LimonChancludoTheme(
    content: @Composable () -> Unit
) {
    // Estética arcade: siempre modo oscuro
    MaterialTheme(
        colorScheme = ArcadeColorScheme,
        typography = ArcadeTypography,
        content = content
    )
}
