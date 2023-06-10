package caios.android.kanade.core.model.music

import androidx.media3.common.MediaItem

sealed interface ControllerState {
    object Initialize : ControllerState
    data class Ready(val mediaItem: MediaItem?, val progress: Long) : ControllerState
    data class Progress(val progress: Long) : ControllerState
    data class Buffering(val progress: Long) : ControllerState
    data class Playing(val isPlaying: Boolean) : ControllerState
}
