package caios.android.kanade.core.model.player

import android.support.v4.media.session.PlaybackStateCompat

enum class RepeatMode {
    OFF, ONE, ALL;

    companion object {
        fun RepeatMode.toPlaybackState(): Int {
            return when (this) {
                OFF -> PlaybackStateCompat.REPEAT_MODE_NONE
                ONE -> PlaybackStateCompat.REPEAT_MODE_ONE
                ALL -> PlaybackStateCompat.REPEAT_MODE_ALL
            }
        }

        fun fromPlaybackState(state: Int): RepeatMode {
            return when (state) {
                PlaybackStateCompat.REPEAT_MODE_NONE -> OFF
                PlaybackStateCompat.REPEAT_MODE_ONE -> ONE
                PlaybackStateCompat.REPEAT_MODE_ALL -> ALL
                else -> OFF
            }
        }
    }
}
