package mx.udg.limonchancludo.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.udg.limonchancludo.data.ControlMode
import mx.udg.limonchancludo.data.GamePreferences
import mx.udg.limonchancludo.data.RecordsRepository
import mx.udg.limonchancludo.game.audio.GameAudio
import mx.udg.limonchancludo.game.model.Characters
import mx.udg.limonchancludo.game.model.GameEvent
import mx.udg.limonchancludo.game.model.GameStatus
import mx.udg.limonchancludo.ui.GameViewModel
import mx.udg.limonchancludo.ui.components.DPad
import mx.udg.limonchancludo.ui.components.GameCanvas
import mx.udg.limonchancludo.ui.components.HudBar
import mx.udg.limonchancludo.ui.components.PixelButton
import mx.udg.limonchancludo.ui.components.SwipeArea

@Composable
fun GameScreen(
    preferences: GamePreferences,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: GameViewModel = viewModel()
    val snapshot by viewModel.snapshot.collectAsState()
    val character = remember(preferences.selectedCharacterId) { Characters.byId(preferences.selectedCharacterId) }
    val context = LocalContext.current

    val audio = remember(preferences.soundEnabled) { GameAudio(context, preferences.soundEnabled) }
    DisposableEffect(audio) {
        onDispose { audio.release() }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is GameEvent.LemonEaten -> audio.playEat()
                is GameEvent.PowerEaten -> audio.playEat()
                is GameEvent.PlayerDied -> audio.playDie()
                is GameEvent.LevelCleared -> audio.playLevelCleared()
            }
        }
    }

    // Pausa al salir de la pantalla y al pasar a segundo plano
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        viewModel.resumeFromLifecycle()
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> viewModel.pauseForLifecycle()
                Lifecycle.Event.ON_START -> viewModel.resumeFromLifecycle()
                else -> Unit
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
            viewModel.pauseForLifecycle()
        }
    }

    LaunchedEffect(snapshot.status, snapshot.score) {
        if (snapshot.status == GameStatus.GAME_OVER || snapshot.status == GameStatus.LEVEL_CLEARED) {
            RecordsRepository.updateBestScore(context, snapshot.score)
        }
    }

    BackHandler {
        if (viewModel.userPaused || snapshot.status != GameStatus.RUNNING) {
            onExit()
        } else {
            viewModel.toggleUserPause()
        }
    }

    val controlMode = preferences.controlMode
    val showSwipe = controlMode == ControlMode.SWIPE || controlMode == ControlMode.BOTH
    val showDpad = controlMode == ControlMode.DPAD || controlMode == ControlMode.BOTH

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        HudBar(
            snapshot = snapshot,
            bestScore = preferences.bestScore,
            onPause = { viewModel.toggleUserPause() }
        )

        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            GameCanvas(
                snapshot = snapshot,
                floatingTexts = viewModel.floatingTexts,
                playerSprite = character.sprite,
                modifier = Modifier.fillMaxSize()
            )

            if (showSwipe && snapshot.status == GameStatus.RUNNING && !viewModel.userPaused) {
                SwipeArea(onDirection = { viewModel.queueDirection(it) })
            }

            when (snapshot.status) {
                GameStatus.LEVEL_CLEARED -> Overlay(
                    title = "NIVEL LIMPIADO",
                    subtitle = "Puntaje: ${snapshot.score}",
                    titleColor = Color(0xFFFFE93B),
                    buttons = {
                        PixelButton(text = "REINICIAR", onClick = { viewModel.restart() })
                        PixelButton(text = "MENU", onClick = onExit)
                    }
                )

                GameStatus.GAME_OVER -> Overlay(
                    title = "FIN DEL JUEGO",
                    subtitle = "Puntaje: ${snapshot.score}",
                    titleColor = Color(0xFFFF4B4B),
                    buttons = {
                        PixelButton(text = "REINTENTAR", onClick = { viewModel.restart() })
                        PixelButton(text = "MENU", onClick = onExit)
                    }
                )

                GameStatus.RUNNING -> if (viewModel.userPaused) {
                    Overlay(
                        title = "PAUSA",
                        subtitle = "Nivel ${snapshot.level}",
                        titleColor = Color(0xFF6C7BFF),
                        buttons = {
                            PixelButton(text = "CONTINUAR", onClick = { viewModel.toggleUserPause() })
                            PixelButton(text = "REINICIAR", onClick = { viewModel.restart() })
                            PixelButton(text = "MENU", onClick = onExit)
                        }
                    )
                }
            }
        }

        if (showDpad) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                DPad(onDirection = { viewModel.queueDirection(it) })
            }
        }
    }
}

@Composable
private fun Overlay(
    title: String,
    subtitle: String,
    titleColor: Color,
    buttons: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xCC05051A)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                color = titleColor,
                fontFamily = FontFamily.Monospace,
                fontSize = 26.sp,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = subtitle,
                color = Color.White,
                fontFamily = FontFamily.Monospace,
                fontSize = 15.sp
            )
            Spacer(Modifier.height(4.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                content = buttons
            )
        }
    }
}
