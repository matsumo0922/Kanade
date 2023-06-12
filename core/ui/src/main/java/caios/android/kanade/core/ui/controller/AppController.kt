package caios.android.kanade.core.ui.controller

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.model.music.ControllerEvent
import caios.android.kanade.core.music.MusicUiState

@Composable
fun AppController(
    uiState: MusicUiState,
    offsetRate: Float,
    onControllerEvent: (event: ControllerEvent) -> Unit,
    onClickBottomController: () -> Unit,
    onClickCloseExpanded: () -> Unit,
    navigateToLyrics: () -> Unit,
    navigateToFavorite: () -> Unit,
    navigateToSleepTimer: () -> Unit,
    navigateToQueue: () -> Unit,
    navigateToKaraoke: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxSize()) {
        if (offsetRate > 0f) {
            BottomController(
                modifier = Modifier
                    .alpha(offsetRate)
                    .height(72.dp)
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable { onClickBottomController() },
                uiState = uiState,
                onClickPlay = { onControllerEvent.invoke(ControllerEvent.Play) },
                onClickPause = { onControllerEvent.invoke(ControllerEvent.Pause) },
                onClickSkipToNext = { onControllerEvent.invoke(ControllerEvent.SkipToNext) },
                onClickSkipToPrevious = { onControllerEvent.invoke(ControllerEvent.SkipToPrevious) },
            )
        }

        if (offsetRate < 1f) {
            MainController(
                modifier = Modifier
                    .alpha(1f - offsetRate)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
                uiState = uiState,
                onClickClose = onClickCloseExpanded,
                onClickSearch = { },
                onClickMenuAddPlaylist = { },
                onClickMenuArtist = { },
                onClickMenuAlbum = { },
                onClickMenuEqualizer = { },
                onClickMenuEdit = { },
                onClickMenuAnalyze = { },
                onClickMenuDetailInfo = { },
                onClickPlay = { onControllerEvent.invoke(ControllerEvent.Play) },
                onClickPause = { onControllerEvent.invoke(ControllerEvent.Pause) },
                onClickSkipToNext = { onControllerEvent.invoke(ControllerEvent.SkipToNext) },
                onClickSkipToPrevious = { onControllerEvent.invoke(ControllerEvent.SkipToPrevious) },
                onClickShuffle = { onControllerEvent.invoke(ControllerEvent.Shuffle(it)) },
                onClickRepeat = { onControllerEvent.invoke(ControllerEvent.Repeat(it)) },
                onClickSeek = { onControllerEvent.invoke(ControllerEvent.Seek(it)) },
                onClickLyrics = navigateToLyrics,
                onClickFavorite = navigateToFavorite,
                onClickSleepTimer = navigateToSleepTimer,
                onClickQueue = navigateToQueue,
                onClickKaraoke = navigateToKaraoke,
            )
        }
    }
}
