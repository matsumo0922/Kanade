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
    terminate: () -> Unit,
) {
    composable(
        route = SettingTopRoute,
        enterTransition = { NavigateAnimation.Detail.enter },
        exitTransition = { NavigateAnimation.Detail.exit },
        popEnterTransition = { NavigateAnimation.Detail.popEnter },
        popExitTransition = { NavigateAnimation.Detail.popExit },
    ) {
        SettingTopScreen(
            modifier = Modifier.fillMaxSize(),
            terminate = terminate,
        )
    }
}
