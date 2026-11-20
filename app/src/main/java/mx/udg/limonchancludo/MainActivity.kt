package mx.udg.limonchancludo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import mx.udg.limonchancludo.data.GamePreferences
import mx.udg.limonchancludo.data.PreferencesRepository
import mx.udg.limonchancludo.game.model.Characters
import mx.udg.limonchancludo.ui.screens.CharacterSelectScreen
import mx.udg.limonchancludo.ui.screens.CreditsScreen
import mx.udg.limonchancludo.ui.screens.GameScreen
import mx.udg.limonchancludo.ui.screens.HomeScreen
import mx.udg.limonchancludo.ui.screens.OptionsScreen
import mx.udg.limonchancludo.ui.screens.RecordsScreen
import mx.udg.limonchancludo.ui.screens.SplashScreen
import mx.udg.limonchancludo.ui.state.Screen
import mx.udg.limonchancludo.ui.theme.LimonChancludoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LimonChancludoTheme {
                AppRoot()
            }
        }
    }
}

@Composable
private fun AppRoot() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val repository = remember { PreferencesRepository(context.applicationContext) }
    val prefs by repository.flow.collectAsState(initial = GamePreferences())
    val scope = rememberCoroutineScope()

    var screenName by rememberSaveable { mutableStateOf(Screen.SPLASH.name) }
    val screen = remember(screenName) { runCatching { Screen.valueOf(screenName) }.getOrDefault(Screen.HOME) }

    fun navigate(target: Screen) {
        screenName = target.name
    }

    BackHandler(enabled = screen != Screen.HOME && screen != Screen.SPLASH) {
        navigate(Screen.HOME)
    }

    when (screen) {
        Screen.SPLASH -> SplashScreen(onFinished = { navigate(Screen.HOME) })

        Screen.HOME -> HomeScreen(
            selectedCharacter = Characters.byId(prefs.selectedCharacterId),
            onNavigate = ::navigate
        )

        Screen.CHARACTERS -> CharacterSelectScreen(
            selectedCharacterId = prefs.selectedCharacterId,
            bestScore = prefs.bestScore,
            onSelect = { id ->
                scope.launch { repository.setSelectedCharacter(id) }
            },
            onBack = { navigate(Screen.HOME) }
        )

        Screen.GAME -> GameScreen(
            preferences = prefs,
            onExit = { navigate(Screen.HOME) }
        )

        Screen.RECORDS -> RecordsScreen(
            bestScore = prefs.bestScore,
            onBack = { navigate(Screen.HOME) }
        )

        Screen.OPTIONS -> OptionsScreen(
            prefs = prefs,
            onSoundChange = { v -> scope.launch { repository.setSoundEnabled(v) } },
            onMusicChange = { v -> scope.launch { repository.setMusicEnabled(v) } },
            onHapticsChange = { v -> scope.launch { repository.setHapticsEnabled(v) } },
            onControlModeChange = { m -> scope.launch { repository.setControlMode(m) } },
            onBack = { navigate(Screen.HOME) }
        )

        Screen.CREDITS -> CreditsScreen(onBack = { navigate(Screen.HOME) })
    }
}
