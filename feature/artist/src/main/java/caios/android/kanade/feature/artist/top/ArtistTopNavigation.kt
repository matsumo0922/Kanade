package caios.android.kanade.feature.artist.top

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import caios.android.kanade.core.design.animation.NavigateAnimation

const val ArtistTopRoute = "artistTop"

fun NavController.navigateToArtistTop(navOptions: NavOptions? = null) {
    this.navigate(ArtistTopRoute, navOptions)
}

fun NavGraphBuilder.artistTopScreen(
    topMargin: Dp,
    navigateToArtistDetail: (Long) -> Unit,
) {
    composable(
        route = ArtistTopRoute,
        enterTransition = {
            when (initialState.destination.route) {
                "homeTop", "playlistTop", "songTop", "albumTop" -> NavigateAnimation.Library.enter
                else -> NavigateAnimation.Detail.popEnter
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                "homeTop", "playlistTop", "songTop", "albumTop" -> NavigateAnimation.Library.exit
                else -> NavigateAnimation.Detail.popExit
            }
        }
    ) {
        ArtistTopRoute(
            topMargin = topMargin,
            navigateToArtistDetail = navigateToArtistDetail,
        )
    }
}
