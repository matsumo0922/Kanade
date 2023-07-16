package caios.android.kanade.feature.lyrics.download

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

const val LyricsDownloadId = "lyricsDownloadSongs"
const val LyricsDownloadDialogRoute = "lyricsDownload/{$LyricsDownloadId}"

fun NavController.navigateToLyricsDownload(songId: Long) {
    this.navigate("lyricsDownload/$songId")
}

fun NavGraphBuilder.lyricsDownloadDialog(
    terminate: () -> Unit,
) {
    dialog(
        route = LyricsDownloadDialogRoute,
        arguments = listOf(navArgument(LyricsDownloadId) { type = NavType.LongType }),
    ) {
        LyricsDownloadRoute(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(16.dp)),
            songId = it.arguments?.getLong(LyricsDownloadId) ?: 0L,
            terminate = terminate,
        )
    }
}
