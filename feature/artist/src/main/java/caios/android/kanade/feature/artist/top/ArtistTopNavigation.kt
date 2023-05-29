package caios.android.kanade.feature.artist.top

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val artistTopRoute = "artistTop"

fun NavController.navigateToArtistTop(navOptions: NavOptions? = null) {
    this.navigate(artistTopRoute, navOptions)
}

fun NavGraphBuilder.artistTopScreen(
    topMargin: Dp,
) {
    composable(route = artistTopRoute) {
        ArtistTopRoute(
            topMargin = topMargin,
        )
    }
}
