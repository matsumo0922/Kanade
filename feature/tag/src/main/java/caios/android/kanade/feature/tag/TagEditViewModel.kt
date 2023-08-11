package caios.android.kanade.feature.tag

import android.content.Context
import android.media.MediaScannerConnection
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.music.Tag
import caios.android.kanade.core.repository.MusicRepository
import com.arthenica.ffmpegkit.FFmpegKit
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class TagEditViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    @ApplicationContext private val context: Context,
    @Dispatcher(KanadeDispatcher.IO) private val io: CoroutineDispatcher,
) : ViewModel() {

    private val _screenState = MutableStateFlow<ScreenState<TagEditUiState>>(ScreenState.Loading)

    val screenState = _screenState.asStateFlow()

    fun fetch(songId: Long) {
        viewModelScope.launch {
            val song = musicRepository.getSong(songId)

            _screenState.value = if (song != null) {
                ScreenState.Idle(TagEditUiState(song))
            } else {
                ScreenState.Error(
                    message = R.string.error_no_data,
                    retryTitle = R.string.common_close,
                )
            }
        }
    }

    suspend fun edit(song: Song, tag: Tag): Boolean = withContext(io) {
        kotlin.runCatching {
            musicRepository.useSongFile(song) { file ->
                tagEdit(song, tag, file!!)
            }

            scan(song)
            refreshLibrary()
        }.fold(
            onSuccess = { true },
            onFailure = {
                Timber.e(it)
                false
            },
        )
    }

    private fun tagEdit(song: Song, tag: Tag, inputFile: File) {
        val outputFile = File(context.cacheDir, "${inputFile.nameWithoutExtension}-output.${inputFile.extension}")

        if (outputFile.exists()) outputFile.delete()

        val command = createCommand(tag, inputFile, outputFile)
        val result = FFmpegKit.execute(command)

        if (!result.returnCode.isValueSuccess || !outputFile.exists()) {
            error("Tag edit failed. [${result.returnCode.isValueSuccess}, ${outputFile.exists()}] $command")
        }

        context.contentResolver.openFileDescriptor(song.uri, "w")?.use { parcelFileDescriptor ->
            val fileOutputStream = FileOutputStream(parcelFileDescriptor.fileDescriptor)
            val fileInputStream = FileInputStream(outputFile)

            fileInputStream.copyTo(fileOutputStream)

            fileInputStream.close()
            fileOutputStream.close()
        }
    }

    private fun scan(song: Song) {
        MediaScannerConnection.scanFile(context, arrayOf(song.data), null) { path, uri ->
            Timber.d("MediaScannerConnection scanned $path, $uri")
        }
    }

    private suspend fun refreshLibrary() {
        musicRepository.clear()
        musicRepository.fetchSongs()
        musicRepository.fetchArtists()
        musicRepository.fetchAlbums()
        musicRepository.fetchPlaylist()
        musicRepository.fetchAlbumArtwork()
        musicRepository.fetchArtistArtwork()
        musicRepository.refresh()
    }

    private fun createCommand(tag: Tag, inputFile: File, outputFile: File): String {
        var command = "-hide_banner -i \"${inputFile.absolutePath}\""
        val metadata = mutableListOf<String>()

        if (tag.title.isNotBlank()) metadata.add("title=\"${tag.title}\"")
        if (tag.artist.isNotBlank()) metadata.add("artist=\"${tag.artist}\"")
        if (tag.album.isNotBlank()) metadata.add("album=\"${tag.album}\"")
        if (tag.composer.isNotBlank()) metadata.add("composer=\"${tag.composer}\"")
        if (tag.genre.isNotBlank()) metadata.add("genre=\"${tag.genre}\"")
        if (tag.year.isNotBlank()) metadata.add("year=\"${tag.year}\"")
        if (tag.track.isNotBlank() && tag.trackTotal.isNotBlank()) metadata.add("track=\"${tag.track}/${tag.trackTotal}\"")
        if (tag.disc.isNotBlank() && tag.discTotal.isNotBlank()) metadata.add("disc=\"${tag.disc}/${tag.discTotal}\"")

        for (data in metadata) {
            command += " -metadata $data"
        }

        command += " -c copy -id3v2_version 3 \"${outputFile.absolutePath}\" -y"

        return command
    }
}

@Stable
data class TagEditUiState(
    val song: Song,
)
