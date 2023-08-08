package caios.android.kanade.feature.search.scan

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import java.net.URLDecoder
import java.net.URLEncoder

const val ScanMediaUri = "scanMediaUri"
const val ScanMediaDialogRoute = "scanMedia/{$ScanMediaUri}"

fun NavController.navigateToScanMedia(uri: Uri) {
    this.navigate("scanMedia/${URLEncoder.encode(uri.toString(), "UTF-8")}")
}

fun NavGraphBuilder.scanMediaDialog(
    terminate: () -> Unit,
) {
    dialog(
        route = ScanMediaDialogRoute,
        arguments = listOf(
            navArgument(ScanMediaUri) { type = NavType.StringType },
        ),
        dialogProperties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        ),
    ) {
        ScanMediaDialog(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(16.dp)),
            uri = URLDecoder.decode(it.arguments?.getString(ScanMediaUri), "UTF-8").toUri(),
            terminate = terminate,
        )
    }
}
