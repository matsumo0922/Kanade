package caios.android.kanade.feature.welcome

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import caios.android.kanade.feature.welcome.permission.WelcomePermissionRoute
import caios.android.kanade.feature.welcome.permission.navigateToWelcomePermission
import caios.android.kanade.feature.welcome.permission.welcomePermissionScreen
import caios.android.kanade.feature.welcome.plus.navigateToWelcomePlus
import caios.android.kanade.feature.welcome.plus.welcomePlusScreen
import caios.android.kanade.feature.welcome.top.WelcomeTopRoute
import caios.android.kanade.feature.welcome.top.welcomeTopScreen
import timber.log.Timber

@Composable
fun WelcomeNavHost(
    navigateToBillingPlus: () -> Unit,
    onComplete: () -> Unit,
    isAgreedTeams: Boolean,
    isAllowedPermission: Boolean,
    modifier: Modifier = Modifier,
) {
    Timber.d("WelcomeNavHost: isAgreedTeams=$isAgreedTeams, isAllowedPermission=$isAllowedPermission")

    val navController = rememberNavController()
    val startDestination = when {
        !isAgreedTeams -> WelcomeTopRoute
        !isAllowedPermission -> WelcomePermissionRoute
        else -> WelcomeTopRoute
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        welcomeTopScreen(
            navigateToWelcomePlus = { navController.navigateToWelcomePlus() },
        )

        welcomePlusScreen(
            navigateToBillingPlus = navigateToBillingPlus,
            navigateToWelcomePermission = { navController.navigateToWelcomePermission() },
        )

        welcomePermissionScreen(
            navigateToHome = onComplete,
        )
    }
}
