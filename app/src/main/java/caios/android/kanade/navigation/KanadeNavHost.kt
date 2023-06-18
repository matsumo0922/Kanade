package caios.android.kanade.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.navigation.compose.NavHost
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.music.MusicViewModel
import caios.android.kanade.feature.album.top.albumTopScreen
import caios.android.kanade.feature.artist.top.artistTopScreen
import caios.android.kanade.feature.home.HomeRoute
import caios.android.kanade.feature.home.homeScreen
import caios.android.kanade.feature.menu.song.showSongMenuDialog
import caios.android.kanade.feature.playlist.top.playlistTopScreen
import caios.android.kanade.feature.song.top.songTopScreen
import caios.android.kanade.ui.KanadeAppState

@Composable
fun KanadeNavHost(
    musicViewModel: MusicViewModel,
    appState: KanadeAppState,
    userData: UserData?,
    libraryTopBarHeight: Dp,
    modifier: Modifier = Modifier,
    startDestination: String = HomeRoute,
) {
    val navController = appState.navController
    val activity = (LocalContext.current as Activity)

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        homeScreen(
            topMargin = libraryTopBarHeight,
        )

        playlistTopScreen(
            topMargin = libraryTopBarHeight,
        )

        songTopScreen(
            topMargin = libraryTopBarHeight,
            navigateToSongMenu = { activity.showSongMenuDialog(userData, it) },
        )

        artistTopScreen(
            topMargin = libraryTopBarHeight,
        )

        albumTopScreen(
            topMargin = libraryTopBarHeight,
        )
    }
}
