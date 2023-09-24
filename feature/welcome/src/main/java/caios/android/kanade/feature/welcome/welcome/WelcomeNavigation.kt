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
        enterTransition = { NavigateAnimation.Horizontal.enter },
        exitTransition = { NavigateAnimation.Horizontal.exit },
        popEnterTransition = { NavigateAnimation.Horizontal.popEnter },
        popExitTransition = { NavigateAnimation.Horizontal.popExit },
    ) {
        WelcomeScreen(
            modifier = Modifier.fillMaxSize(),
            navigateToWelcomePermission = navigateToWelcomePermission,
        )
    }
}
