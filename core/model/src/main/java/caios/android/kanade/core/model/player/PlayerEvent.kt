package caios.android.kanade.core.model.player

import caios.android.kanade.core.model.music.Song

sealed interface PlayerEvent {

    data class Initialize(val playWhenReady: Boolean) : PlayerEvent

    data class NewPlay(
        val index: Int,
        val queue: List<Song>,
        val playWhenReady: Boolean,
    ) : PlayerEvent

    data object Play : PlayerEvent

    data object Pause : PlayerEvent

    data object PauseTransient : PlayerEvent

    data object Stop : PlayerEvent

    data object SkipToNext : PlayerEvent

    data object SkipToPrevious : PlayerEvent

    data class SkipToQueue(
        val index: Int,
        val playWhenReady: Boolean? = null,
    ) : PlayerEvent

    data class Seek(val progress: Float) : PlayerEvent

    data class Shuffle(val shuffleMode: ShuffleMode) : PlayerEvent

    data class Repeat(val repeatMode: RepeatMode) : PlayerEvent

    data class Dack(val isEnabled: Boolean) : PlayerEvent
}
