package caios.android.kanade.feature.playlist.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import caios.android.kanade.core.design.animation.NavigateAnimation
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.model.music.Song

const val PlaylistDetailId = "playlistDetailId"
const val PlaylistDetailRoute = "playlistDetail/{$PlaylistDetailId}"

fun NavController.navigateToPlaylistDetail(playlistId: Long) {
    this.navigate("playlistDetail/$playlistId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.playlistDetailScreen(
    navigateToPlaylistMenu: (Playlist) -> Unit,
    navigateToSongMenu: (Song) -> Unit,
    terminate: () -> Unit,
) {
    composable(
        route = PlaylistDetailRoute,
        arguments = listOf(navArgument(PlaylistDetailId) { type = NavType.LongType }),
        enterTransition = { NavigateAnimation.Vertical.enter },
        exitTransition = { NavigateAnimation.Vertical.exit },
        popEnterTransition = { NavigateAnimation.Vertical.popEnter },
        popExitTransition = { NavigateAnimation.Vertical.popExit },
    ) {
        PlaylistDetailRoute(
            modifier = Modifier.fillMaxSize(),
            playlistId = it.arguments?.getLong(PlaylistDetailId) ?: -1L,
            navigateToSongMenu = navigateToSongMenu,
            navigateToPlaylistMenu = navigateToPlaylistMenu,
            terminate = terminate,
        )
    }
}
