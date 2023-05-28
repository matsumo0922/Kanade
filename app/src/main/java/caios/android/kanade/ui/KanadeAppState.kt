package caios.android.kanade.ui

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
import caios.android.kanade.feature.album.top.navigateToAlbumTop
import caios.android.kanade.feature.artist.top.navigateToArtistTop
import caios.android.kanade.feature.home.navigateToHome
import caios.android.kanade.feature.playlist.top.navigateToPlaylistTop
import caios.android.kanade.feature.song.top.navigateToSongTop
import caios.android.kanade.navigation.LibraryDestination
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberKanadeAppState(
    windowSize: WindowSizeClass,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): KanadeAppState {
    return remember(
        navController,
        coroutineScope,
        windowSize,
    ) {
        KanadeAppState(
            navController,
            coroutineScope,
            windowSize,
        )
    }
}

@Stable
class KanadeAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val windowSize: WindowSizeClass,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

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
}
