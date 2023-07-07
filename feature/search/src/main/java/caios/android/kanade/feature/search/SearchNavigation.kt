package caios.android.kanade.feature.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import caios.android.kanade.core.design.animation.NavigateAnimation
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.model.music.Song

const val SearchRoute = "search"

fun NavController.navigateToSearch() {
    this.navigate(SearchRoute) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.searchScreen(
    navigateToArtistDetail: (Long) -> Unit,
    navigateToAlbumDetail: (Long) -> Unit,
    navigateToPlaylistDetail: (Long) -> Unit,
    navigateToSongMenu: (Song) -> Unit,
    navigateToArtistMenu: (Artist) -> Unit,
    navigateToAlbumMenu: (Album) -> Unit,
    navigateToPlaylistMenu: (Playlist) -> Unit,
    terminate: () -> Unit,
) {
    composable(
        route = SearchRoute,
        enterTransition = { NavigateAnimation.Detail.enter },
        exitTransition = { NavigateAnimation.Detail.exit },
        popEnterTransition = { NavigateAnimation.Detail.popEnter },
        popExitTransition = { NavigateAnimation.Detail.popExit },
    ) {
        SearchRoute(
            navigateToArtistDetail = navigateToArtistDetail,
            navigateToAlbumDetail = navigateToAlbumDetail,
            navigateToPlaylistDetail = navigateToPlaylistDetail,
            navigateToSongMenu = navigateToSongMenu,
            navigateToArtistMenu = navigateToArtistMenu,
            navigateToAlbumMenu = navigateToAlbumMenu,
            navigateToPlaylistMenu = navigateToPlaylistMenu,
            terminate = terminate,
        )
    }
}
