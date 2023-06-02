package caios.android.kanade.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import caios.android.kanade.core.design.R
import caios.android.kanade.navigation.LibraryDestination
import caios.android.kanade.navigation.isLibraryDestinationInHierarchy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KanadeDrawer(
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
    onClickItem: (LibraryDestination) -> Unit = {},
) {
    ModalDrawerSheet {
        Column(
            modifier = modifier
                .width(256.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.inverseOnSurface)
                .padding(24.dp),
        ) {
            NavigationDrawerItem(
                isSelected = currentDestination.isLibraryDestinationInHierarchy(LibraryDestination.Home),
                label = stringResource(R.string.navigation_home),
                icon = Icons.Default.Home,
                onClick = { onClickItem.invoke(LibraryDestination.Home) },
            )

            NavigationDrawerItem(
                isSelected = currentDestination.isLibraryDestinationInHierarchy(LibraryDestination.Playlist),
                label = stringResource(R.string.navigation_playlist),
                icon = Icons.Filled.QueueMusic,
                onClick = { onClickItem.invoke(LibraryDestination.Playlist) },
            )

            NavigationDrawerItem(
                isSelected = currentDestination.isLibraryDestinationInHierarchy(LibraryDestination.Song),
                label = stringResource(R.string.navigation_song),
                icon = Icons.Filled.MusicNote,
                onClick = { onClickItem.invoke(LibraryDestination.Song) },
            )

            NavigationDrawerItem(
                isSelected = currentDestination.isLibraryDestinationInHierarchy(LibraryDestination.Artist),
                label = stringResource(R.string.navigation_artist),
                icon = Icons.Filled.Person,
                onClick = { onClickItem.invoke(LibraryDestination.Artist) },
            )

            NavigationDrawerItem(
                isSelected = currentDestination.isLibraryDestinationInHierarchy(LibraryDestination.Album),
                label = stringResource(R.string.navigation_album),
                icon = Icons.Default.Album,
                onClick = { onClickItem.invoke(LibraryDestination.Album) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NavigationDrawerItem(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    NavigationDrawerItem(
        modifier = modifier,
        selected = isSelected,
        label = {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = label,
            )
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
            )
        },
        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
        onClick = onClick,
    )
}