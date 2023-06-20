package caios.android.kanade.feature.artist.top

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val ArtistTopRoute = "artistTop"

fun NavController.navigateToArtistTop(navOptions: NavOptions? = null) {
    this.navigate(ArtistTopRoute, navOptions)
}

fun NavGraphBuilder.artistTopScreen(
    topMargin: Dp,
    navigateToArtistDetail: (Long) -> Unit,
) {
    composable(route = ArtistTopRoute) {
        ArtistTopRoute(
            topMargin = topMargin,
            navigateToArtistDetail = navigateToArtistDetail,
        )
    }
}
