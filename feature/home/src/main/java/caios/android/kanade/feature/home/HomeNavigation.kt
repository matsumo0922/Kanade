package caios.android.kanade.feature.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import caios.android.kanade.core.design.animation.NavigateAnimation
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Song

const val HomeRoute = "homeTop"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(HomeRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(
    topMargin: Dp,
    navigateToQueue: () -> Unit,
    navigateToSongDetail: (String, List<Long>) -> Unit,
    navigateToSongMenu: (Song) -> Unit,
    navigateToAlbumDetail: (Long) -> Unit,
    navigateToAlbumMenu: (Album) -> Unit,
) {
    composable(
        route = HomeRoute,
        enterTransition = {
            when (initialState.destination.route) {
                "playlistTop", "songTop", "artistTop", "albumTop" -> NavigateAnimation.Library.enter
                else -> NavigateAnimation.Vertical.popEnter
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                "playlistTop", "songTop", "artistTop", "albumTop" -> NavigateAnimation.Library.exit
                else -> NavigateAnimation.Vertical.exit
            }
        },
    ) {
        HomeRoute(
            modifier = Modifier.fillMaxSize(),
            topMargin = topMargin,
            navigateToQueue = navigateToQueue,
            navigateToSongDetail = navigateToSongDetail,
            navigateToSongMenu = navigateToSongMenu,
            navigateToAlbumDetail = navigateToAlbumDetail,
            navigateToAlbumMenu = navigateToAlbumMenu,
        )
    }
}
