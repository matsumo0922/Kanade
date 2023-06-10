package caios.android.kanade.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.compose.NavHost
import caios.android.kanade.core.music.MusicViewModel
import caios.android.kanade.feature.album.top.albumTopScreen
import caios.android.kanade.feature.artist.top.artistTopScreen
import caios.android.kanade.feature.home.homeRoute
import caios.android.kanade.feature.home.homeScreen
import caios.android.kanade.feature.playlist.top.playlistTopScreen
import caios.android.kanade.feature.song.top.songTopScreen
import caios.android.kanade.ui.KanadeAppState

@Composable
fun KanadeNavHost(
    musicViewModel: MusicViewModel,
    appState: KanadeAppState,
    libraryTopBarHeight: Dp,
    modifier: Modifier = Modifier,
    startDestination: String = homeRoute,
) {
    val navController = appState.navController

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
            onClickSong = { index, songs -> musicViewModel.onPlayWithNewQueue(index, songs, true) }
        )

        artistTopScreen(
            topMargin = libraryTopBarHeight,
        )

        albumTopScreen(
            topMargin = libraryTopBarHeight,
        )
    }
}
