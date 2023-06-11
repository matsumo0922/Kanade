package caios.android.kanade.core.ui.controller.items

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.design.theme.center
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.music.MusicUiState

@Composable
internal fun MainControllerInfoSection(
    uiState: MusicUiState,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier) {
        val (title, artist, slider, progress, remain) = createRefs()

        Text(
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top)
                start.linkTo(parent.start, 16.dp)
                end.linkTo(parent.end, 16.dp)

                width = Dimension.fillToConstraints
            },
            text = uiState.song?.title ?: stringResource(R.string.music_unknown_title),
            style = MaterialTheme.typography.titleLarge.center(),
            color = MaterialTheme.colorScheme.onSurface,
        )

        Text(
            modifier = Modifier.constrainAs(artist) {
                top.linkTo(title.bottom, 8.dp)
                start.linkTo(parent.start, 16.dp)
                end.linkTo(parent.end, 16.dp)

                width = Dimension.fillToConstraints
            },
            text = uiState.song?.artist ?: stringResource(R.string.music_unknown_artist),
            style = MaterialTheme.typography.bodyMedium.center(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Slider(
            modifier = Modifier.constrainAs(slider) {
                top.linkTo(artist.bottom, 24.dp)
                start.linkTo(parent.start, 16.dp)
                end.linkTo(parent.end, 16.dp)

                width = Dimension.fillToConstraints
            },
            value = uiState.progressParent,
            valueRange = 0f..1f,
            onValueChange = onSeek,
            enabled = uiState.song != null,
        )

        Text(
            modifier = Modifier.constrainAs(progress) {
                top.linkTo(slider.bottom, 2.dp)
                start.linkTo(parent.start, 24.dp)
                bottom.linkTo(parent.bottom)
            },
            text = uiState.progressString,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text(
            modifier = Modifier.constrainAs(remain) {
                top.linkTo(slider.bottom, 2.dp)
                end.linkTo(parent.end, 24.dp)
                bottom.linkTo(parent.bottom)
            },
            text = uiState.song?.duration?.toDurationString() ?: "--:--",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private fun Long.toDurationString(): String {
    val second = this / 1000
    val minute = second / 60
    val hour = minute / 60

    return if (hour > 0) {
        "%02d:%02d:%02d".format(hour, minute % 60, second % 60)
    } else {
        "%02d:%02d".format(minute, second % 60)
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    KanadeBackground(Modifier.wrapContentSize()) {
        MainControllerInfoSection(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            uiState = MusicUiState().copy(
                song = Song.dummy(),
                progressParent = 0.3f,
                progressString = "01:58",
            ),
            onSeek = { },
        )
    }
}