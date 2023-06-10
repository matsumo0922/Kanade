package caios.android.kanade.feature.song.top

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import caios.android.kanade.core.model.music.Song

const val songTopRoute = "songTop"

fun NavController.navigateToSongTop(navOptions: NavOptions? = null) {
    this.navigate(songTopRoute, navOptions)
}

fun NavGraphBuilder.songTopScreen(
    topMargin: Dp,
    onClickSong: (Int, List<Song>) -> Unit,
) {
    composable(route = songTopRoute) {
        SongTopRoute(
            topMargin = topMargin,
            onClickSong = onClickSong,
        )
    }
}
