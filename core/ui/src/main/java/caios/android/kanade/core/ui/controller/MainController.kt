package caios.android.kanade.core.ui.controller

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.model.music.RepeatMode
import caios.android.kanade.core.model.music.ShuffleMode
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.music.MusicUiState
import caios.android.kanade.core.ui.controller.items.MainControllerBottomButtonSection
import caios.android.kanade.core.ui.controller.items.MainControllerControlButtonSection
import caios.android.kanade.core.ui.controller.items.MainControllerInfoSection
import caios.android.kanade.core.ui.controller.items.MainControllerToolBarSection

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
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        MainControllerToolBarSection(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            onClickClose = { /*TODO*/ },
            onClickSearch = { /*TODO*/ },
            onClickMenuAddPlaylist = { /*TODO*/ },
            onClickMenuArtist = { /*TODO*/ },
            onClickMenuAlbum = { /*TODO*/ },
            onClickMenuEqualizer = { /*TODO*/ },
            onClickMenuEdit = { /*TODO*/ },
            onClickMenuAnalyze = { /*TODO*/ },
            onClickMenuDetailInfo = { /*TODO*/ },
        )

        MainControllerInfoSection(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            uiState = uiState,
            onSeek = { /*TODO*/ },
        )

        MainControllerControlButtonSection(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            uiState = uiState,
            onClickPlay = { /*TODO*/ },
            onClickPause = { /*TODO*/ },
            onClickSkipToNext = { /*TODO*/ },
            onClickSkipToPrevious = { /*TODO*/ },
            onClickShuffle = { /*TODO*/ },
            onClickRepeat = { /*TODO*/ },
        )

        MainControllerBottomButtonSection(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(),
            isFavorite = false,
            onClickLyrics = { /*TODO*/ },
            onClickFavorite = { /*TODO*/ },
            onClickSleepTimer = { /*TODO*/ },
            onClickQueue = { /*TODO*/ },
            onClickKaraoke = { /*TODO*/ },
        )
    }
}

@Preview(showBackground = true)
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