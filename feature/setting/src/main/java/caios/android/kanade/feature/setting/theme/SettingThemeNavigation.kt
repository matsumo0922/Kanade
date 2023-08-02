package caios.android.kanade.feature.setting.theme

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog

const val SettingThemeDialogRoute = "SettingTheme"

fun NavController.navigateToSettingTheme() {
    this.navigate(SettingThemeDialogRoute)
}

fun NavGraphBuilder.SettingThemeDialog(
    terminate: () -> Unit,
) {
    dialog(
        route = SettingThemeDialogRoute,
    ) {

    }
}
