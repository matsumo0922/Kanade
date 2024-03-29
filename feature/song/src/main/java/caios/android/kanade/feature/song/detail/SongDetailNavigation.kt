package caios.android.kanade.feature.song.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import caios.android.kanade.core.design.animation.NavigateAnimation
import caios.android.kanade.core.model.music.Song
import kotlinx.collections.immutable.toImmutableList

const val SongDetailTitle = "songDetailTitle"
const val SongDetailIds = "songDetailIds"
const val SongDetailRoute = "songDetail/{$SongDetailTitle}/{$SongDetailIds}"

fun NavController.navigateToSongDetail(title: String, songIds: List<Long>) {
    this.navigate("songDetail/$title/${songIds.joinToString(",")},") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.songDetailScreen(
    navigateToSongMenu: (Song) -> Unit,
    terminate: () -> Unit,
) {
    composable(
        route = SongDetailRoute,
        arguments = listOf(
            navArgument(SongDetailTitle) { type = NavType.StringType },
            navArgument(SongDetailIds) { type = NavType.StringType },
        ),
        enterTransition = { NavigateAnimation.Vertical.enter },
        exitTransition = { NavigateAnimation.Vertical.exit },
        popEnterTransition = { NavigateAnimation.Vertical.popEnter },
        popExitTransition = { NavigateAnimation.Vertical.popExit },
    ) {
        SongDetailRoute(
            modifier = Modifier.fillMaxSize(),
            title = it.arguments?.getString(SongDetailTitle) ?: "Songs",
            songIds = (it.arguments?.getString(SongDetailIds) ?: "")
                .split(",")
                .mapNotNull { id -> id.toLongOrNull() }
                .toImmutableList(),
            navigateToSongMenu = navigateToSongMenu,
            navigateToAddToPlaylist = { },
            terminate = terminate,
        )
    }
}
