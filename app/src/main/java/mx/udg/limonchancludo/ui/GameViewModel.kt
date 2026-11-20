package mx.udg.limonchancludo.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import mx.udg.limonchancludo.game.engine.FIXED_STEP
import mx.udg.limonchancludo.game.engine.GameEngine
import mx.udg.limonchancludo.game.model.Direction
import mx.udg.limonchancludo.game.model.GameEvent
import mx.udg.limonchancludo.game.model.GameSnapshot

data class FloatingText(val col: Int, val row: Int, val text: String, val bornAt: Float)

/**
 * Expone el estado del motor a Compose y ejecuta el bucle a 60 Hz
 * con paso fijo e interpolación acumulada.
 */
class GameViewModel : ViewModel() {

    private val engine = GameEngine(0)

    private val _snapshot = MutableStateFlow(engine.snapshot())
    val snapshot: StateFlow<GameSnapshot> = _snapshot.asStateFlow()

    private val _events = MutableSharedFlow<GameEvent>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<GameEvent> = _events.asSharedFlow()

    var floatingTexts by mutableStateOf(listOf<FloatingText>())
        private set

    /** Pausa abierta por el jugador (overlay). */
    var userPaused by mutableStateOf(false)
        private set

    /** Pausa por ciclo de vida (app en segundo plano). */
    var lifecyclePaused by mutableStateOf(false)
        private set

    val isRunning: Boolean get() = !userPaused && !lifecyclePaused

    private var lastFrameTime = 0L
    private var accumulator = 0f
    private var loopJob: Job? = null

    init {
        startLoop()
    }

    private fun startLoop() {
        loopJob = viewModelScope.launch {
            lastFrameTime = System.nanoTime()
            while (isActive) {
                val now = System.nanoTime()
                val dt = ((now - lastFrameTime) / 1_000_000_000f).coerceAtMost(0.05f)
                lastFrameTime = now

                if (isRunning) {
                    accumulator += dt
                    while (accumulator >= FIXED_STEP) {
                        val events = engine.step(FIXED_STEP)
                        val nowTime = engine.elapsed
                            for (e in events) {
                                _events.tryEmit(e)
                                when (e) {
                                    is GameEvent.LemonEaten -> addFloating(e.col, e.row, "+${e.points}", nowTime)
                                    is GameEvent.PowerEaten -> addFloating(e.col, e.row, "+${e.points}", nowTime)
                                    is GameEvent.SandalEaten -> addFloating(
                                        engine.state.player.col,
                                        engine.state.player.row,
                                        "+${e.points}",
                                        nowTime
                                    )
                                    else -> Unit
                                }
                            }
                        accumulator -= FIXED_STEP
                    }
                    val snap = engine.snapshot()
                    _snapshot.value = snap
                    
                    // Prune texts
                    if (floatingTexts.isNotEmpty()) {
                        floatingTexts = floatingTexts.filter { snap.timeSeconds - it.bornAt < 0.8f }
                    }
                }
                delay(16)
            }
        }
    }

    private fun addFloating(col: Int, row: Int, text: String, at: Float) {
        floatingTexts = floatingTexts + FloatingText(col, row, text, at)
    }

    fun queueDirection(dir: Direction) {
        if (isRunning) engine.queueDirection(dir)
    }

    fun pauseForLifecycle() {
        lifecyclePaused = true
    }

    fun resumeFromLifecycle() {
        lifecyclePaused = false
        lastFrameTime = System.nanoTime()
    }

    fun toggleUserPause() {
        userPaused = !userPaused
        lastFrameTime = System.nanoTime()
        _snapshot.value = engine.snapshot()
    }

    fun restart() {
        engine.restart()

        userPaused = false
        lifecyclePaused = false
        accumulator = 0f
        lastFrameTime = System.nanoTime()
        _snapshot.value = engine.snapshot()
    }

    override fun onCleared() {
        loopJob?.cancel()
    }
}
