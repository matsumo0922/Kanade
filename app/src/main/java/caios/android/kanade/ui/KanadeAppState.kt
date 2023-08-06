package caios.android.kanade.ui

import android.app.Activity
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import caios.android.kanade.core.common.network.util.ToastUtil
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.music.MusicViewModel
import caios.android.kanade.feature.album.detail.navigateToAlbumDetail
import caios.android.kanade.feature.album.top.AlbumTopRoute
import caios.android.kanade.feature.album.top.navigateToAlbumTop
import caios.android.kanade.feature.artist.detail.navigateToArtistDetail
import caios.android.kanade.feature.artist.top.ArtistTopRoute
import caios.android.kanade.feature.artist.top.navigateToArtistTop
import caios.android.kanade.feature.home.HomeRoute
import caios.android.kanade.feature.home.navigateToHome
import caios.android.kanade.feature.lyrics.top.navigateToLyricsTop
import caios.android.kanade.feature.menu.album.showAlbumMenuDialog
import caios.android.kanade.feature.menu.artist.showArtistMenuDialog
import caios.android.kanade.feature.menu.playlist.showPlaylistMenuDialog
import caios.android.kanade.feature.menu.song.showSongMenuDialog
import caios.android.kanade.feature.playlist.add.navigateToAddToPlaylist
import caios.android.kanade.feature.playlist.export.navigateToExportPlaylist
import caios.android.kanade.feature.playlist.rename.navigateToRenamePlaylist
import caios.android.kanade.feature.playlist.top.PlaylistTopRoute
import caios.android.kanade.feature.playlist.top.navigateToPlaylistTop
import caios.android.kanade.feature.queue.showQueueDialog
import caios.android.kanade.feature.share.ShareUtil
import caios.android.kanade.feature.song.top.SongTopRoute
import caios.android.kanade.feature.song.top.navigateToSongTop
import caios.android.kanade.navigation.LibraryDestination
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberKanadeAppState(
    windowSize: WindowSizeClass,
    musicViewModel: MusicViewModel,
    userData: UserData?,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): KanadeAppState {
    return remember(
        navController,
        musicViewModel,
        userData,
        coroutineScope,
        windowSize,
    ) {
        KanadeAppState(
            navController,
            musicViewModel,
            userData,
            coroutineScope,
            windowSize,
        )
    }
}

@Stable
class KanadeAppState(
    val navController: NavHostController,
    val musicViewModel: MusicViewModel,
    val userData: UserData?,
    val coroutineScope: CoroutineScope,
    val windowSize: WindowSizeClass,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentLibraryDestination: LibraryDestination?
        @Composable get() = when (currentDestination?.route) {
            HomeRoute -> LibraryDestination.Home
            PlaylistTopRoute -> LibraryDestination.Playlist
            SongTopRoute -> LibraryDestination.Song
            ArtistTopRoute -> LibraryDestination.Artist
            AlbumTopRoute -> LibraryDestination.Album
            else -> null
        }

    val libraryDestinations = LibraryDestination.values().asList()

    fun navigateToLibrary(libraryDestination: LibraryDestination) {
        val navOption = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (libraryDestination) {
            LibraryDestination.Home -> navController.navigateToHome(navOption)
            LibraryDestination.Song -> navController.navigateToSongTop(navOption)
            LibraryDestination.Artist -> navController.navigateToArtistTop(navOption)
            LibraryDestination.Album -> navController.navigateToAlbumTop(navOption)
            LibraryDestination.Playlist -> navController.navigateToPlaylistTop(navOption)
        }
    }

    fun navigateToQueue(activity: Activity) {
        activity.showQueueDialog(
            userData = userData,
            navigateToSongMenu = { song ->
                activity.showSongMenuDialog(
                    musicViewModel = musicViewModel,
                    userData = userData,
                    song = song,
                    navigateToAddToPlaylist = { songIds ->
                        navController.navigateToAddToPlaylist(songIds)
                    },
                    navigateToArtistDetail = { artistId ->
                        navController.navigateToArtistDetail(artistId)
                    },
                    navigateToAlbumDetail = { albumId ->
                        navController.navigateToAlbumDetail(albumId)
                    },
                    navigateToLyricsTop = { songId ->
                        navController.navigateToLyricsTop(songId)
                    },
                    navigateToShare = {
                        ShareUtil.showShareDialog(activity, listOf(it))
                    },
                )
            },
            navigateToAddToPlaylist = {
                navController.navigateToAddToPlaylist(it)
            },
            navigateToShare = {
                ShareUtil.showShareDialog(activity, it)
            },
        )
    }

    fun showSongMenuDialog(activity: Activity, song: Song) {
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
            navigateToLyricsTop = {
                navController.navigateToLyricsTop(it)
            },
            navigateToShare = {
                ShareUtil.showShareDialog(activity, listOf(it))
            },
        )
    }

    fun showArtistMenuDialog(activity: Activity, artist: Artist) {
        activity.showArtistMenuDialog(
            musicViewModel = musicViewModel,
            userData = userData,
            artist = artist,
            navigateToAddToPlaylist = {
                navController.navigateToAddToPlaylist(it)
            },
            navigateToShare = {
                ShareUtil.showShareDialog(activity, it.songs)
            },
        )
    }

    fun showAlbumMenuDialog(activity: Activity, album: Album) {
        activity.showAlbumMenuDialog(
            musicViewModel = musicViewModel,
            userData = userData,
            album = album,
            navigateToAddToPlaylist = {
                navController.navigateToAddToPlaylist(it)
            },
            navigateToShare = {
                ShareUtil.showShareDialog(activity, it.songs)
            },
        )
    }

    fun showPlaylistMenuDialog(activity: Activity, playlist: Playlist) {
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
            navigateToExport = {
                navController.navigateToExportPlaylist(it.id)
            },
            navigateToShare = {
                ShareUtil.showShareDialog(activity, it.songs)
            },
        )
    }
}
