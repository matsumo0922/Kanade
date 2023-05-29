package caios.android.kanade.feature.album.top

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val albumTopRoute = "albumTop"

fun NavController.navigateToAlbumTop(navOptions: NavOptions? = null) {
    this.navigate(albumTopRoute, navOptions)
}

fun NavGraphBuilder.albumTopScreen(
    topMargin: Dp,
) {
    composable(route = albumTopRoute) {
        AlbumTopRoute(
            topMargin = topMargin,
        )
    }
}
