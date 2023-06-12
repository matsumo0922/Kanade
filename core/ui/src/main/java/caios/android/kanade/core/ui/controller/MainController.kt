package caios.android.kanade.core.ui.controller

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import caios.android.kanade.core.ui.controller.items.MainControllerArtworkSection
import caios.android.kanade.core.ui.controller.items.MainControllerBottomButtonSection
import caios.android.kanade.core.ui.controller.items.MainControllerControlButtonSection
import caios.android.kanade.core.ui.controller.items.MainControllerInfoSection
import caios.android.kanade.core.ui.controller.items.MainControllerToolBarSection

@Composable
fun MainController(
    uiState: MusicUiState,
    onClickClose: () -> Unit,
    onClickSearch: () -> Unit,
    onClickMenuAddPlaylist: () -> Unit,
    onClickMenuArtist: () -> Unit,
    onClickMenuAlbum: () -> Unit,
    onClickMenuEqualizer: () -> Unit,
    onClickMenuEdit: () -> Unit,
    onClickMenuAnalyze: () -> Unit,
    onClickMenuDetailInfo: () -> Unit,
    onClickPlay: () -> Unit,
    onClickPause: () -> Unit,
    onClickSkipToNext: () -> Unit,
    onClickSkipToPrevious: () -> Unit,
    onClickShuffle: (ShuffleMode) -> Unit,
    onClickRepeat: (RepeatMode) -> Unit,
    onClickSeek: (Float) -> Unit,
    onClickLyrics: () -> Unit,
    onClickFavorite: () -> Unit,
    onClickSleepTimer: () -> Unit,
    onClickQueue: () -> Unit,
    onClickKaraoke: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        MainControllerToolBarSection(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .wrapContentHeight(),
            onClickClose = onClickClose,
            onClickSearch = onClickSearch,
            onClickMenuAddPlaylist = onClickMenuAddPlaylist,
            onClickMenuArtist = onClickMenuArtist,
            onClickMenuAlbum = onClickMenuAlbum,
            onClickMenuEqualizer = onClickMenuEqualizer,
            onClickMenuEdit = onClickMenuEdit,
            onClickMenuAnalyze = onClickMenuAnalyze,
            onClickMenuDetailInfo = onClickMenuDetailInfo,
        )

        MainControllerArtworkSection(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            songs = uiState.queueItems,
            index = uiState.queueIndex,
            onSwipeArtwork = { },
        )

        MainControllerInfoSection(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            uiState = uiState,
            onSeek = onClickSeek,
        )

        MainControllerControlButtonSection(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            uiState = uiState,
            onClickPlay = onClickPlay,
            onClickPause = onClickPause,
            onClickSkipToNext = onClickSkipToNext,
            onClickSkipToPrevious = onClickSkipToPrevious,
            onClickShuffle = onClickShuffle,
            onClickRepeat = onClickRepeat,
        )

        MainControllerBottomButtonSection(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = 24.dp)
                .fillMaxWidth()
                .wrapContentSize(),
            isFavorite = false,
            onClickLyrics = onClickLyrics,
            onClickFavorite = onClickFavorite,
            onClickSleepTimer = onClickSleepTimer,
            onClickQueue = onClickQueue,
            onClickKaraoke = onClickKaraoke,
        )

        Spacer(Modifier.height(12.dp))
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
                shuffleMode = ShuffleMode.ON,
                repeatMode = RepeatMode.ALL,
                queueItems = Song.dummies(20),
                queueIndex = 3,
            ),
            onClickClose = { },
            onClickSearch = { },
            onClickMenuAddPlaylist = { },
            onClickMenuArtist = { },
            onClickMenuAlbum = { },
            onClickMenuEqualizer = { },
            onClickMenuEdit = { },
            onClickMenuAnalyze = { },
            onClickMenuDetailInfo = { },
            onClickPlay = { },
            onClickPause = { },
            onClickSkipToNext = { },
            onClickSkipToPrevious = { },
            onClickShuffle = { },
            onClickRepeat = { },
            onClickSeek = { },
            onClickLyrics = { },
            onClickFavorite = { },
            onClickSleepTimer = { },
            onClickQueue = { },
            onClickKaraoke = { },
        )
    }
}
