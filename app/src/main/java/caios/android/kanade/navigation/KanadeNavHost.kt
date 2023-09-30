package caios.android.kanade.navigation

import android.app.Activity
import android.content.Intent
import android.media.audiofx.AudioEffect
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.navigation.compose.NavHost
import caios.android.kanade.core.common.network.util.ToastUtil
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.model.Version
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
import caios.android.kanade.feature.download.format.downloadFormatScreen
import caios.android.kanade.feature.download.format.navigateToDownloadFormat
import caios.android.kanade.feature.download.input.downloadInputDialog
import caios.android.kanade.feature.equalizer.equalizerScreen
import caios.android.kanade.feature.home.HomeRoute
import caios.android.kanade.feature.home.homeScreen
import caios.android.kanade.feature.information.about.aboutScreen
import caios.android.kanade.feature.information.song.navigateToSongInformation
import caios.android.kanade.feature.information.song.songInformationDialog
import caios.android.kanade.feature.information.versions.showVersionHistoryDialog
import caios.android.kanade.feature.lyrics.download.lyricsDownloadDialog
import caios.android.kanade.feature.lyrics.download.navigateToLyricsDownload
import caios.android.kanade.feature.lyrics.top.lyricsTopScreen
import caios.android.kanade.feature.lyrics.top.navigateToLyricsTop
import caios.android.kanade.feature.menu.album.showAlbumMenuDialog
import caios.android.kanade.feature.menu.artist.showArtistMenuDialog
import caios.android.kanade.feature.menu.delete.deleteSongDialog
import caios.android.kanade.feature.menu.delete.navigateToDeleteSong
import caios.android.kanade.feature.menu.playlist.showPlaylistMenuDialog
import caios.android.kanade.feature.menu.song.showSongMenuDialog
import caios.android.kanade.feature.playlist.add.addToPlaylistDialog
import caios.android.kanade.feature.playlist.add.navigateToAddToPlaylist
import caios.android.kanade.feature.playlist.create.createPlaylistDialog
import caios.android.kanade.feature.playlist.create.navigateToCreatePlaylist
import caios.android.kanade.feature.playlist.detail.navigateToPlaylistDetail
import caios.android.kanade.feature.playlist.detail.playlistDetailScreen
import caios.android.kanade.feature.playlist.export.exportPlaylistDialog
import caios.android.kanade.feature.playlist.export.navigateToExportPlaylist
import caios.android.kanade.feature.playlist.external.importPlaylistDialog
import caios.android.kanade.feature.playlist.external.navigateToImportPlaylist
import caios.android.kanade.feature.playlist.fab.fabPlaylistDialog
import caios.android.kanade.feature.playlist.fab.navigateToFabPlaylist
import caios.android.kanade.feature.playlist.rename.navigateToRenamePlaylist
import caios.android.kanade.feature.playlist.rename.renamePlaylistDialog
import caios.android.kanade.feature.playlist.top.playlistTopScreen
import caios.android.kanade.feature.queue.showQueueDialog
import caios.android.kanade.feature.search.scan.scanMediaDialog
import caios.android.kanade.feature.setting.developer.navigateToSettingDeveloper
import caios.android.kanade.feature.setting.developer.settingDeveloperDialog
import caios.android.kanade.feature.setting.oss.navigateToSettingLicense
import caios.android.kanade.feature.setting.oss.settingLicenseScreen
import caios.android.kanade.feature.setting.theme.navigateToSettingTheme
import caios.android.kanade.feature.setting.theme.settingThemeScreen
import caios.android.kanade.feature.setting.top.settingTopScreen
import caios.android.kanade.feature.share.ShareUtil
import caios.android.kanade.feature.song.detail.navigateToSongDetail
import caios.android.kanade.feature.song.detail.songDetailScreen
import caios.android.kanade.feature.song.top.songTopScreen
import caios.android.kanade.feature.sort.showSortDialog
import caios.android.kanade.feature.tag.navigateToTagEdit
import caios.android.kanade.feature.tag.tagEditScreen
import caios.android.kanade.ui.KanadeAppState
import kotlinx.collections.immutable.ImmutableList
import kotlin.reflect.KClass

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
            navigateToLyricsTop = {
                navController.navigateToLyricsTop(it)
            },
            navigateToTagEdit = {
                navController.navigateToTagEdit(it)
            },
            navigateToSongInformation = {
                navController.navigateToSongInformation(it)
            },
            navigateToShare = {
                ShareUtil.showShareDialog(activity, listOf(it))
            },
            navigateToDelete = {
                navController.navigateToDeleteSong(listOf(it.id))
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
            navigateToShare = {
                ShareUtil.showShareDialog(activity, it.songs)
            },
            navigateToDelete = {
                navController.navigateToDeleteSong(it.songs.map { song -> song.id })
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
            navigateToShare = {
                ShareUtil.showShareDialog(activity, it.songs)
            },
            navigateToDelete = {
                navController.navigateToDeleteSong(it.songs.map { song -> song.id })
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
            navigateToExport = {
                navController.navigateToExportPlaylist(it.id)
            },
            navigateToShare = {
                ShareUtil.showShareDialog(activity, it.songs)
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
            navigateToShare = {
                ShareUtil.showShareDialog(activity, it)
            },
        )
    }

    fun showSortDialog(type: KClass<*>) {
        activity.showSortDialog(
            musicViewModel = musicViewModel,
            userData = userData,
            type = type,
        )
    }

    fun showVersionHistory(versionHistory: ImmutableList<Version>) {
        activity.showVersionHistoryDialog(
            userData = userData,
            versionHistory = versionHistory,
        )
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
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
            navigateToSort = ::showSortDialog,
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
            navigateToSort = ::showSortDialog,
        )

        artistTopScreen(
            topMargin = libraryTopBarHeight,
            navigateToSort = ::showSortDialog,
            navigateToArtistDetail = {
                navController.navigateToArtistDetail(it)
            },
        )

        albumTopScreen(
            topMargin = libraryTopBarHeight,
            navigateToSort = ::showSortDialog,
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
            navigateToImportPlaylist = {
                navController.navigateToImportPlaylist()
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

        exportPlaylistDialog(
            terminate = {
                navController.popBackStack()
            },
        )

        importPlaylistDialog(
            terminate = {
                navController.popBackStack()
            },
        )

        songInformationDialog(
            terminate = {
                navController.popBackStack()
            },
        )

        deleteSongDialog(
            terminate = {
                navController.popBackStack()
            },
        )

        lyricsTopScreen(
            navigateToLyricsDownload = {
                navController.navigateToLyricsDownload(it)
            },
            terminate = {
                navController.popBackStack()
            },
        )

        lyricsDownloadDialog(
            terminate = {
                navController.popBackStack()
            },
        )

        tagEditScreen(
            terminate = {
                navController.popBackStack()
            },
        )

        scanMediaDialog(
            terminate = {
                navController.popBackStack()
            },
        )

        downloadInputDialog(
            navigateToDownloadFormat = {
                navController.navigateToDownloadFormat(it)
            },
            terminate = {
                navController.popBackStack()
            },
        )

        downloadFormatScreen(
            navigateToBillingPlus = {
                appState.showBillingPlusDialog(activity)
            },
            navigateToTagEdit = {
                navController.navigateToTagEdit(it)
            },
            terminate = {
                navController.popBackStack()
            },
        )

        equalizerScreen(
            terminate = {
                navController.popBackStack()
            },
        )

        aboutScreen(
            navigateToVersionHistory = ::showVersionHistory,
            navigateToDonate = {
            },
            terminate = {
                navController.popBackStack()
            },
        )

        settingTopScreen(
            navigateToEqualizer = {
                activity.startActivity(
                    Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL).apply {
                        putExtra(AudioEffect.EXTRA_PACKAGE_NAME, activity.packageName)
                        putExtra(AudioEffect.EXTRA_AUDIO_SESSION, 0)
                        putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)
                    },
                )
            },
            navigateToSettingTheme = {
                navController.navigateToSettingTheme()
            },
            navigateToSettingDeveloper = {
                navController.navigateToSettingDeveloper()
            },
            navigateToOpenSourceLicense = {
                navController.navigateToSettingLicense()
            },
            terminate = {
                navController.popBackStack()
            },
        )

        settingThemeScreen(
            navigateToBillingPlus = {
                appState.showBillingPlusDialog(activity)
            },
            terminate = {
                navController.popBackStack()
            },
        )

        settingLicenseScreen(
            terminate = {
                navController.popBackStack()
            },
        )

        settingDeveloperDialog(
            terminate = {
                navController.popBackStack()
            },
        )
    }
}
