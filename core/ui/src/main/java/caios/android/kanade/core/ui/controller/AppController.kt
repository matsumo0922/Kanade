package caios.android.kanade.core.ui.controller

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.PlayerEvent
import caios.android.kanade.core.music.MusicUiState
import kotlin.math.pow

@Composable
fun AppController(
    uiState: MusicUiState,
    windowSize: WindowSizeClass,
    offsetRate: Float,
    onControllerEvent: (event: PlayerEvent) -> Unit,
    onClickBottomController: () -> Unit,
    onClickCloseExpanded: () -> Unit,
    onClickFavorite: (Song) -> Unit,
    onFetchFavorite: suspend (Song) -> Boolean,
    navigateToEqualizer: () -> Unit,
    navigateToSearch: () -> Unit,
    navigateToArtist: (Long) -> Unit,
    navigateToAlbum: (Long) -> Unit,
    navigateToAddToPlaylist: (Long) -> Unit,
    navigateToLyrics: (Long) -> Unit,
    navigateToSongInfo: (Long) -> Unit,
    navigateToQueue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxSize()) {
        BackHandler(offsetRate == 0f) {
            onClickCloseExpanded()
        }

        if (offsetRate != 0f) {
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
        }

        if (offsetRate != 1f) {
            MainController(
                modifier = Modifier
                    .alpha(1f - offsetRate)
                    .fillMaxSize()
                    .background(Color(0xFF121314)),
                uiState = uiState,
                windowSize = windowSize,
                onClickClose = onClickCloseExpanded,
                onClickSearch = navigateToSearch,
                onClickMenuAddPlaylist = navigateToAddToPlaylist,
                onClickMenuArtist = navigateToArtist,
                onClickMenuAlbum = navigateToAlbum,
                onClickMenuEqualizer = navigateToEqualizer,
                onClickMenuEdit = { },
                onClickMenuDetailInfo = navigateToSongInfo,
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
                onClickQueue = navigateToQueue,
                onFetchFavorite = onFetchFavorite,
            )
        }
    }
}

@Suppress("UnusedPrivateMember")
private fun calculateMoveOffset(toOffset: Offset, fromOffset: Offset, percent: Float): Offset {
    return Offset(
        lerp(toOffset.x, fromOffset.x, percent),
        lerp(toOffset.y, fromOffset.y, percent),
    )
}

@Suppress("UnusedPrivateMember")
private fun calculateMoveSize(toSize: IntSize, fromSize: IntSize, percent: Float): IntSize {
    return IntSize(
        lerp(toSize.width.toFloat(), fromSize.width.toFloat(), percent).toInt(),
        lerp(toSize.height.toFloat(), fromSize.height.toFloat(), percent).toInt(),
    )
}

@Suppress("UnusedPrivateMember")
private fun calculateMoveOffsetBezier(toOffset: Offset, fromOffset: Offset, thirdOffset: Offset, percent: Float): Offset {
    return Offset(
        (1f - percent).pow(2) * toOffset.x + 2f * percent * (1f - percent) * thirdOffset.x + percent.pow(2) * fromOffset.x,
        (1f - percent).pow(2) * toOffset.y + 2f * percent * (1f - percent) * thirdOffset.y + percent.pow(2) * fromOffset.y,
    )
}
