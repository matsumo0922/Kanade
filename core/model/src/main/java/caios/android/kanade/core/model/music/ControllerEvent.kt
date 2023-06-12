package caios.android.kanade.core.model.music

sealed interface ControllerEvent {
    object Play : ControllerEvent
    object Pause : ControllerEvent
    object SkipToNext : ControllerEvent
    object SkipToPrevious : ControllerEvent
    object Stop : ControllerEvent
    data class Seek(val progress: Float) : ControllerEvent
    data class Shuffle(val shuffleMode: ShuffleMode) : ControllerEvent
    data class Repeat(val repeatMode: RepeatMode) : ControllerEvent
}
