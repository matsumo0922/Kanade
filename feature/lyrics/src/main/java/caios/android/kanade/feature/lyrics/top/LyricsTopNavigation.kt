package caios.android.kanade.feature.lyrics.top

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import caios.android.kanade.core.design.animation.NavigateAnimation

const val LyricsTopId = "lyricsTopId"
const val LyricsTopRoute = "lyricsTop/{$LyricsTopId}"

fun NavController.navigateToLyricsTop(songId: Long) {
    this.navigate("lyricsTop/$songId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.lyricsTopScreen(
    navigateToLyricsDownload: (Long) -> Unit,
    terminate: () -> Unit,
) {
    composable(
        route = LyricsTopRoute,
        arguments = listOf(navArgument(LyricsTopId) { type = NavType.LongType }),
        enterTransition = { NavigateAnimation.Vertical.enter },
        exitTransition = { NavigateAnimation.Vertical.exit },
        popEnterTransition = { NavigateAnimation.Vertical.popEnter },
        popExitTransition = { NavigateAnimation.Vertical.popExit },
    ) {
        LyricsTopRoute(
            modifier = Modifier.fillMaxSize(),
            songId = it.arguments!!.getLong(LyricsTopId),
            navigateToLyricsDownload = navigateToLyricsDownload,
            terminate = terminate,
        )
    }
}
