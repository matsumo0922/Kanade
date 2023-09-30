package caios.android.kanade.feature.setting.oss

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import caios.android.kanade.core.design.animation.NavigateAnimation

const val SettingLicenseDialogRoute = "SettingLicense"

fun NavController.navigateToSettingLicense() {
    this.navigate(SettingLicenseDialogRoute)
}

fun NavGraphBuilder.settingLicenseScreen(
    terminate: () -> Unit,
) {
    composable(
        route = SettingLicenseDialogRoute,
        enterTransition = { NavigateAnimation.Horizontal.enter },
        exitTransition = { NavigateAnimation.Horizontal.exit },
        popEnterTransition = { NavigateAnimation.Horizontal.popEnter },
        popExitTransition = { NavigateAnimation.Horizontal.popExit },
    ) {
        SettingLicenseScreen(
            modifier = Modifier.fillMaxSize(),
            terminate = terminate,
        )
    }
}
