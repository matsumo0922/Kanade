package caios.android.kanade.core.music.analyzer

import caios.android.kanade.core.common.network.di.ApplicationScope
import caios.android.kanade.core.datastore.PreferenceVolume
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.music.Volume
import caios.android.kanade.core.repository.MusicRepository
import com.arthenica.ffmpegkit.FFmpegKit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class VolumeAnalyzer @Inject constructor(
    private val musicRepository: MusicRepository,
    private val preferenceVolume: PreferenceVolume,
    @ApplicationScope private val scope: CoroutineScope,
) {
    private val cache = ConcurrentHashMap<Long, Volume>()
    private val _data = MutableStateFlow(emptyList<Volume>())

    init {
        scope.launch {
            preferenceVolume.data.collect { lyrics ->
                cache.clear()
                cache.putAll(lyrics.associateBy { it.songId })

                _data.value = lyrics.toList()
            }
        }
    }

    val data: SharedFlow<List<Volume>> = _data.asSharedFlow()

    fun getVolume(song: Song): Volume? {
        return cache[song.id]
    }

    fun isAnalyzed(song: Song): Boolean {
        return cache.containsKey(song.id)
    }

    suspend fun analyze(song: Song): Volume? = withContext(scope.coroutineContext) {
        cache[song.id] ?: kotlin.runCatching {
            musicRepository.useSongFile(song) { file ->
                analyzeVolume(song, file!!)
            }
        }.getOrNull()?.also {
            preferenceVolume.save(it)
        }
    }

    private fun analyzeVolume(song: Song, file: File): Volume? {
        val command = "-hide_banner -i \"${file.absolutePath}\" -vn -af volumedetect -f null -"
        val result = FFmpegKit.execute(command)

        return if (result.returnCode.isValueSuccess) {
            Timber.d("Successfully analyzed volume: ${song.title}")
            analyzeOutput(song, file, result.logsAsString)
        } else {
            Timber.e("Failed to analyze volume: ${song.title}")
            null
        }
    }

    private fun analyzeOutput(song: Song, file: File, output: String): Volume {
        fun String.substringAtoB(startIndex: Int, a: String, b: String): String? {
            if (startIndex < 0 || this.length < 3) return null

            val firstIndex = this.indexOf(a, startIndex)
            val secondIndex = this.indexOf(b, firstIndex + a.length)

            return substring(firstIndex + a.length, secondIndex)
        }

        val formatText = output.replace("\n", " ")
        val encoder = "encoder"
        val maxVolume = "max_volume"
        val meanVolume = "mean_volume"

        return Volume(
            songId = song.id,
            fileSize = file.length(),
            encoder = formatText.indexOf(encoder).let { formatText.substringAtoB(it, ": ", " ") },
            meanVolume = formatText.indexOf(maxVolume).let { formatText.substringAtoB(it, ": ", " ")?.toDoubleOrNull() },
            maxVolume = formatText.indexOf(meanVolume).let { formatText.substringAtoB(it, ": ", " ")?.toDoubleOrNull() },
        )
    }
}
