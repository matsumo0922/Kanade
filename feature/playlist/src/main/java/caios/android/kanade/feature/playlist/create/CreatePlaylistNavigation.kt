package caios.android.kanade.feature.playlist.create

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
import kotlinx.collections.immutable.toImmutableList

const val CreatePlaylistSongs = "createPlaylistSongs"
const val CreatePlaylistDialogRoute = "createPlaylist/{$CreatePlaylistSongs}"

fun NavController.navigateToCreatePlaylist(songIds: List<Long> = emptyList()) {
    this.navigate("createPlaylist/${songIds.joinToString(",")}")
}

fun NavGraphBuilder.createPlaylistDialog(
    terminate: () -> Unit,
) {
    dialog(
        route = CreatePlaylistDialogRoute,
        arguments = listOf(
            navArgument(CreatePlaylistSongs) { type = NavType.StringType },
        ),
    ) {
        CreatePlaylistDialog(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(16.dp)),
            songIds = (it.arguments?.getString(CreatePlaylistSongs) ?: "").split(",").map { id -> id.toLong() }.toImmutableList(),
            onTerminate = terminate,
        )
    }
}
