package caios.android.kanade.feature.playlist.create

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import kotlinx.collections.immutable.toImmutableList

const val CreatePlaylistSongs = "createPlaylistSongs"
const val CreatePlaylistDialogRoute = "createPlaylist/{$CreatePlaylistSongs}"

fun NavController.navigateToCreatePlaylist(songIds: List<Long> = emptyList()) {
    this.navigate("createPlaylist/${songIds.joinToString(",")},")
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
                .wrapContentHeight(),
            songIds = (it.arguments?.getString(CreatePlaylistSongs) ?: "")
                .split(",")
                .mapNotNull { id -> id.toLongOrNull() }
                .toImmutableList(),
            onTerminate = terminate,
        )
    }
}
