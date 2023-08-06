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
import kotlin.reflect.KClass

const val PlaylistTopRoute = "playlistTop"

fun NavController.navigateToPlaylistTop(navOptions: NavOptions? = null) {
    this.navigate(PlaylistTopRoute, navOptions)
}

fun NavGraphBuilder.playlistTopScreen(
    topMargin: Dp,
    navigateToPlaylistDetail: (Long) -> Unit,
    navigateToPlaylistMenu: (Playlist) -> Unit,
    navigateToPlaylistEdit: () -> Unit,
    navigateToSort: (KClass<*>) -> Unit,
) {
    composable(
        route = PlaylistTopRoute,
        enterTransition = {
            when (initialState.destination.route) {
                "homeTop", "songTop", "artistTop", "albumTop" -> NavigateAnimation.Library.enter
                else -> NavigateAnimation.Vertical.popEnter
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                "homeTop", "songTop", "artistTop", "albumTop" -> NavigateAnimation.Library.exit
                else -> NavigateAnimation.Vertical.exit
            }
        },
    ) {
        PlaylistTopRoute(
            modifier = Modifier.fillMaxSize(),
            topMargin = topMargin,
            navigateToPlaylistDetail = navigateToPlaylistDetail,
            navigateToPlaylistMenu = navigateToPlaylistMenu,
            navigateToPlaylistEdit = navigateToPlaylistEdit,
            navigateToSort = navigateToSort,
        )
    }
}
