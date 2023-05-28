package caios.android.kanade.feature.playlist.top

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable


const val playlistTopRoute = "playlistTop"

fun NavController.navigateToPlaylistTop(navOptions: NavOptions? = null) {
    this.navigate(playlistTopRoute, navOptions)
}

fun NavGraphBuilder.playlistTopScreen() {
    composable(route = playlistTopRoute) {
        PlaylistTopRoute()
    }
}