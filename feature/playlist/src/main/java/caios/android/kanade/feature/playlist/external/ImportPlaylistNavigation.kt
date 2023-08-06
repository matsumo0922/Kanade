package caios.android.kanade.feature.playlist.external

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog

const val ImportPlaylistRoute = "importPlaylist"

fun NavController.navigateToImportPlaylist() {
    this.navigate(ImportPlaylistRoute)
}

fun NavGraphBuilder.importPlaylistDialog(
    terminate: () -> Unit,
) {
    dialog(
        route = ImportPlaylistRoute,
    ) {
        ImportPlaylistRoute(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            terminate = terminate,
        )
    }
}
