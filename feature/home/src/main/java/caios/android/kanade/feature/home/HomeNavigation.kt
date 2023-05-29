package caios.android.kanade.feature.home

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val homeRoute = "home"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(homeRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(
    topMargin: Dp,
) {
    composable(route = homeRoute) {
        HomeRoute(
            topMargin = topMargin,
        )
    }
}
