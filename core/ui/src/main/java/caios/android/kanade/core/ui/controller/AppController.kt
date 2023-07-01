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
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.PlayerEvent
import caios.android.kanade.core.music.MusicUiState

@Composable
fun AppController(
    uiState: MusicUiState,
    offsetRate: Float,
    onControllerEvent: (event: PlayerEvent) -> Unit,
    onClickBottomController: () -> Unit,
    onClickCloseExpanded: () -> Unit,
    onClickFavorite: (Song) -> Unit,
    onRequestLyrics: (Song) -> Unit,
    onFetchFavorite: suspend (Song) -> Boolean,
    navigateToLyrics: () -> Unit,
    navigateToSleepTimer: () -> Unit,
    navigateToQueue: () -> Unit,
    navigateToKaraoke: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxSize()) {
        BottomController(
            modifier = Modifier
                .alpha(offsetRate)
                .height(72.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(MaterialTheme.colorScheme.surface)
                .clickable { onClickBottomController() },
            uiState = uiState,
            onClickPlay = { onControllerEvent.invoke(PlayerEvent.Play) },
            onClickPause = { onControllerEvent.invoke(PlayerEvent.Pause) },
            onClickSkipToNext = { onControllerEvent.invoke(PlayerEvent.SkipToNext) },
            onClickSkipToPrevious = { onControllerEvent.invoke(PlayerEvent.SkipToPrevious) },
        )

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
            onClickPlay = { onControllerEvent.invoke(PlayerEvent.Play) },
            onClickPause = { onControllerEvent.invoke(PlayerEvent.Pause) },
            onClickSkipToNext = { onControllerEvent.invoke(PlayerEvent.SkipToNext) },
            onClickSkipToPrevious = { onControllerEvent.invoke(PlayerEvent.SkipToPrevious) },
            onClickSkipToQueue = { onControllerEvent.invoke(PlayerEvent.SkipToQueue(it)) },
            onClickShuffle = { onControllerEvent.invoke(PlayerEvent.Shuffle(it)) },
            onClickRepeat = { onControllerEvent.invoke(PlayerEvent.Repeat(it)) },
            onClickSeek = { onControllerEvent.invoke(PlayerEvent.Seek(it)) },
            onClickLyrics = navigateToLyrics,
            onClickFavorite = { uiState.song?.let { onClickFavorite.invoke(it) } },
            onClickSleepTimer = navigateToSleepTimer,
            onClickQueue = navigateToQueue,
            onClickKaraoke = navigateToKaraoke,
            onRequestLyrics = onRequestLyrics,
            onFetchFavorite = onFetchFavorite,
        )
    }
}
