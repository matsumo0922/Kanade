package caios.android.kanade.feature.setting.top

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import caios.android.kanade.core.design.animation.NavigateAnimation

const val SettingTopRoute = "settingTop"

fun NavController.navigateToSettingTop() {
    this.navigate(SettingTopRoute)
}

fun NavGraphBuilder.settingTopScreen(
    navigateToSettingTheme: () -> Unit,
    terminate: () -> Unit,
) {
    composable(
        route = SettingTopRoute,
        enterTransition = { NavigateAnimation.Vertical.enter },
        exitTransition = { NavigateAnimation.Horizontal.exit },
        popEnterTransition = { NavigateAnimation.Horizontal.popEnter },
        popExitTransition = { NavigateAnimation.Vertical.popExit },
    ) {
        SettingTopRoute(
            modifier = Modifier.fillMaxSize(),
            navigateToSettingTheme = navigateToSettingTheme,
            terminate = terminate,
        )
    }
}
