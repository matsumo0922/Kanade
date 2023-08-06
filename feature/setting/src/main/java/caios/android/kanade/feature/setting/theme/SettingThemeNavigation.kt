package caios.android.kanade.feature.setting.theme

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import caios.android.kanade.core.design.animation.NavigateAnimation

const val SettingThemeDialogRoute = "SettingTheme"

fun NavController.navigateToSettingTheme() {
    this.navigate(SettingThemeDialogRoute)
}

fun NavGraphBuilder.settingThemeScreen(
    terminate: () -> Unit,
) {
    composable(
        route = SettingThemeDialogRoute,
        enterTransition = { NavigateAnimation.Horizontal.enter },
        exitTransition = { NavigateAnimation.Horizontal.exit },
        popEnterTransition = { NavigateAnimation.Horizontal.popEnter },
        popExitTransition = { NavigateAnimation.Horizontal.popExit },
    ) {
        SettingThemeRoute(
            modifier = Modifier.fillMaxSize(),
            terminate = terminate,
        )
    }
}
