package caios.android.kanade.core.ui.controller.items

import android.widget.ImageView
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.RepeatMode
import caios.android.kanade.core.model.player.ShuffleMode
import caios.android.kanade.core.music.MusicUiState

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
internal fun MainControllerControlButtonSection(
    uiState: MusicUiState,
    onClickPlay: () -> Unit,
    onClickPause: () -> Unit,
    onClickSkipToNext: () -> Unit,
    onClickSkipToPrevious: () -> Unit,
    onClickShuffle: (ShuffleMode) -> Unit,
    onClickRepeat: (RepeatMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(horizontal = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ShuffleButton(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(50)),
            shuffleMode = uiState.shuffleMode,
            onClick = onClickShuffle,
        )

        Icon(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(50))
                .clickable { onClickSkipToPrevious() }
                .padding(8.dp)
                .border(
                    width = 1.dp,
                    color = LocalContentColor.current,
                    shape = RoundedCornerShape(50),
                )
                .padding(4.dp),
            imageVector = Icons.Filled.SkipPrevious,
            contentDescription = "Skip to previous",
        )

        Icon(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(50))
                .clickable { if (uiState.isPlaying) onClickPause() else onClickPlay() }
                .padding(8.dp)
                .border(
                    width = 1.dp,
                    color = LocalContentColor.current,
                    shape = RoundedCornerShape(50),
                )
                .padding(8.dp),
            imageVector = if (uiState.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            contentDescription = "Play or Pause",
        )

        Icon(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(50))
                .clickable { onClickSkipToNext() }
                .padding(8.dp)
                .border(
                    width = 1.dp,
                    color = LocalContentColor.current,
                    shape = RoundedCornerShape(50),
                )
                .padding(4.dp),
            imageVector = Icons.Filled.SkipNext,
            contentDescription = "Skip to next",
        )

        RepeatButton(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(50)),
            repeatMode = uiState.repeatMode,
            onClick = onClickRepeat,
        )
    }
}

@Composable
private fun ShuffleButton(
    shuffleMode: ShuffleMode,
    onClick: (ShuffleMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = when (shuffleMode) {
        ShuffleMode.OFF -> android.R.attr.state_checked
        ShuffleMode.ON -> android.R.attr.state_checked * -1
    }

    val color = when (shuffleMode) {
        ShuffleMode.OFF -> MaterialTheme.colorScheme.onSurfaceVariant
        ShuffleMode.ON -> MaterialTheme.colorScheme.onSurface
    }

    AndroidView(
        modifier = modifier
            .clickable {
                onClick.invoke(
                    when (shuffleMode) {
                        ShuffleMode.ON -> ShuffleMode.OFF
                        ShuffleMode.OFF -> ShuffleMode.ON
                    },
                )
            }
            .padding(8.dp),
        factory = {
            ImageView(it).apply {
                setImageResource(R.drawable.as_shuffle)
                setImageState(intArrayOf(state), true)
            }
        },
        update = {
            it.setImageState(intArrayOf(state), true)
            it.setColorFilter(color.toArgb())
        },
    )
}

@Composable
private fun RepeatButton(
    repeatMode: RepeatMode,
    onClick: (RepeatMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = when (repeatMode) {
        RepeatMode.OFF -> android.R.attr.state_first
        RepeatMode.ALL -> android.R.attr.state_middle
        RepeatMode.ONE -> android.R.attr.state_last
    }

    val color = when (repeatMode) {
        RepeatMode.OFF -> MaterialTheme.colorScheme.onSurfaceVariant
        RepeatMode.ALL -> MaterialTheme.colorScheme.onSurface
        RepeatMode.ONE -> MaterialTheme.colorScheme.onSurface
    }

    AndroidView(
        modifier = modifier
            .clickable {
                onClick.invoke(
                    when (repeatMode) {
                        RepeatMode.OFF -> RepeatMode.ALL
                        RepeatMode.ALL -> RepeatMode.ONE
                        RepeatMode.ONE -> RepeatMode.OFF
                    },
                )
            }
            .padding(8.dp),
        factory = {
            ImageView(it).apply {
                setImageResource(R.drawable.as_repeat)
                setImageState(intArrayOf(state), true)
            }
        },
        update = {
            it.setImageState(intArrayOf(state), true)
            it.setColorFilter(color.toArgb())
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun MainControllerButtonSectionPreview() {
    KanadeBackground(Modifier.wrapContentSize()) {
        MainControllerControlButtonSection(
            uiState = MusicUiState().copy(
                song = Song.dummy(),
                shuffleMode = ShuffleMode.ON,
                repeatMode = RepeatMode.ALL,
            ),
            onClickPlay = { },
            onClickPause = { },
            onClickSkipToNext = { },
            onClickSkipToPrevious = { },
            onClickShuffle = { },
            onClickRepeat = { },
        )
    }
}
