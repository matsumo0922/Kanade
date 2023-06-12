package caios.android.kanade.core.ui.music

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.model.music.Artist

@Composable
fun ArtistHolder(
    artist: Artist,
    onClickHolder: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.padding(4.dp)) {
        Card(
            modifier = Modifier.clickable { onClickHolder.invoke() },
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Artwork(
                    modifier = Modifier.fillMaxWidth(),
                    artwork = artist.artwork,
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = artist.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.unit_song, artist.songs.size),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ArtistHolderPreview() {
    KanadeBackground(Modifier.wrapContentSize()) {
        ArtistHolder(
            modifier = Modifier.width(64.dp),
            artist = Artist.dummy(),
            onClickHolder = { },
        )
    }
}
