package caios.android.kanade.feature.playlist.rename

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument

const val RenamePlaylistId = "renamePlaylistSongs"
const val RenamePlaylistDialogRoute = "renamePlaylist/{$RenamePlaylistId}"

fun NavController.navigateToRenamePlaylist(playlistId: Long) {
    this.navigate("renamePlaylist/$playlistId")
}

fun NavGraphBuilder.renamePlaylistDialog(
    terminate: () -> Unit,
) {
    dialog(
        route = RenamePlaylistDialogRoute,
        arguments = listOf(navArgument(RenamePlaylistId) { type = NavType.LongType },),
    ) {
        RenamePlaylistDialog(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(16.dp)),
            playlistId = it.arguments?.getLong(RenamePlaylistId) ?: 0L,
            terminate = terminate,
        )
    }
}
