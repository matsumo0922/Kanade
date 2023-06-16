package caios.android.kanade.core.ui.controller.items

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.design.theme.center
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.music.MusicUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainControllerInfoSection(
    uiState: MusicUiState,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier) {
        val (title, artist, slider, progress, remain) = createRefs()

        val interactionSource = remember { MutableInteractionSource() }
        var sliderPosition by remember { mutableStateOf<Float?>(null) }
        val position by animateFloatAsState(sliderPosition ?: uiState.progressParent)

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
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
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
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Slider(
            modifier = Modifier.constrainAs(slider) {
                top.linkTo(artist.bottom, 24.dp)
                start.linkTo(parent.start, 16.dp)
                end.linkTo(parent.end, 16.dp)

                width = Dimension.fillToConstraints
            },
            enabled = uiState.song != null,
            value = position,
            valueRange = 0f..1f,
            onValueChange = {
                sliderPosition = it
            },
            onValueChangeFinished = {
                sliderPosition?.also {
                    onSeek.invoke(it)
                    sliderPosition = null
                }
            },
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = interactionSource,
                    thumbSize = DpSize(12.dp, 12.dp),
                    modifier = Modifier.padding(top = 4.dp),
                )
            },
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
                progress = 20000,
            ),
            onSeek = { },
        )
    }
}
