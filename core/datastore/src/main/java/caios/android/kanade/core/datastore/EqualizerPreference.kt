package caios.android.kanade.core.datastore

import android.content.Context
import androidx.core.content.edit
import caios.android.kanade.core.model.music.Equalizer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class EqualizerPreference @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val preference by lazy { context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE) }

    private var _data = MutableStateFlow(
        Equalizer(
            bands = listOf(),
            bassBoost = 0f,
            preset = Equalizer.Preset.NONE,
        )
    )

    val data = _data.asStateFlow()

    fun setPreset(preset: Equalizer.Preset) {
        preference.edit {
            putInt("preset", preset.ordinal)
        }
        updatePresetData(preset)
    }

    fun getPreset(): Equalizer.Preset {
        val value = preference.getInt("preset", 0)
        val preset = Equalizer.Preset.values().getOrNull(value) ?: Equalizer.Preset.NONE

        updatePresetData(preset)

        return preset
    }

    fun setHz(hz: Int, value: Float) {
        preference.edit {
            putFloat(hz.toString(), value)
        }
        updateHz(hz, value)
    }

    fun getHz(hz: Int): Float {
        return preference.getFloat(hz.toString(), 0f).also {
            updateHz(hz, it)
        }
    }

    fun setBassBoost(gain: Float) {
        preference.edit {
            putFloat("bassBoost", gain)
        }
        updateBassBoost(gain)
    }

    fun getBassBoost(): Float {
        return preference.getFloat("bassBoost", 0f).also {
            updateBassBoost(it)
        }
    }

    private fun updatePresetData(preset: Equalizer.Preset) {
        _data.value = _data.value.copy(
            preset = preset,
            bands = _data.value.bands.map { it.copy(value = getHz(it.hz)) }
        )
    }

    private fun updateHz(hz: Int, value: Float) {
        _data.value = _data.value.copy(
            bands = _data.value.bands.map { if (it.hz == hz) it.copy(value = value) else it }
        )
    }

    private fun updateBassBoost(gain: Float) {
        _data.value = _data.value.copy(
            bassBoost = gain
        )
    }

    companion object {
        private const val FILE_NAME = "EqualizerPreference"
    }
}
