package caios.android.kanade.feature.menu.delete

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

const val DeleteSongSongs = "deleteSongSongs"
const val DeleteSongDialogRoute = "deleteSong/{$DeleteSongSongs}"

fun NavController.navigateToDeleteSong(songIds: List<Long> = emptyList()) {
    this.navigate("deleteSong/${songIds.joinToString(",")}")
}

fun NavGraphBuilder.deleteSongDialog(
    terminate: () -> Unit,
) {
    dialog(
        route = DeleteSongDialogRoute,
        arguments = listOf(navArgument(DeleteSongSongs) { type = NavType.StringType }),
    ) {
        DeleteSongDialog(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(16.dp)),
            songIds = (it.arguments?.getString(DeleteSongSongs) ?: "").split(",").map { id -> id.toLong() }.toImmutableList(),
            terminate = terminate,
        )
    }
}
