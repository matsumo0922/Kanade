package caios.android.kanade.feature.search.scan

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.storage.StorageManager
import android.os.storage.StorageVolume
import android.provider.DocumentsContract
import android.webkit.MimeTypeMap
import androidx.compose.runtime.Stable
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
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

            uiState.value = uiState.value.copy(
                progress = 0,
                total = paths.size,
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
        val storageManager = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val documentsContract = DocumentsContract.buildDocumentUriUsingTree(uri, DocumentsContract.getTreeDocumentId(uri))
        val documentsTree = DocumentFile.fromTreeUri(context, documentsContract) ?: return emptyList()

        return documentsTree.listFiles().map { getPathFromDocumentFile(storageManager, it) }.flatten().filterNotNull()
    }

    @SuppressLint("DiscouragedPrivateApi")
    private fun getPathFromDocumentFile(storageManager: StorageManager, documentFile: DocumentFile): List<String?> {
        if (documentFile.isFile) {
            val documentId = DocumentsContract.getDocumentId(documentFile.uri)
            val documentParts = documentId.split(":")
            val documentType = documentParts.first()

            for (volume in storageManager.storageVolumes) {
                if ((volume.isPrimary && documentType.equals("primary", true)) || volume.uuid?.equals(documentType, true) == true) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        return listOf(File(volume.directory?.absolutePath, documentParts[1]).absolutePath)
                    } else {
                        val getPath = StorageVolume::class.java.getDeclaredMethod("getPath")
                        return listOf(getPath.invoke(volume) as String?)
                    }
                }
            }

            return listOf(null)
        }

        if (documentFile.isDirectory) {
            return documentFile.listFiles().map { getPathFromDocumentFile(storageManager, it) }.flatten()
        }

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
