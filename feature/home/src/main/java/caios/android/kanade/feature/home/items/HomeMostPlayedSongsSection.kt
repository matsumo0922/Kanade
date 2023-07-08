package caios.android.kanade.feature.home.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.theme.bold
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.ui.music.MiniSongHolder

internal fun LazyListScope.homeMostPlayedSongsSection(
    histories: List<Pair<Song, Int>>,
    onClickSongMenu: (Song) -> Unit,
    onClickPlay: (Int, List<Song>) -> Unit,
    onClickMore: () -> Unit,
) {
    item {
        Spacer(Modifier.height(16.dp))
    }

    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.home_title_most_played_songs),
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
    }

    item {
        Spacer(Modifier.height(16.dp))
    }

    itemsIndexed(
        items = histories,
        key = { index, history -> "most-${history.first.id}-$index" },
    ) { index, history ->
        MiniSongHolder(
            modifier = Modifier.fillMaxWidth(),
            song = history.first,
            subText = stringResource(R.string.unit_song, history.second),
            onClickHolder = { onClickPlay.invoke(index, histories.map { it.first }) },
            onClickMenu = { onClickSongMenu.invoke(history.first) },
        )
    }

    item {
        Spacer(Modifier.height(16.dp))
    }
}
