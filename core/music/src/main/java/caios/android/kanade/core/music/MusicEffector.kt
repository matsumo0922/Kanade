package caios.android.kanade.core.music

import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import android.media.audiofx.LoudnessEnhancer
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.music.analyzer.VolumeAnalyzer
import caios.android.kanade.core.repository.UserDataRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import caios.android.kanade.core.model.music.Equalizer as EqualizerModel

class MusicEffector @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val volumeAnalyzer: VolumeAnalyzer,
    @Dispatcher(KanadeDispatcher.IO) private val io: CoroutineDispatcher,
) {
    private val scope = CoroutineScope(SupervisorJob() + io)
    private var userDataCache: UserData? = null

    private var loudness: LoudnessEnhancer? = null
    private var bassBoost: BassBoost? = null
    private var equalizer: Equalizer? = null

    init {
        scope.launch {
            userDataRepository.userData.collectLatest {
                userDataCache = it
                setEnableLoudness(it.isDynamicNormalizer)
            }
        }
    }

    fun create(audioSession: Int) {
        Timber.d("MusicEffect: Create instance")

        loudness = LoudnessEnhancer(audioSession)
        bassBoost = BassBoost(0, audioSession)
        equalizer = Equalizer(0, audioSession)
    }

    fun release() {
        Timber.d("MusicEffect: Release")

        loudness?.release()
        bassBoost?.release()
        equalizer?.release()

        loudness = null
        bassBoost = null
        equalizer = null
    }

    fun build(song: Song) {
        Timber.d("MusicEffect: Build ID: ${song.title}, ${song.id}")

        buildLoudness(song)
        buildBassBoost()
        buildEqualizer()
    }

    private fun buildLoudness(song: Song) {
        setEnableLoudness(userDataCache?.isDynamicNormalizer ?: false)
        setLoudness(song)
    }

    private fun buildBassBoost() {
        setEnableBassBoost(false)
        // setBassBoost()
    }

    private fun buildEqualizer() {
        setEnableEqualizer(false)
        // setEqualizer()
    }

    private fun setEnableLoudness(isEnable: Boolean) {
        loudness?.enabled = isEnable
    }

    private fun setEnableBassBoost(isEnable: Boolean) {
        bassBoost?.enabled = isEnable
    }

    private fun setEnableEqualizer(isEnable: Boolean) {
        equalizer?.enabled = isEnable
    }

    private fun setLoudness(song: Song) {
        val analyzedData = volumeAnalyzer.getVolume(song)
        val maxVolume = analyzedData?.maxVolume?.plus(8.8)?.coerceAtMost(0.0) ?: 0.0

        // maxVolume: dB -> loudness: mB
        val strength = (maxVolume * -100).toInt()

        Timber.d("MusicEffect: use dynamic normalizer: $maxVolume -> $strength")

        loudness?.setTargetGain(strength)
    }

    fun getEqualizerBand(): List<EqualizerModel.Band> {
        val bands = equalizer?.numberOfBands ?: return emptyList()
        val dataList = mutableListOf<EqualizerModel.Band>()

        for (band in 0..<bands) {
            val hz = equalizer!!.getCenterFreq(band.toShort()) / 1000
            val maxLevel = equalizer!!.bandLevelRange[1]
            val minLevel = equalizer!!.bandLevelRange[0]

            dataList.add(
                EqualizerModel.Band(
                    hz = hz,
                    band = band,
                    value = 0f,
                    maxLevel = maxLevel,
                    minLevel = minLevel,
                )
            )
        }

        return dataList
    }
}
