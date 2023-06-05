package caios.android.kanade.core.model.music

sealed interface ControllerState {
    object Initialize: ControllerState
    data class Ready(val duration: Long) : ControllerState
    data class Progress(val progress: Long) : ControllerState
    data class Buffering(val progress: Long) : ControllerState
    data class Playing(val isPlaying: Boolean) : ControllerState
}