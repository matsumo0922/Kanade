package caios.android.kanade.feature.welcome

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import caios.android.kanade.feature.welcome.permission.navigateToWelcomePermission
import caios.android.kanade.feature.welcome.welcome.WelcomeRoute
import caios.android.kanade.feature.welcome.welcome.welcomeScreen

@Composable
fun WelcomeNavHost(
    modifier: Modifier = Modifier,
    startDestination: String = WelcomeRoute,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        welcomeScreen(
            navigateToWelcomePermission = { navController.navigateToWelcomePermission() },
        )
    }
}
