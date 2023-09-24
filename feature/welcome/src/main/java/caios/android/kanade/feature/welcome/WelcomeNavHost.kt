package caios.android.kanade.feature.welcome

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import caios.android.kanade.feature.welcome.permission.navigateToWelcomePermission
import caios.android.kanade.feature.welcome.permission.welcomePermissionScreen
import caios.android.kanade.feature.welcome.plus.navigateToWelcomePlus
import caios.android.kanade.feature.welcome.plus.welcomePlusScreen
import caios.android.kanade.feature.welcome.top.WelcomeTopRoute
import caios.android.kanade.feature.welcome.top.welcomeTopScreen

@Composable
fun WelcomeNavHost(
    navigateToBillingPlus: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = WelcomeTopRoute,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        welcomeTopScreen(
            navigateToWelcomePermission = { navController.navigateToWelcomePermission() },
        )

        welcomePermissionScreen(
            navigateToWelcomePlus = { navController.navigateToWelcomePlus() },
        )

        welcomePlusScreen(
            navigateToBillingPlus = navigateToBillingPlus,
            navigateToHome = onComplete,
        )
    }
}
