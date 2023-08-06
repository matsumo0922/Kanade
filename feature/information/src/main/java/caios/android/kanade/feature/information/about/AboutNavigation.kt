package caios.android.kanade.feature.information.about

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import caios.android.kanade.core.design.animation.NavigateAnimation
import caios.android.kanade.core.model.Version
import kotlinx.collections.immutable.ImmutableList

const val AboutRoute = "about"

fun NavController.navigateToAbout() {
    this.navigate(AboutRoute)
}

fun NavGraphBuilder.aboutScreen(
    navigateToVersionHistory: (ImmutableList<Version>) -> Unit,
    navigateToDonate: () -> Unit,
    terminate: () -> Unit,
) {
    composable(
        route = AboutRoute,
        enterTransition = { NavigateAnimation.Vertical.enter },
        exitTransition = { NavigateAnimation.Vertical.exit },
        popEnterTransition = { NavigateAnimation.Vertical.popEnter },
        popExitTransition = { NavigateAnimation.Vertical.popExit },
    ) {
        AboutRoute(
            modifier = Modifier.fillMaxSize(),
            navigateToVersionHistory = navigateToVersionHistory,
            navigateToDonate = navigateToDonate,
            terminate = terminate,
        )
    }
}
