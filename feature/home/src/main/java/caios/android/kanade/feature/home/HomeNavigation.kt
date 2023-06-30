package caios.android.kanade.feature.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import caios.android.kanade.core.design.animation.NavigateAnimation

const val HomeRoute = "homeTop"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(HomeRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(
    topMargin: Dp,
) {
    composable(
        route = HomeRoute,
        enterTransition = {
            when (initialState.destination.route) {
                "playlistTop", "songTop", "artistTop", "albumTop" -> NavigateAnimation.Library.enter
                else -> NavigateAnimation.Detail.popEnter
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                "playlistTop", "songTop", "artistTop", "albumTop" -> NavigateAnimation.Library.exit
                else -> NavigateAnimation.Detail.exit
            }
        },
    ) {
        HomeRoute(
            modifier = Modifier.fillMaxSize(),
            topMargin = topMargin,
        )
    }
}
