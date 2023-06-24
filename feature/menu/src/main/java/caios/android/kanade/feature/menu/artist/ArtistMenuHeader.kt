package caios.android.kanade.feature.menu.artist

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.ui.music.Artwork
import caios.android.kanade.core.ui.util.marquee

@Composable
internal fun ArtistMenuHeader(
    artist: Artist,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Card(
            modifier = Modifier.padding(end = 8.dp),
            shape = RoundedCornerShape(50),
        ) {
            Artwork(
                modifier = Modifier.size(48.dp),
                artwork = artist.artwork,
            )
        }

        Text(
            modifier = Modifier
                .weight(1f)
                .marquee(),
            text = artist.artist,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ArtistMenuHeaderPreview() {
    KanadeBackground {
        ArtistMenuHeader(
            modifier = Modifier.fillMaxWidth(),
            artist = Artist.dummy()
        )
    }
}
