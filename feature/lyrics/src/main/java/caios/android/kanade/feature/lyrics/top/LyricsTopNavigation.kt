package caios.android.kanade.feature.lyrics.top

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import caios.android.kanade.core.design.animation.NavigateAnimation
import caios.android.kanade.core.model.music.Song

const val LyricsTopId = "lyricsTopId"
const val LyricsTopRoute = "lyricsTop/{$LyricsTopId}"

fun NavController.navigateToLyricsTop(songId: Long) {
    this.navigate("lyricsTop/$songId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.lyricsTopScreen(
    navigateToSongMenu: (Song) -> Unit,
    terminate: () -> Unit,
) {
    composable(
        route = LyricsTopRoute,
        arguments = listOf(navArgument(LyricsTopId) { type = NavType.LongType }),
        enterTransition = { NavigateAnimation.Detail.enter },
        exitTransition = { NavigateAnimation.Detail.exit },
        popEnterTransition = { NavigateAnimation.Detail.popEnter },
        popExitTransition = { NavigateAnimation.Detail.popExit },
    ) {

    }
}
