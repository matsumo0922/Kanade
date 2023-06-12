package caios.android.kanade.core.music

import androidx.media3.common.MediaItem
import caios.android.kanade.core.model.music.ControllerEvent
import caios.android.kanade.core.model.music.ControllerState
import caios.android.kanade.core.model.music.Queue
import caios.android.kanade.core.model.music.RepeatMode
import caios.android.kanade.core.model.music.ShuffleMode
import kotlinx.coroutines.flow.StateFlow

interface MusicController {
    val state: StateFlow<ControllerState>

    suspend fun onControllerEvent(event: ControllerEvent)

    suspend fun restorePlayerState(
        currentItems: List<MediaItem>,
        originalItems: List<MediaItem>,
        index: Int,
        progress: Long,
        shuffleMode: ShuffleMode,
        repeatMode: RepeatMode,
    )

    fun onPlayWithNewQueue(index: Int, queue: List<MediaItem>, playWhenReady: Boolean)
    fun getQueue(): Queue
}
