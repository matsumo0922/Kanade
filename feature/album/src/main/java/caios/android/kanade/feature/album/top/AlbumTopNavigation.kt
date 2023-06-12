package caios.android.kanade.feature.album.top

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val AlbumTopRoute = "albumTop"

fun NavController.navigateToAlbumTop(navOptions: NavOptions? = null) {
    this.navigate(AlbumTopRoute, navOptions)
}

fun NavGraphBuilder.albumTopScreen(
    topMargin: Dp,
) {
    composable(route = AlbumTopRoute) {
        AlbumTopRoute(
            topMargin = topMargin,
        )
    }
}
