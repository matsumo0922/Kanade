package caios.android.kanade.feature.album.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import caios.android.kanade.core.design.animation.NavigateAnimation
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Song

const val AlbumDetailId = "albumDetailId"
const val AlbumDetailRoute = "albumDetail/{$AlbumDetailId}"

fun NavController.navigateToAlbumDetail(albumId: Long) {
    this.navigate("albumDetail/$albumId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.albumDetailScreen(
    navigateToAlbumMenu: (Album) -> Unit,
    navigateToSongMenu: (Song) -> Unit,
    terminate: () -> Unit,
) {
    composable(
        route = AlbumDetailRoute,
        arguments = listOf(
            navArgument(AlbumDetailId) { type = NavType.LongType },
        ),
        enterTransition = { NavigateAnimation.Detail.enter },
        exitTransition = { NavigateAnimation.Detail.exit },
        popEnterTransition = { NavigateAnimation.Detail.popEnter },
        popExitTransition = { NavigateAnimation.Detail.popExit },
    ) {
        AlbumDetailRoute(
            modifier = Modifier.fillMaxSize(),
            albumId = it.arguments?.getLong(AlbumDetailId) ?: -1L,
            navigateToSongMenu = navigateToSongMenu,
            navigateToAlbumMenu = navigateToAlbumMenu,
            terminate = terminate,
        )
    }
}
