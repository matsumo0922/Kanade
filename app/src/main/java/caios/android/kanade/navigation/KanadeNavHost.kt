package caios.android.kanade.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.navigation.compose.NavHost
import caios.android.kanade.core.common.network.util.ToastUtil
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.music.MusicViewModel
import caios.android.kanade.feature.album.detail.albumDetailScreen
import caios.android.kanade.feature.album.detail.navigateToAlbumDetail
import caios.android.kanade.feature.album.top.albumTopScreen
import caios.android.kanade.feature.artist.detail.artistDetailScreen
import caios.android.kanade.feature.artist.detail.navigateToArtistDetail
import caios.android.kanade.feature.artist.top.artistTopScreen
import caios.android.kanade.feature.home.HomeRoute
import caios.android.kanade.feature.home.homeScreen
import caios.android.kanade.feature.menu.album.showAlbumMenuDialog
import caios.android.kanade.feature.menu.artist.showArtistMenuDialog
import caios.android.kanade.feature.menu.playlist.showPlaylistMenuDialog
import caios.android.kanade.feature.menu.song.showSongMenuDialog
import caios.android.kanade.feature.playlist.add.addToPlaylistDialog
import caios.android.kanade.feature.playlist.add.navigateToAddToPlaylist
import caios.android.kanade.feature.playlist.create.createPlaylistDialog
import caios.android.kanade.feature.playlist.create.navigateToCreatePlaylist
import caios.android.kanade.feature.playlist.detail.navigateToPlaylistDetail
import caios.android.kanade.feature.playlist.detail.playlistDetailScreen
import caios.android.kanade.feature.playlist.fab.fabPlaylistDialog
import caios.android.kanade.feature.playlist.fab.navigateToFabPlaylist
import caios.android.kanade.feature.playlist.rename.navigateToRenamePlaylist
import caios.android.kanade.feature.playlist.rename.renamePlaylistDialog
import caios.android.kanade.feature.playlist.top.playlistTopScreen
import caios.android.kanade.feature.queue.showQueueDialog
import caios.android.kanade.feature.search.searchScreen
import caios.android.kanade.feature.song.detail.navigateToSongDetail
import caios.android.kanade.feature.song.detail.songDetailScreen
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

    fun showSongMenuDialog(song: Song) {
        activity.showSongMenuDialog(
            musicViewModel = musicViewModel,
            userData = userData,
            song = song,
            navigateToAddToPlaylist = {
                navController.navigateToAddToPlaylist(it)
            },
            navigateToArtistDetail = {
                navController.navigateToArtistDetail(it)
            },
            navigateToAlbumDetail = {
                navController.navigateToAlbumDetail(it)
            },
        )
    }

    fun showArtistMenuDialog(artist: Artist) {
        activity.showArtistMenuDialog(
            musicViewModel = musicViewModel,
            userData = userData,
            artist = artist,
            navigateToAddToPlaylist = {
                navController.navigateToAddToPlaylist(it)
            },
        )
    }

    fun showAlbumMenuDialog(album: Album) {
        activity.showAlbumMenuDialog(
            musicViewModel = musicViewModel,
            userData = userData,
            album = album,
            navigateToAddToPlaylist = {
                navController.navigateToAddToPlaylist(it)
            },
        )
    }

    fun showPlaylistMenuDialog(playlist: Playlist) {
        activity.showPlaylistMenuDialog(
            musicViewModel = musicViewModel,
            userData = userData,
            playlist = playlist,
            navigateToRename = {
                if (it.isSystemPlaylist) {
                    ToastUtil.show(activity, R.string.playlist_error_rename_system_playlist)
                } else {
                    navController.navigateToRenamePlaylist(it.id)
                }
            },
        )
    }

    fun showQueueDialog() {
        activity.showQueueDialog(
            userData = userData,
            navigateToSongMenu = ::showSongMenuDialog,
            navigateToAddToPlaylist = {
                appState.navController.navigateToAddToPlaylist(it)
            },
        )
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        searchScreen(
            navigateToArtistDetail = {
                navController.navigateToArtistDetail(it)
            },
            navigateToAlbumDetail = {
                navController.navigateToAlbumDetail(it)
            },
            navigateToPlaylistDetail = {
                navController.navigateToPlaylistDetail(it)
            },
            navigateToSongMenu = ::showSongMenuDialog,
            navigateToArtistMenu = ::showArtistMenuDialog,
            navigateToAlbumMenu = ::showAlbumMenuDialog,
            navigateToPlaylistMenu = ::showPlaylistMenuDialog,
            terminate = {
                navController.popBackStack()
            },
        )

        homeScreen(
            topMargin = libraryTopBarHeight,
            navigateToQueue = ::showQueueDialog,
            navigateToSongMenu = ::showSongMenuDialog,
            navigateToAlbumMenu = ::showAlbumMenuDialog,
            navigateToSongDetail = { title, songIds ->
                navController.navigateToSongDetail(title, songIds)
            },
            navigateToAlbumDetail = {
                navController.navigateToAlbumDetail(it)
            },
        )

        playlistTopScreen(
            topMargin = libraryTopBarHeight,
            navigateToPlaylistMenu = ::showPlaylistMenuDialog,
            navigateToPlaylistEdit = {
                navController.navigateToFabPlaylist()
            },
            navigateToPlaylistDetail = {
                navController.navigateToPlaylistDetail(it)
            },
        )

        songTopScreen(
            topMargin = libraryTopBarHeight,
            navigateToSongMenu = ::showSongMenuDialog,
        )

        artistTopScreen(
            topMargin = libraryTopBarHeight,
            navigateToArtistDetail = {
                navController.navigateToArtistDetail(it)
            },
        )

        albumTopScreen(
            topMargin = libraryTopBarHeight,
            navigateToAlbumDetail = {
                navController.navigateToAlbumDetail(it)
            },
            navigateToAlbumMenu = ::showAlbumMenuDialog,
        )

        songDetailScreen(
            navigateToSongMenu = ::showSongMenuDialog,
            terminate = {
                navController.popBackStack()
            },
        )

        artistDetailScreen(
            navigateToSongDetail = { title, songIds ->
                navController.navigateToSongDetail(title, songIds)
            },
            navigateToAlbumDetail = {
                navController.navigateToAlbumDetail(it)
            },
            navigateToSongMenu = ::showSongMenuDialog,
            navigateToArtistMenu = ::showArtistMenuDialog,
            navigateToAlbumMenu = ::showAlbumMenuDialog,
            terminate = {
                navController.popBackStack()
            },
        )

        albumDetailScreen(
            navigateToSongMenu = ::showSongMenuDialog,
            navigateToAlbumMenu = ::showAlbumMenuDialog,
            terminate = {
                navController.popBackStack()
            },
        )

        playlistDetailScreen(
            navigateToSongMenu = ::showSongMenuDialog,
            navigateToPlaylistMenu = ::showPlaylistMenuDialog,
            terminate = {
                navController.popBackStack()
            },
        )

        fabPlaylistDialog(
            navigateToCreatePlaylist = {
                navController.navigateToCreatePlaylist(emptyList())
            },
            terminate = {
                navController.popBackStack()
            },
        )

        renamePlaylistDialog(
            terminate = {
                navController.popBackStack()
            },
        )

        createPlaylistDialog(
            terminate = {
                navController.popBackStack()
            },
        )

        addToPlaylistDialog(
            navigateToCreatePlaylist = { songIds ->
                navController.navigateToCreatePlaylist(songIds)
            },
            terminate = {
                navController.popBackStack()
            },
        )
    }
}
