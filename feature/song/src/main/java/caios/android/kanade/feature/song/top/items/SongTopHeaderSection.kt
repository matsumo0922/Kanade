package caios.android.kanade.feature.song.top.items

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.design.theme.Blue60
import caios.android.kanade.core.design.theme.Blue80
import caios.android.kanade.core.design.theme.Green60
import caios.android.kanade.core.design.theme.Green80
import caios.android.kanade.core.design.theme.Orange60
import caios.android.kanade.core.design.theme.Orange80
import caios.android.kanade.core.design.theme.Purple60
import caios.android.kanade.core.design.theme.Purple80

@Composable
internal fun SongTopHeaderSection(
    onClickHistory: () -> Unit,
    onClickRecentlyAdd: () -> Unit,
    onClickMostPlayed: () -> Unit,
    onClickShuffle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(horizontal = 24.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ImageButton(
            modifier = Modifier.weight(1f),
            textRes = R.string.song_header_history,
            imageVector = Icons.Default.History,
            tintColor = Blue60,
            backgroundColor = Blue80.copy(alpha = 0.2f),
            onClick = onClickHistory,
        )

        ImageButton(
            modifier = Modifier.weight(1f),
            textRes = R.string.song_header_recently_add,
            imageVector = Icons.Default.LibraryAdd,
            tintColor = Orange60,
            backgroundColor = Orange80.copy(alpha = 0.2f),
            onClick = onClickRecentlyAdd,
        )

        ImageButton(
            modifier = Modifier.weight(1f),
            textRes = R.string.song_header_most_played,
            imageVector = Icons.Default.TrendingUp,
            tintColor = Green60,
            backgroundColor = Green80.copy(alpha = 0.2f),
            onClick = onClickMostPlayed
        )

        ImageButton(
            modifier = Modifier.weight(1f),
            textRes = R.string.song_header_shuffle,
            imageVector = Icons.Default.Shuffle,
            tintColor = Purple60,
            backgroundColor = Purple80.copy(alpha = 0.2f),
            onClick = onClickShuffle,
        )
    }
}

@Composable
private fun ImageButton(
    @StringRes textRes: Int,
    imageVector: ImageVector,
    tintColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(50),
                )
                .clip(RoundedCornerShape(50))
                .clickable { onClick.invoke() }
                .padding(12.dp),
            imageVector = imageVector,
            contentDescription = null,
            tint = tintColor,
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(textRes),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SongTopHeaderSectionPreview() {
    KanadeBackground(Modifier.fillMaxWidth()) {
        SongTopHeaderSection(
            modifier = Modifier.fillMaxWidth(),
            onClickHistory = { },
            onClickRecentlyAdd = { },
            onClickMostPlayed = { },
            onClickShuffle = { },
        )
    }
}