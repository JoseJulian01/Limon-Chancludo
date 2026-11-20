package mx.udg.limonchancludo.data

import android.content.Context

object RecordsRepository {

    suspend fun getBestScore(context: Context): Int {
        return runCatching {
            PreferencesRepository(context.applicationContext).flow
        }.getOrNull()?.let { flow ->
            // Se puede leer del flujo de preferencias
            0
        } ?: 0
    }

    suspend fun updateBestScore(context: Context, score: Int) {
        runCatching {
            PreferencesRepository(context.applicationContext).submitScore(score)
        }
    }
}
