package caios.android.kanade.core.model.player

import caios.android.kanade.core.model.music.Song

sealed interface PlayerEvent {

    data class Initialize(val playWhenReady: Boolean) : PlayerEvent

    data class NewPlay(
        val index: Int,
        val queue: List<Song>,
        val playWhenReady: Boolean,
    ) : PlayerEvent

    object Play : PlayerEvent

    object Pause : PlayerEvent

    object PauseTransient : PlayerEvent

    object Stop : PlayerEvent

    object SkipToNext : PlayerEvent

    object SkipToPrevious : PlayerEvent

    data class SkipToQueue(val index: Int) : PlayerEvent

    data class Seek(val progress: Float) : PlayerEvent

    data class Shuffle(val shuffleMode: ShuffleMode) : PlayerEvent

    data class Repeat(val repeatMode: RepeatMode) : PlayerEvent

    data class Dack(val isEnabled: Boolean) : PlayerEvent
}
