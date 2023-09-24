package caios.android.kanade.feature.welcome.welcome

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import caios.android.kanade.core.design.animation.NavigateAnimation

const val WelcomeRoute = "welcome"

fun NavController.navigateToWelcome() {
    this.navigate(WelcomeRoute)
}

fun NavGraphBuilder.welcomeScreen(
    navigateToWelcomePermission: () -> Unit,
) {
    composable(
        route = WelcomeRoute,
        enterTransition = { NavigateAnimation.Vertical.enter },
        exitTransition = { NavigateAnimation.Vertical.exit },
        popEnterTransition = { NavigateAnimation.Vertical.popEnter },
        popExitTransition = { NavigateAnimation.Vertical.popExit },
    ) {
        WelcomeScreen(
            modifier = Modifier.fillMaxSize(),
            navigateToWelcomePermission = navigateToWelcomePermission,
        )
    }
}
