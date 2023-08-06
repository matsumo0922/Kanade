package caios.android.kanade.feature.information.song

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument

const val SongInformationId = "songInformationSongId"
const val SongInformationRoute = "songInformation/{$SongInformationId}"

fun NavController.navigateToSongInformation(songId: Long) {
    this.navigate("songInformation/$songId")
}

fun NavGraphBuilder.songInformationDialog(
    terminate: () -> Unit,
) {
    dialog(
        route = SongInformationRoute,
        arguments = listOf(
            navArgument(SongInformationId) { type = NavType.LongType },
        ),
    ) {
        SongInformationRoute(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            songId = it.arguments?.getLong(SongInformationId) ?: -1L,
            terminate = terminate,
        )
    }
}
