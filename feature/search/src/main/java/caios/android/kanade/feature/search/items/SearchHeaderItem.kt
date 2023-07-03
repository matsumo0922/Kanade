@file:Suppress("MatchingDeclarationName")

package caios.android.kanade.feature.search.items

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Album
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlaylistPlay
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.design.theme.extraBold

@Composable
internal fun SearchHeaderItem(
    type: HeaderItemType,
    size: Int,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentWidth()
                .align(Alignment.Center),
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    imageVector = when (type) {
                        HeaderItemType.Song -> Icons.Outlined.MusicNote
                        HeaderItemType.Artist -> Icons.Outlined.Person
                        HeaderItemType.Album -> Icons.Outlined.Album
                        HeaderItemType.Playlist -> Icons.Outlined.PlaylistPlay
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )

                Text(
                    text = "$size " + when (type) {
                        HeaderItemType.Song -> "Songs"
                        HeaderItemType.Artist -> "Artists"
                        HeaderItemType.Album -> "Albums"
                        HeaderItemType.Playlist -> "Playlists"
                    },
                    style = MaterialTheme.typography.titleMedium.extraBold(),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

internal enum class HeaderItemType {
    Song, Artist, Album, Playlist
}

@Preview(showBackground = true)
@Composable
private fun SearchHeaderItemPreview1() {
    KanadeBackground {
        SearchHeaderItem(
            type = HeaderItemType.Song,
            size = 12,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchHeaderItemPreview2() {
    KanadeBackground {
        SearchHeaderItem(
            type = HeaderItemType.Artist,
            size = 23,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchHeaderItemPreview3() {
    KanadeBackground {
        SearchHeaderItem(
            type = HeaderItemType.Album,
            size = 871,
        )
    }
} @Preview(showBackground = true)
@Composable
private fun SearchHeaderItemPreview4() {
    KanadeBackground {
        SearchHeaderItem(
            type = HeaderItemType.Playlist,
            size = 1145,
        )
    }
}
