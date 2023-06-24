package caios.android.kanade.core.ui.amlv

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import caios.android.kanade.core.model.music.Lyrics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun rememberLyricsViewState(lyrics: Lyrics?, listener: LyricsViewState.Listener): LyricsViewState {
    val scope = rememberCoroutineScope()
    return remember(scope, lyrics) { LyricsViewState(lyrics, scope, listener) }
}

@Stable
class LyricsViewState(
    lyrics: Lyrics?,
    private val scope: CoroutineScope,
    private val listener: Listener,
) {
    val lyrics: Lyrics?

    private val lineCount = lyrics?.lines?.size ?: 0

    var position by mutableStateOf(0L)

    internal var currentLineIndex by mutableStateOf(-1)
        private set

    var isPlaying by mutableStateOf(false)
        private set

    private var playbackJob: Job? = null

    init {
        this.lyrics = lyrics?.run {
            // Make sure all lines are sorted by the start time
            copy(lines = lines.sortedBy { it.startAt }.map { it.copy(startAt = (it.startAt - 500).coerceAtLeast(0)) })
        }
    }

    fun play() {
        if (lyrics == null) return

        val lines = lyrics.lines
        if (lines.isEmpty()) return

        if (position !in 0..lyrics.optimalDurationMillis) {
            return
        }

        playbackJob?.cancel()
        playbackJob = scope.launch {
            currentLineIndex = findLineIndexAt(position)
            isPlaying = true
        }

        playbackJob!!.invokeOnCompletion { cause ->
            if (cause == null) {
                isPlaying = false
            }
        }
    }

    fun pause() {
        isPlaying = false
        playbackJob?.cancel()
    }

    fun seekToLine(index: Int) {
        val lines = lyrics?.lines ?: return
        val idx = index.coerceIn(-1, lineCount - 1)
        val position = if (idx >= 0) lines[idx].startAt else 0L

        seekTo(position)
        listener.onSeek(position)
    }

    fun seekTo(position: Long) {
        val playAfterSeeking = isPlaying
        if (isPlaying) {
            playbackJob?.cancel()
        }
        this.position = position
        if (playAfterSeeking) {
            play()
        } else {
            currentLineIndex = findLineIndexAt(position)
        }
    }

    private fun findLineIndexAt(position: Long): Int {
        if (position < 0 || lyrics == null) return -1
        val lines = lyrics.lines
        for (i in lines.lastIndex downTo 0) {
            if (position >= lines[i].startAt) {
                return i
            }
        }
        return -1
    }

    interface Listener {
        fun onSeek(position: Long)
    }
}
