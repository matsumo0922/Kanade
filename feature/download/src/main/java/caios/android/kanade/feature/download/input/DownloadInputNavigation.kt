package caios.android.kanade.feature.download.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
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
        ),
    ) {
        DownloadInputDialog(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(16.dp)),
            terminate = terminate,
        )
    }
}
