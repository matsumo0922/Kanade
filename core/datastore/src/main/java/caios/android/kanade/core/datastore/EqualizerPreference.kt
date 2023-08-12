package caios.android.kanade.core.datastore

import android.content.Context
import androidx.core.content.edit
import caios.android.kanade.core.model.music.Equalizer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class EqualizerPreference @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val preference by lazy { context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE) }

    fun getEqualizer(): Equalizer {
        val preferences = preference.all
        val bassBoost = preferences["bassBoost"] as? Float ?: 0f
        val hzs = preferences.filterKeys { it != "bassBoost" }.map { it.key.toInt() to it.value as Float }.toMap()

        return Equalizer(hzs, bassBoost)
    }

    fun setHz(hz: Int, gain: Float) {
        preference.edit {
            putFloat(hz.toString(), gain)
        }
    }

    fun setBassBoost(gain: Float) {
        preference.edit {
            putFloat("bassBoost", gain)
        }
    }

    companion object {
        private const val FILE_NAME = "EqualizerPreference"
    }
}
