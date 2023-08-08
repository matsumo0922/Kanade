package caios.android.kanade.feature.download

import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog

const val DownloadInputRoute = "downloadInput"

fun NavController.navigateToDownloadInput() {
    this.navigate(DownloadInputRoute)
}

fun NavGraphBuilder.downloadInputDialog(
    terminate: () -> Unit,
) {
    dialog(
        route = DownloadInputRoute,
        dialogProperties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        )
    ) {

    }
}
