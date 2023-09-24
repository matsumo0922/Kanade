package caios.android.kanade.feature.welcome.plus

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import caios.android.kanade.core.design.animation.NavigateAnimation

const val WelcomePlusRoute = "welcomePlus"

fun NavController.navigateToWelcomePlus() {
    this.navigate(WelcomePlusRoute)
}

fun NavGraphBuilder.welcomePlusScreen(
    navigateToBillingPlus: () -> Unit,
    navigateToWelcomePermission: () -> Unit,
) {
    composable(
        route = WelcomePlusRoute,
        enterTransition = { NavigateAnimation.Horizontal.enter },
        exitTransition = { NavigateAnimation.Horizontal.exit },
        popEnterTransition = { NavigateAnimation.Horizontal.popEnter },
        popExitTransition = { NavigateAnimation.Horizontal.popExit },
    ) {
        WelcomePlusScreen(
            modifier = Modifier.fillMaxSize(),
            navigateToBillingPlus = navigateToBillingPlus,
            navigateToWelcomePermission = navigateToWelcomePermission,
        )
    }
}
