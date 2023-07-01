package caios.android.kanade.feature.playlist.top

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import caios.android.kanade.core.design.animation.NavigateAnimation
import caios.android.kanade.core.model.music.Playlist

const val PlaylistTopRoute = "playlistTop"

fun NavController.navigateToPlaylistTop(navOptions: NavOptions? = null) {
    this.navigate(PlaylistTopRoute, navOptions)
}

fun NavGraphBuilder.playlistTopScreen(
    topMargin: Dp,
    navigateToPlaylistMenu: (Playlist) -> Unit,
    navigateToPlaylistEdit: () -> Unit,
) {
    composable(
        route = PlaylistTopRoute,
        enterTransition = {
            when (initialState.destination.route) {
                "homeTop", "songTop", "artistTop", "albumTop" -> NavigateAnimation.Library.enter
                else -> NavigateAnimation.Detail.popEnter
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                "homeTop", "songTop", "artistTop", "albumTop" -> NavigateAnimation.Library.exit
                else -> NavigateAnimation.Detail.exit
            }
        },
    ) {
        PlaylistTopRoute(
            modifier = Modifier.fillMaxSize(),
            topMargin = topMargin,
            navigateToPlaylistMenu = navigateToPlaylistMenu,
            navigateToPlaylistEdit = navigateToPlaylistEdit,
        )
    }
}
