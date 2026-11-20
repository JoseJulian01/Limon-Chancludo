package mx.udg.limonchancludo.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.gameDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "limon_chancludo_prefs"
)

enum class ControlMode { SWIPE, DPAD, BOTH }

data class GamePreferences(
    val selectedCharacterId: String = "clasico",
    val soundEnabled: Boolean = true,
    val musicEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val controlMode: ControlMode = ControlMode.SWIPE,
    val bestScore: Int = 0
)

/**
 * Persistencia local de ajustes, personaje elegido y mejor puntaje.
 */
class PreferencesRepository(private val context: Context) {

    private object Keys {
        val SELECTED_CHARACTER = stringPreferencesKey("selected_character")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val MUSIC_ENABLED = booleanPreferencesKey("music_enabled")
        val HAPTICS_ENABLED = booleanPreferencesKey("haptics_enabled")
        val CONTROL_MODE = stringPreferencesKey("control_mode")
        val BEST_SCORE = intPreferencesKey("best_score")
    }

    val flow: Flow<GamePreferences> = context.gameDataStore.data.map { p ->
        GamePreferences(
            selectedCharacterId = p[Keys.SELECTED_CHARACTER] ?: "clasico",
            soundEnabled = p[Keys.SOUND_ENABLED] ?: true,
            musicEnabled = p[Keys.MUSIC_ENABLED] ?: true,
            hapticsEnabled = p[Keys.HAPTICS_ENABLED] ?: true,
            controlMode = p[Keys.CONTROL_MODE]
                ?.let { runCatching { ControlMode.valueOf(it) }.getOrDefault(ControlMode.SWIPE) }
                ?: ControlMode.SWIPE,
            bestScore = p[Keys.BEST_SCORE] ?: 0
        )
    }

    suspend fun setSelectedCharacter(id: String) {
        context.gameDataStore.edit { it[Keys.SELECTED_CHARACTER] = id }
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.gameDataStore.edit { it[Keys.SOUND_ENABLED] = enabled }
    }

    suspend fun setMusicEnabled(enabled: Boolean) {
        context.gameDataStore.edit { it[Keys.MUSIC_ENABLED] = enabled }
    }

    suspend fun setHapticsEnabled(enabled: Boolean) {
        context.gameDataStore.edit { it[Keys.HAPTICS_ENABLED] = enabled }
    }

    suspend fun setControlMode(mode: ControlMode) {
        context.gameDataStore.edit { it[Keys.CONTROL_MODE] = mode.name }
    }

    /** Guarda solo si supera el récord anterior. */
    suspend fun submitScore(score: Int) {
        context.gameDataStore.edit { p ->
            if (score > (p[Keys.BEST_SCORE] ?: 0)) {
                p[Keys.BEST_SCORE] = score
            }
        }
    }
}
