package caios.android.kanade.feature.home.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.theme.bold
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.ui.music.AlbumHolder
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun HomeRecentlyAddedAlbumsSection(
    albums: ImmutableList<Album>,
    onClickMore: () -> Unit,
    onClickAlbum: (Long) -> Unit,
    onClickAlbumPlay: (Int, List<Song>) -> Unit,
    onClickAlbumMenu: (Album) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.home_title_recently_added_albums),
                style = MaterialTheme.typography.titleMedium.bold(),
                color = MaterialTheme.colorScheme.onSurface,
            )

            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(50))
                    .clickable { onClickMore.invoke() }
                    .padding(4.dp),
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 12.dp),
        ) {
            items(
                items = albums,
                key = { "added-${it.albumId}" },
            ) { album ->
                AlbumHolder(
                    modifier = Modifier.width(160.dp),
                    album = album,
                    onClickHolder = { onClickAlbum.invoke(album.albumId) },
                    onClickPlay = { onClickAlbumPlay.invoke(0, album.songs) },
                    onClickMenu = { onClickAlbumMenu.invoke(album) },
                )
            }
        }
    }
}
