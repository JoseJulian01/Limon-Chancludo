package mx.udg.limonchancludo.game.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log

class GameAudio(context: Context, enabled: Boolean) {
    private val soundPool = if (enabled) {
        SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .build())
            .build()
    } else null

    // IDs de los sonidos cargados
    private var eatId: Int = 0
    private var dieId: Int = 0
    private var levelId: Int = 0

    init {
        if (soundPool != null) {
            // Nota: En una implementación real requeriría archivos en res/raw/
            // Por ahora, el SoundPool inicializa pero los IDs serán 0 si no existen.
            Log.d("GameAudio", "SoundPool inicializado")
        }
    }

    fun playEat() {
        if (soundPool != null && eatId != 0) soundPool.play(eatId, 1f, 1f, 1, 0, 1f)
    }

    fun playDie() {
        if (soundPool != null && dieId != 0) soundPool.play(dieId, 1f, 1f, 1, 0, 1f)
    }
    
    fun playLevelCleared() {
        if (soundPool != null && levelId != 0) soundPool.play(levelId, 1f, 1f, 1, 0, 1f)
    }

    fun release() {
        soundPool?.release()
    }
}
