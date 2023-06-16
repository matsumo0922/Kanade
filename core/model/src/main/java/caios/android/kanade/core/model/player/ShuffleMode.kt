package caios.android.kanade.core.model.player

import android.support.v4.media.session.PlaybackStateCompat

enum class ShuffleMode {
    ON, OFF;

    companion object {
        fun ShuffleMode.toPlaybackState(): Int {
            return when (this) {
                ON -> PlaybackStateCompat.SHUFFLE_MODE_ALL
                OFF -> PlaybackStateCompat.SHUFFLE_MODE_NONE
            }
        }

        fun fromPlaybackState(state: Int): ShuffleMode {
            return when (state) {
                PlaybackStateCompat.SHUFFLE_MODE_ALL -> ON
                PlaybackStateCompat.SHUFFLE_MODE_NONE -> OFF
                else -> OFF
            }
        }
    }
}
