package caios.android.kanade.feature.artist.detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.Song

const val ArtistDetailId = "artistDetailId"
const val ArtistDetailRoute = "artistDetail/{$ArtistDetailId}"

fun NavController.navigateToArtistDetail(artistId: Long) {
    this.navigate("artistDetail/$artistId")
}

fun NavGraphBuilder.artistDetailScreen(
    navigateToArtistMenu: (Artist) -> Unit,
    navigateToSongMenu: (Song) -> Unit,
    navigateToAlbumDetail: (Long) -> Unit,
    terminate: () -> Unit,
) {
    composable(
        route = ArtistDetailRoute,
        arguments = listOf(
            navArgument(ArtistDetailId) { type = NavType.LongType },
        ),
    ) {
        ArtistDetailRoute(
            artistId = it.arguments?.getLong(ArtistDetailId) ?: -1L,
            navigateToArtistMenu = navigateToArtistMenu,
            navigateToSongMenu = navigateToSongMenu,
            terminate = terminate,
        )
    }
}
