package caios.android.kanade.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import caios.android.kanade.ui.KanadeAppState

@Composable
fun KanadeNavHost(
    appState: KanadeAppState,
    modifier: Modifier = Modifier,
    startDestination: String = "",
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {

    }
}
