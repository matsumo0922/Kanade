package caios.android.kanade.feature.song.top

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import caios.android.kanade.core.model.music.Song

const val SongTopRoute = "songTop"

fun NavController.navigateToSongTop(navOptions: NavOptions? = null) {
    this.navigate(SongTopRoute, navOptions)
}

fun NavGraphBuilder.songTopScreen(
    topMargin: Dp,
    navigateToSongMenu: (Song) -> Unit,
) {
    composable(
        route = SongTopRoute,
        enterTransition = {
            fadeIn(tween(240)) + scaleIn(
                initialScale = 0.92f,
                transformOrigin = TransformOrigin.Center,
                animationSpec = tween(240, 0, CubicBezierEasing(0.0f, 0.0f, 0.0f, 1.0f)),
            )
        },
    ) {
        SongTopRoute(
            topMargin = topMargin,
            navigateToSongMenu = navigateToSongMenu,
        )
    }
}
