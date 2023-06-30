package caios.android.kanade.feature.song.top

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import caios.android.kanade.core.design.animation.NavigateAnimation
import caios.android.kanade.core.model.music.Song

const val SongTopRoute = "songTop"

fun NavController.navigateToSongTop(navOptions: NavOptions? = null) {
    this.navigate(SongTopRoute, navOptions)
}

fun NavGraphBuilder.songTopScreen(
    topMargin: Dp,
    navigateToSongMenu: (Song) -> Unit,
) {
    composable(
        route = SongTopRoute,
        enterTransition = {
            when (initialState.destination.route) {
                "homeTop", "playlistTop", "artistTop", "albumTop" -> NavigateAnimation.Library.enter
                else -> NavigateAnimation.Detail.popEnter
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                "homeTop", "playlistTop", "artistTop", "albumTop" -> NavigateAnimation.Library.exit
                else -> NavigateAnimation.Detail.popExit
            }
        },
    ) {
        SongTopRoute(
            topMargin = topMargin,
            navigateToSongMenu = navigateToSongMenu,
        )
    }
}
