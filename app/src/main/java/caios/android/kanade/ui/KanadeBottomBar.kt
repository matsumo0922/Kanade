package caios.android.kanade.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavDestination
import caios.android.kanade.core.design.component.AnimatedIcon
import caios.android.kanade.core.design.component.KanadeNavigationBar
import caios.android.kanade.core.design.component.KanadeNavigationBarItem
import caios.android.kanade.core.design.component.KanadeNavigationDefaults
import caios.android.kanade.core.design.icon.Icon
import caios.android.kanade.navigation.LibraryDestination
import caios.android.kanade.navigation.isLibraryDestinationInHierarchy
import kotlinx.collections.immutable.ImmutableList

@Composable
fun KanadeBottomBar(
    destination: ImmutableList<LibraryDestination>,
    onNavigateToDestination: (LibraryDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    KanadeNavigationBar(modifier) {
        destination.forEach { destination ->
            val isSelected = currentDestination.isLibraryDestinationInHierarchy(destination)

            KanadeNavigationBarItem(
                isSelected = isSelected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    if (destination.icon is Icon.DrawableResourceIcon) {
                        AnimatedIcon(
                            animatedIcon = destination.icon.id,
                            isSelected = isSelected,
                            tint = if (isSelected) {
                                KanadeNavigationDefaults.navigationSelectedItemColor()
                            } else {
                                KanadeNavigationDefaults.navigationContentColor()
                            },
                        )
                    }
                },
                label = {
                    Text(
                        text = stringResource(destination.textId),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                    )
                },
            )
        }
    }
}
