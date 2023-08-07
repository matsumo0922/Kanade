package caios.android.kanade.feature.search.scan

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.repository.MusicRepository
import com.hippo.unifile.UniFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ScanMediaViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    @Dispatcher(KanadeDispatcher.IO) private val io: CoroutineDispatcher,
) : ViewModel() {

    val uiState = MutableStateFlow(ScanMediaUiState())

    init {
        viewModelScope.launch {
            musicRepository.config.map {
                uiState.value = uiState.value.copy(songs = musicRepository.sortedSongs(it))
            }
        }
    }

    fun scan(context: Context, uri: Uri) {
        viewModelScope.launch(io) {
            val paths = getPaths(context, uri)
            val mimeTypes = paths.map { MimeTypeMap.getSingleton().getMimeTypeFromExtension(getExtension(it)) }

            Timber.d("ScanMediaViewModel scan: paths=${paths.size}")

            uiState.value = uiState.value.copy(
                progress = if (paths.isEmpty()) -1 else 0,
                total = if (paths.isEmpty()) -1 else paths.size,
            )

            MediaScannerConnection.scanFile(context, paths.toTypedArray(), mimeTypes.toTypedArray()) { path, _ ->
                uiState.value = uiState.value.copy(
                    progress = uiState.value.progress + 1,
                    currentItem = path,
                )
            }
        }
    }

    suspend fun refreshLibrary() {
        musicRepository.clear()
        musicRepository.fetchSongs()
        musicRepository.fetchArtists()
        musicRepository.fetchAlbums()
        musicRepository.fetchPlaylist()
        musicRepository.fetchAlbumArtwork()
        musicRepository.fetchArtistArtwork()
        musicRepository.refresh()
    }

    private fun getExtension(path: String): String {
        val commandIndex = path.indexOfLast { it == '.' }
        if (commandIndex < 0 || commandIndex < path.length - 1) return ""

        return getExtension(path.substring(commandIndex + 1))
    }

    private fun getPaths(context: Context, uri: Uri): List<String> {
        val uniFile = UniFile.fromUri(context, uri)
        val paths = getPathFromDocumentFile(uniFile)

        Timber.d("ScanMediaViewModel getPaths: ${paths.size}")

        return paths
    }

    private fun getPathFromDocumentFile(uniFile: UniFile): List<String> {
        if (uniFile.isDirectory) return uniFile.listFiles()?.map { getPathFromDocumentFile(it) }?.flatten() ?: emptyList()
        if (uniFile.isFile) return listOfNotNull(uniFile.filePath)

        return emptyList()
    }
}

@Stable
data class ScanMediaUiState(
    val songs: List<Song>? = null,
    val total: Int = 0,
    val progress: Int = 0,
    val currentItem: String? = null,
)
