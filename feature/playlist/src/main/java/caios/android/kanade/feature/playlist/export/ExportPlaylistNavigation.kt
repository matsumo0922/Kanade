package caios.android.kanade.feature.playlist.export

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument

const val ExportPlaylistId = "exportPlaylistId"
const val ExportPlaylistDialogRoute = "exportPlaylist/{$ExportPlaylistId}"

fun NavController.navigateToExportPlaylist(playlistId: Long) {
    this.navigate("exportPlaylist/$playlistId")
}

fun NavGraphBuilder.exportPlaylistDialog(
    terminate: () -> Unit,
) {
    dialog(
        route = ExportPlaylistDialogRoute,
        arguments = listOf(
            navArgument(ExportPlaylistId) { type = NavType.LongType },
        ),
    ) {
        ExportPlaylistRoute(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            playlistId = it.arguments?.getLong(ExportPlaylistId) ?: -1L,
            terminate = terminate,
        )
    }
}
