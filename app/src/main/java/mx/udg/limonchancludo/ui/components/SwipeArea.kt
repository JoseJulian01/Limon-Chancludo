package mx.udg.limonchancludo.ui.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import mx.udg.limonchancludo.game.model.Direction
import kotlin.math.abs

private const val DRAG_THRESHOLD_PX = 28f

/**
 * Superficie de gestos: acumula el arrastre y emite la dirección
 * dominante cuando supera el umbral (control por swipe).
 */
@Composable
fun SwipeArea(
    onDirection: (Direction) -> Unit,
    modifier: Modifier = Modifier
) {
    var accX by remember { mutableFloatStateOf(0f) }
    var accY by remember { mutableFloatStateOf(0f) }

    fun emitReady() {
        when {
            abs(accX) >= DRAG_THRESHOLD_PX && abs(accX) >= abs(accY) -> {
                onDirection(if (accX > 0) Direction.RIGHT else Direction.LEFT)
                accX = 0f
                accY = 0f
            }

            abs(accY) >= DRAG_THRESHOLD_PX -> {
                onDirection(if (accY > 0) Direction.DOWN else Direction.UP)
                accX = 0f
                accY = 0f
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        accX = 0f
                        accY = 0f
                    },
                    onDragCancel = {
                        accX = 0f
                        accY = 0f
                    },
                    onDragEnd = {
                        emitReady()
                    }
                ) { _, dragAmount ->
                    accX += dragAmount.x
                    accY += dragAmount.y
                    emitReady()
                }
            }
    )
}
