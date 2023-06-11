package caios.android.kanade.core.ui.controller

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.model.music.RepeatMode
import caios.android.kanade.core.model.music.ShuffleMode
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.music.MusicUiState

@Composable
fun MainController(
    uiState: MusicUiState,
    onClickPlay: () -> Unit,
    onClickPause: () -> Unit,
    onClickSkipToNext: () -> Unit,
    onClickSkipToPrevious: () -> Unit,
    onClickShuffle: (ShuffleMode) -> Unit,
    onClickRepeat: (RepeatMode) -> Unit,
    modifier: Modifier = Modifier,
) {

}

@Preview
@Composable
private fun Preview() {
    KanadeBackground {
        MainController(
            uiState = MusicUiState().copy(
                song = Song.dummy(),
                progressParent = 0.3f,
                progressString = "01:58",
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