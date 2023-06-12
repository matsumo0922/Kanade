package caios.android.kanade.feature.playlist.top

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val PlaylistTopRoute = "playlistTop"

fun NavController.navigateToPlaylistTop(navOptions: NavOptions? = null) {
    this.navigate(PlaylistTopRoute, navOptions)
}

fun NavGraphBuilder.playlistTopScreen(
    topMargin: Dp,
) {
    composable(route = PlaylistTopRoute) {
        PlaylistTopRoute(
            topMargin = topMargin,
        )
    }
}
