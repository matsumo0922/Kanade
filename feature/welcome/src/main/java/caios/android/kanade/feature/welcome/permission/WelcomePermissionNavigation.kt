package caios.android.kanade.feature.welcome.permission

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import caios.android.kanade.core.design.animation.NavigateAnimation

const val WelcomePermissionRoute = "welcomePermission"

fun NavController.navigateToWelcomePermission() {
    this.navigate(WelcomePermissionRoute)
}

fun NavGraphBuilder.welcomePermissionScreen(
    terminate: () -> Unit,
) {
    composable(
        route = WelcomePermissionRoute,
        enterTransition = { NavigateAnimation.Vertical.enter },
        exitTransition = { NavigateAnimation.Vertical.exit },
        popEnterTransition = { NavigateAnimation.Vertical.popEnter },
        popExitTransition = { NavigateAnimation.Vertical.popExit },
    ) {

    }
}
