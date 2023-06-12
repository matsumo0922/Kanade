package caios.android.kanade.feature.home

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val HomeRoute = "home"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(HomeRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(
    topMargin: Dp,
) {
    composable(route = HomeRoute) {
        HomeRoute(
            topMargin = topMargin,
        )
    }
}
