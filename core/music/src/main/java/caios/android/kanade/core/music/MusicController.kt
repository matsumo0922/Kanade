package caios.android.kanade.core.music

import caios.android.kanade.core.model.music.ControllerEvent
import caios.android.kanade.core.model.music.ControllerState
import caios.android.kanade.core.model.music.Song
import kotlinx.coroutines.flow.StateFlow

interface MusicController {
    val state: StateFlow<ControllerState>

    suspend fun onControllerEvent(event: ControllerEvent)
    fun onPlayFromMediaId(index: Int, queue: List<Song>, playWhenReady: Boolean)
}