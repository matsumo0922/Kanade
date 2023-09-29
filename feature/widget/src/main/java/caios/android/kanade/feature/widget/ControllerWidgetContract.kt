package caios.android.kanade.feature.widget

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.glance.color.ColorProviders
import kotlin.math.ln

internal interface ControllerWidgetContract {
    object Args {
        const val KEY_CURRENT_SONG_ID = "key_current_song_id"
        const val KEY_CURRENT_SONG_TITLE = "key_current_song_title"
        const val KEY_CURRENT_SONG_ARTIST = "key_current_song_artist"
        const val KEY_CURRENT_SONG_ARTWORK = "key_current_song_artwork"
        const val KEY_IS_PLAYING = "key_player_state"
        const val KEY_IS_PLUS_USER = "key_is_plus_user"
    }

    object Actions {
        const val ACTION_PLAY = "caios.system.kanade3.play"
        const val ACTION_PAUSE = "caios.system.kanade3.pause"
        const val ACTION_SKIP_TO_NEXT = "caios.system.kanade3.skip_to_next"
        const val ACTION_SKIP_TO_PREVIOUS = "caios.system.kanade3.skip_to_previous"
    }
}

internal fun ColorProviders.surfaceColorAtElevation(context: Context, elevation: Dp): Color {
    if (elevation == 0.dp) return surface.getColor(context)
    val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
    return primary.getColor(context).copy(alpha = alpha).compositeOver(surface.getColor(context))
}
