package caios.android.kanade.feature.playlist.add

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

const val AddToPlaylistSongs = "addToPlaylistSongs"
const val AddToPlaylistDialogRoute = "addToPlaylist/{$AddToPlaylistSongs}"

fun NavController.navigateToAddToPlaylist(songIds: List<Long> = emptyList()) {
    this.navigate("addToPlaylist/${songIds.joinToString(",")}")
}

fun NavGraphBuilder.addToPlaylistDialog(
    navigateToCreatePlaylist: (List<Long>) -> Unit,
    terminate: () -> Unit,
) {
    dialog(
        route = AddToPlaylistDialogRoute,
        arguments = listOf(
            navArgument(AddToPlaylistSongs) { type = NavType.StringType },
        ),
    ) {
        AddToPlaylistDialog(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(16.dp)),
            songIds = (it.arguments?.getString(AddToPlaylistSongs) ?: "").split(",").map { id -> id.toLong() }.toImmutableList(),
            navigateToCreatePlaylist = navigateToCreatePlaylist,
            onTerminate = terminate,
        )
    }
}
