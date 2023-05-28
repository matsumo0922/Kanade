package caios.android.kanade.feature.song.top

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val songTopRoute = "songTop"

fun NavController.navigateToSongTop(navOptions: NavOptions? = null) {
    this.navigate(songTopRoute, navOptions)
}

fun NavGraphBuilder.songTopScreen() {
    composable(route = songTopRoute) {
        SongTopRoute()
    }
}
