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

const val SongDetailTitle = "songDetailTitle"
const val SongDetailIds = "songDetailIds"
const val SongDetailRoute = "songDetail/{$SongDetailTitle}/{$SongDetailIds}"

fun NavController.navigateToSongDetail(title: String, songIds: List<Long>) {
    this.navigate("songDetail/$title/${songIds.joinToString(",")}")
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
        enterTransition = { NavigateAnimation.Detail.enter },
        exitTransition = { NavigateAnimation.Detail.exit },
        popEnterTransition = { NavigateAnimation.Detail.popEnter },
        popExitTransition = { NavigateAnimation.Detail.popExit },
    ) {
        SongDetailRoute(
            modifier = Modifier.fillMaxSize(),
            title = it.arguments?.getString(SongDetailTitle) ?: "Songs",
            songIds = (it.arguments?.getString(SongDetailIds) ?: "").split(",").map { id -> id.toLong() },
            navigateToSongMenu = navigateToSongMenu,
            navigateToAddToPlaylist = { },
            terminate = terminate,
        )
    }
}
