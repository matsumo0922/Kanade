package caios.android.kanade.feature.equalizer

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import caios.android.kanade.core.design.animation.NavigateAnimation

const val EqualizerRoute = "equalizer"

fun NavController.navigateToEqualizer() {
    this.navigate(EqualizerRoute)
}

fun NavGraphBuilder.equalizerScreen(
    terminate: () -> Unit,
) {
    composable(
        route = EqualizerRoute,
        enterTransition = { NavigateAnimation.Vertical.enter },
        exitTransition = { NavigateAnimation.Vertical.exit },
        popEnterTransition = { NavigateAnimation.Vertical.popEnter },
        popExitTransition = { NavigateAnimation.Vertical.popExit },
    ) {

    }
}
