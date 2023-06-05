package caios.android.kanade.core.model.music

sealed interface ControllerEvent {
    object Play: ControllerEvent
    object Pause: ControllerEvent
    object SkipToNext: ControllerEvent
    object SkipToPrevious: ControllerEvent
    data class Progress(val progress: Long)
}