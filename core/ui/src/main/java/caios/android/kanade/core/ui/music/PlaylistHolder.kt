package caios.android.kanade.core.ui.music

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.model.music.Playlist

@Composable
fun PlaylistHolder(
    playlist: Playlist,
    onClickHolder: () -> Unit,
    onClickPlay: () -> Unit,
    onClickMenu: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClickHolder.invoke() },
        ) {
        Column(
            modifier = Modifier.padding(bottom = 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp),
                    contentColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp),
                ),
            ) {
                Box(Modifier.fillMaxWidth()) {
                    MultiArtwork(
                        modifier = Modifier.fillMaxWidth(),
                        songs = playlist.songs,
                    )

                    PlayButton(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.BottomStart)
                            .size(28.dp),
                        onClick = onClickPlay,
                    )

                    Icon(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(32.dp)
                            .align(Alignment.TopEnd)
                            .clip(RoundedCornerShape(50))
                            .clickable { onClickMenu.invoke() }
                            .padding(4.dp),
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            Text(
                modifier = Modifier
                    .padding(
                        top = 4.dp,
                        start = 4.dp,
                        end = 4.dp,
                    )
                    .fillMaxWidth(),
                text = playlist.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .fillMaxWidth(),
                text = stringResource(R.string.unit_song, playlist.items.size),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun PlayButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.clickable { onClick.invoke() },
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp),
            contentColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp),
        ),
    ) {
        Icon(
            modifier = Modifier.padding(4.dp),
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaylistHolderPreview() {
    KanadeBackground {
        PlaylistHolder(
            modifier = Modifier.width(256.dp),
            playlist = Playlist.dummy(size = 2),
            onClickHolder = { },
            onClickPlay = { },
            onClickMenu = { },
        )
    }
}
