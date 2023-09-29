package caios.android.kanade.feature.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import caios.android.kanade.core.design.R
import caios.android.kanade.feature.widget.items.ControllerWidgetScreen

class ControllerWidget : GlanceAppWidget() {

    override val sizeMode = SizeMode.Exact

    @SuppressLint("WrongConstant")
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val state = currentState<Preferences>()

            val songId = state[longPreferencesKey(KEY_CURRENT_SONG_ID)] ?: 0L
            val songTitle = state[stringPreferencesKey(KEY_CURRENT_SONG_TITLE)] ?: context.getString(R.string.music_unknown_title)
            val songArtist = state[stringPreferencesKey(KEY_CURRENT_SONG_ARTIST)] ?: context.getString(R.string.music_unknown_artist)
            val songArtworkBytes = state[stringPreferencesKey(KEY_CURRENT_SONG_ARTWORK)] ?: ""
            val isPlaying = state[booleanPreferencesKey(KEY_IS_PLAYING)] ?: false
            val isPlusUser = state[booleanPreferencesKey(KEY_IS_PLUS_USER)] ?: false

            val artworkBitmap = decodeArtwork(songArtworkBytes)

            GlanceTheme {
                ControllerWidgetScreen(
                    songTitle = songTitle,
                    songArtwork = artworkBitmap,
                    isPlaying = isPlaying,
                    isPlusUser = isPlusUser,
                )
            }
        }
    }

    private fun decodeArtwork(artworkBytes: String): Bitmap? = runCatching {
        val bytes = Base64.decode(artworkBytes, 0)
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }.getOrNull()

    companion object {
        const val KEY_CURRENT_SONG_ID = "key_current_song_id"
        const val KEY_CURRENT_SONG_TITLE = "key_current_song_title"
        const val KEY_CURRENT_SONG_ARTIST = "key_current_song_artist"
        const val KEY_CURRENT_SONG_ARTWORK = "key_current_song_artwork"
        const val KEY_IS_PLAYING = "key_player_state"
        const val KEY_IS_PLUS_USER = "key_is_plus_user"

        const val ACTION_PLAY = "caios.system.kanade3.play"
        const val ACTION_PAUSE = "caios.system.kanade3.pause"
        const val ACTION_SKIP_TO_NEXT = "caios.system.kanade3.skip_to_next"
        const val ACTION_SKIP_TO_PREVIOUS = "caios.system.kanade3.skip_to_previous"
    }
}
