package caios.android.kanade.feature.download.format

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.datastore.DownloadPathPreference
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.download.VideoInfo
import com.hippo.unifile.UniFile
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DownloadFormatViewModel @Inject constructor(
    private val downloadPathPreference: DownloadPathPreference,
    @Dispatcher(KanadeDispatcher.IO) private val io: CoroutineDispatcher,
) : ViewModel() {

    private val _screenState = MutableStateFlow<ScreenState<DownloadFormatUiState>>(ScreenState.Loading)

    val screenState = _screenState.asStateFlow()

    fun fetch(context: Context, videoInfo: VideoInfo) {
        viewModelScope.launch {
            val uri = withContext(io) { downloadPathPreference.getUri() }
            val uniFile = UniFile.fromUri(context, uri)

            _screenState.value = ScreenState.Idle(
                DownloadFormatUiState(
                    videoInfo = videoInfo,
                    saveUniFile = uniFile,
                )
            )
        }
    }

    fun download(
        context: Context,
        videoInfo: VideoInfo,
        format: VideoInfo.Format,
        extractAudio: Boolean,
        uniFile: UniFile,
    ) {
        viewModelScope.launch(io) {
            downloadVideo(
                context = context,
                videoInfo = videoInfo,
                format = format,
                extractAudio = extractAudio,
                uniFile = uniFile,
            ) { progress, _, line ->
                updateDownloadState(progress, line)
            }.onFailure {
                downloadFailed()
            }
        }
    }

    fun updateSaveUri(context: Context, savePath: Uri) {
        downloadPathPreference.saveUri(savePath)

        val state = screenState.value
        val uniFile = UniFile.fromUri(context, savePath)

        if (state is ScreenState.Idle) {
            _screenState.value = ScreenState.Idle(state.data.copy(saveUniFile = uniFile))
        }
    }

    fun downloadComplete() {
        val state = screenState.value

        if (state is ScreenState.Idle) {
            _screenState.value = ScreenState.Idle(state.data.copy(downloadState = null))
        }
    }

    private fun downloadFailed() {
        val state = screenState.value

        if (state is ScreenState.Idle) {
            _screenState.value = ScreenState.Idle(state.data.copy(downloadState = DownloadFormatUiState.DownloadState.Failed))
        }
    }

    private fun updateDownloadState(progress: Float, line: String) {
        val state = screenState.value
        val download = DownloadFormatUiState.DownloadState.Progress(
            progress = progress * 0.01f,
            line = line,
        )

        if (state is ScreenState.Idle) {
            _screenState.value = ScreenState.Idle(state.data.copy(downloadState = download))
        }
    }

    private fun downloadVideo(
        context: Context,
        videoInfo: VideoInfo,
        format: VideoInfo.Format,
        extractAudio: Boolean,
        uniFile: UniFile,
        callback: (Float, Long, String) -> Unit
    ): Result<UniFile> {
        val url = videoInfo.originalUrl ?: return Result.failure(Exception("Invalid url"))
        val file = File(context.cacheDir, "sdcard_tmp").run { resolve(videoInfo.id) }

        val request = YoutubeDLRequest(url).apply {
            addOption("-v")
            addOption("--no-mtime")
            addOption("--no-playlist")

            // Enable aria2c
            addOption("--downloader", "libaria2c.so")
            addOption("--external-downloader-args", "aria2c:\"--summary-interval=1\"")

            if (extractAudio || videoInfo.acodec == "none") {
                applyAudioOptions(format)
            } else {
                applyVideoOptions(format)
            }

            addOption("-P", file.absolutePath)
            addOption("-o", "%(title).200B [%(id)s].%(ext)s")
        }

        Timber.d("Download Command: ${request.buildCommand().joinToString(" ")}")

        try {
            YoutubeDL.getInstance().execute(request, callback = callback)
        } catch (e: Throwable) {
            Timber.w(e)
            return Result.failure(e)
        }

        return moveFile(file, uniFile)
    }

    private fun YoutubeDLRequest.applyAudioOptions(format: VideoInfo.Format) {
        if (format.formatId.isNullOrBlank()) {
            addOption("--audio-format", "m4a")
        } else {
            addOption("-f", format.formatId!!)
        }

        addOption("-x")
        addOption("--embed-metadata")
        addOption("--embed-thumbnail")
        addOption("--convert-thumbnails", "jpg")
        addOption("--ppa", """ffmpeg: -c:v mjpeg -vf crop=\"'if(gt(ih,iw),iw,ih)':'if(gt(iw,ih),ih,iw)'\"""")

        addOption("--parse-metadata", "%(release_year,upload_date)s:%(meta_date)s")
        addOption("--parse-metadata", "%(album,title)s:%(meta_album)s")
    }

    private fun YoutubeDLRequest.applyVideoOptions(format: VideoInfo.Format) {
        if (!format.formatId.isNullOrBlank()) {
            addOption("--format", format.formatId!!)
        }
    }

    private fun moveFile(tempFile: File, parentUniFile: UniFile): Result<UniFile> {
        return kotlin.runCatching {
            val newUniFile = parentUniFile.createFile(tempFile.name)

            newUniFile.openOutputStream().use { output ->
                tempFile.inputStream().use { input ->
                    input.copyTo(output)
                }
            }

            tempFile.delete()
            newUniFile
        }
    }
}

@Stable
data class DownloadFormatUiState(
    val videoInfo: VideoInfo,
    val saveUniFile: UniFile? = null,
    val downloadState: DownloadState? = null,
) {
    sealed interface DownloadState {

        data class Progress(
            val progress: Float,
            val line: String,
        ) : DownloadState

        data object Failed : DownloadState
    }
}
