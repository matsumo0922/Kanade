package caios.android.kanade.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import caios.android.kanade.core.design.component.AnimatedIcon
import caios.android.kanade.core.design.component.KanadeNavigationBar
import caios.android.kanade.core.design.component.KanadeNavigationBarItem
import caios.android.kanade.core.design.icon.Icon
import caios.android.kanade.navigation.LibraryDestination

@Composable
fun KanadeBottomBar(
    destination: List<LibraryDestination>,
    onNavigateToDestination: (LibraryDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    KanadeNavigationBar(modifier) {
        destination.forEach { destination ->
            val isSelected = (currentDestination?.hierarchy?.any { it.route?.contains(destination.name, true) ?: false } == true)

            KanadeNavigationBarItem(
                isSelected = isSelected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    if (destination.icon is Icon.DrawableResourceIcon) {
                        AnimatedIcon(
                            animatedIcon = destination.icon.id,
                            isSelected = isSelected,
                        )
                    }
                },
                label = { Text(stringResource(destination.textId)) },
            )
        }
    }
}
