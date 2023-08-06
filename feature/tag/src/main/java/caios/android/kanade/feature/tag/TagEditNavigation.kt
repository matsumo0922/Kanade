package caios.android.kanade.feature.tag

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import caios.android.kanade.core.design.animation.NavigateAnimation

const val TagEditId = "tagEditId"
const val TagEditRoute = "tagEdit/{$TagEditId}"

fun NavController.navigateToTagEdit(songId: Long) {
    this.navigate("tagEdit/$songId")
}

fun NavGraphBuilder.tagEditScreen(
    terminate: () -> Unit,
) {
    composable(
        route = TagEditRoute,
        arguments = listOf(navArgument(TagEditId) { type = NavType.LongType }),
        enterTransition = { NavigateAnimation.Vertical.enter },
        exitTransition = { NavigateAnimation.Vertical.exit },
        popEnterTransition = { NavigateAnimation.Vertical.popEnter },
        popExitTransition = { NavigateAnimation.Vertical.popExit },
    ) {
        TagEditRoute(
            modifier = Modifier.fillMaxSize(),
            songId = it.arguments?.getLong(TagEditId) ?: -1L,
            terminate = terminate,
        )
    }
}
