package caios.android.kanade.feature.home

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

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
            fadeIn(tween(240)) + scaleIn(
                initialScale = 0.92f,
                transformOrigin = TransformOrigin.Center,
                animationSpec = tween(240, 0, CubicBezierEasing(0.0f, 0.0f, 0.0f, 1.0f)),
            )
        },
    ) {
        HomeRoute(
            modifier = Modifier.fillMaxSize(),
            topMargin = topMargin,
        )
    }
}
