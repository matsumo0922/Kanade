package caios.android.kanade.feature.artist.top

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import caios.android.kanade.core.design.animation.NavigateAnimation
import kotlin.reflect.KClass

const val ArtistTopRoute = "artistTop"

fun NavController.navigateToArtistTop(navOptions: NavOptions? = null) {
    this.navigate(ArtistTopRoute, navOptions)
}

fun NavGraphBuilder.artistTopScreen(
    topMargin: Dp,
    navigateToArtistDetail: (Long) -> Unit,
    navigateToSort: (KClass<*>) -> Unit,
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
                else -> NavigateAnimation.Detail.exit
            }
        },
    ) {
        ArtistTopRoute(
            modifier = Modifier.fillMaxSize(),
            topMargin = topMargin,
            navigateToArtistDetail = navigateToArtistDetail,
            navigateToSort = navigateToSort,
        )
    }
}
