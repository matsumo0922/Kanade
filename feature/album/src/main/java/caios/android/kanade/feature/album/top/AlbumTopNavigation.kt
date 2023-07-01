package caios.android.kanade.feature.album.top

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import caios.android.kanade.core.design.animation.NavigateAnimation
import caios.android.kanade.core.model.music.Album

const val AlbumTopRoute = "albumTop"

fun NavController.navigateToAlbumTop(navOptions: NavOptions? = null) {
    this.navigate(AlbumTopRoute, navOptions)
}

fun NavGraphBuilder.albumTopScreen(
    topMargin: Dp,
    navigateToAlbumDetail: (Long) -> Unit,
    navigateToAlbumMenu: (Album) -> Unit,
) {
    composable(
        route = AlbumTopRoute,
        enterTransition = {
            when (initialState.destination.route) {
                "homeTop", "playlistTop", "songTop", "artistTop" -> NavigateAnimation.Library.enter
                else -> NavigateAnimation.Detail.popEnter
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                "homeTop", "playlistTop", "songTop", "artistTop" -> NavigateAnimation.Library.exit
                else -> NavigateAnimation.Detail.exit
            }
        },
    ) {
        AlbumTopRoute(
            topMargin = topMargin,
            navigateToAlbumDetail = navigateToAlbumDetail,
            navigateToAlbumMenu = navigateToAlbumMenu,
        )
    }
}
