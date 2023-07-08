package caios.android.kanade.core.ui.music

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.theme.end
import caios.android.kanade.core.model.Order
import caios.android.kanade.core.model.player.MusicOrder
import caios.android.kanade.core.model.player.MusicOrderOption

@Composable
fun SortInfo(
    sortOrder: MusicOrder,
    itemSize: Int,
    onClickSort: (MusicOrder) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .clickable { onClickSort.invoke(sortOrder) }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.padding(end = 4.dp),
                text = stringResource(
                    when (sortOrder.option) {
                        is MusicOrderOption.Song -> when (sortOrder.option as MusicOrderOption.Song) {
                            MusicOrderOption.Song.NAME -> R.string.sort_option_title
                            MusicOrderOption.Song.ARTIST -> R.string.sort_option_artist
                            MusicOrderOption.Song.ALBUM -> R.string.sort_option_album
                            MusicOrderOption.Song.DURATION -> R.string.sort_option_duration
                            MusicOrderOption.Song.YEAR -> R.string.sort_option_year
                            MusicOrderOption.Song.TRACK -> R.string.sort_option_track
                        }
                        is MusicOrderOption.Album -> when (sortOrder.option as MusicOrderOption.Album) {
                            MusicOrderOption.Album.NAME -> R.string.sort_option_title
                            MusicOrderOption.Album.TRACKS -> R.string.sort_option_tracks
                            MusicOrderOption.Album.ARTIST -> R.string.sort_option_artist
                            MusicOrderOption.Album.YEAR -> R.string.sort_option_year
                        }
                        is MusicOrderOption.Artist -> when (sortOrder.option as MusicOrderOption.Artist) {
                            MusicOrderOption.Artist.NAME -> R.string.sort_option_title
                            MusicOrderOption.Artist.TRACKS -> R.string.sort_option_tracks
                            MusicOrderOption.Artist.ALBUMS -> R.string.sort_option_albums
                        }
                        is MusicOrderOption.Playlist -> when (sortOrder.option as MusicOrderOption.Playlist) {
                            MusicOrderOption.Playlist.NAME -> R.string.sort_option_title
                            MusicOrderOption.Playlist.TRACKS -> R.string.sort_option_tracks
                        }
                    },
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = if (sortOrder.order == Order.ASC) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                contentDescription = null,
            )
        }

        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(1f),
            text = when (sortOrder.option) {
                is MusicOrderOption.Song -> stringResource(R.string.unit_song, itemSize)
                is MusicOrderOption.Artist -> stringResource(R.string.unit_artist, itemSize)
                is MusicOrderOption.Album -> stringResource(R.string.unit_album, itemSize)
                is MusicOrderOption.Playlist -> stringResource(R.string.unit_playlist, itemSize)
                else -> stringResource(R.string.common_unknown)
            },
            style = MaterialTheme.typography.bodyMedium.end(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview
@Composable
fun PreviewSortInfo() {
    SortInfo(
        sortOrder = MusicOrder(
            order = Order.ASC,
            option = MusicOrderOption.Album.ARTIST,
        ),
        itemSize = 0,
        onClickSort = {},
    )
}
