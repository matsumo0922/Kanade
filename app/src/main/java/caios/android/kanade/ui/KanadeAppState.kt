package caios.android.kanade.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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

    val shouldShowBottomBar: Boolean
        get() = windowSize.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowNavRail: Boolean
        get() = !shouldShowBottomBar

    fun navigateToLibrary(libraryDestination: LibraryDestination) {
        val navOption = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (libraryDestination) {
            LibraryDestination.Home     -> navController.navigate("library/home", navOption)
            LibraryDestination.Song     -> navController.navigate("library/song", navOption)
            LibraryDestination.Artist   -> navController.navigate("library/artist", navOption)
            LibraryDestination.Album    -> navController.navigate("library/album", navOption)
            LibraryDestination.Playlist -> navController.navigate("library/playlist", navOption)
        }
    }
}