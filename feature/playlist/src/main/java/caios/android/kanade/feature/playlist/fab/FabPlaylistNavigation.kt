package caios.android.kanade.feature.playlist.fab

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import caios.android.kanade.core.design.animation.NavigateAnimation
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.feature.playlist.top.PlaylistTopRoute

const val FabPlaylistRoute = "fabPlaylist"

fun NavController.navigateToFabPlaylist() {
    this.navigate(FabPlaylistRoute)
}

fun NavGraphBuilder.fabPlaylistDialog(
    navigateToCreatePlaylist: () -> Unit,
    terminate: () -> Unit,
) {
    dialog(
        route = FabPlaylistRoute,
    ) {
        FabPlaylistRoute(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            navigateToCreatePlaylist = navigateToCreatePlaylist,
            terminate = terminate,
        )
    }
}
