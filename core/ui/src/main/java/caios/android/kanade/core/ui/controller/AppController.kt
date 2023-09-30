package caios.android.kanade.core.ui.controller

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.palette.graphics.Palette
import caios.android.kanade.core.design.theme.Blue40
import caios.android.kanade.core.design.theme.Green40
import caios.android.kanade.core.design.theme.Orange40
import caios.android.kanade.core.design.theme.Purple40
import caios.android.kanade.core.design.theme.Teal40
import caios.android.kanade.core.model.music.Artwork
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.PlayerEvent
import caios.android.kanade.core.music.MusicUiState
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import okhttp3.internal.toHexString
import timber.log.Timber
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

        val context = LocalContext.current
        val view = LocalView.current

        var isFavorite by remember { mutableStateOf(false) }
        var isLyricsVisible by remember { mutableStateOf(false) }
        var artworkColor by remember { mutableStateOf(Color.Transparent) }

        val gradientColor by animateColorAsState(
            targetValue = artworkColor,
            animationSpec = tween(300, 0, LinearEasing),
            label = "gradientColor",
        )

        val lyricsAlpha by animateFloatAsState(
            targetValue = if (isLyricsVisible) 1f else 0f,
            animationSpec = tween(200, 0, LinearEasing),
            label = "lyricsAlpha",
        )

        LaunchedEffect(uiState.song) {
            val song = uiState.song ?: return@LaunchedEffect
            val artwork = song.albumArtwork

            artworkColor = getArtworkColor(context, artwork)
            isFavorite = onFetchFavorite.invoke(song)
        }

        LaunchedEffect(isLyricsVisible) {
            view.keepScreenOn = (isLyricsVisible && uiState.lyrics != null)
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
                isFavorite = isFavorite,
                isLyricsVisible = isLyricsVisible,
                gradientColor = gradientColor,
                lyricsAlpha = lyricsAlpha,
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
                onClickLyrics = { isLyricsVisible = !isLyricsVisible },
                onClickLyricsEdit = navigateToLyrics,
                onClickFavorite = {
                    uiState.song?.let {
                        isFavorite = !isFavorite
                        onClickFavorite.invoke(it)
                    }
                },
                onClickQueue = navigateToQueue,
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

private suspend fun getArtworkColor(
    context: Context,
    artwork: Artwork,
): Color {
    try {
        val builder = when (artwork) {
            is Artwork.Internal -> {
                return when (artwork.name.toList().sumOf { it.code } % 5) {
                    0 -> Blue40
                    1 -> Green40
                    2 -> Orange40
                    3 -> Purple40
                    4 -> Teal40
                    else -> throw IllegalArgumentException("Unknown album name.")
                }
            }

            is Artwork.Web -> ImageRequest.Builder(context).data(artwork.url)
            is Artwork.MediaStore -> ImageRequest.Builder(context).data(artwork.uri)
            else -> return Color.Transparent
        }.allowHardware(false)

        val request = builder.build()
        val result = (ImageLoader(context).execute(request) as? SuccessResult)?.drawable
            ?: return Color.Transparent
        val palette = Palette.from((result as BitmapDrawable).bitmap).generate()

        val list = listOfNotNull(
            palette.mutedSwatch,
            palette.lightMutedSwatch,
            palette.vibrantSwatch,
            palette.lightVibrantSwatch,
            palette.darkVibrantSwatch,
            palette.darkMutedSwatch,
        )

        Timber.d("artwork palette: ${list.map { it.rgb.toHexString() }}")

        return Color(list.first().rgb)
    } catch (e: Throwable) {
        Timber.e(e)
        return Color.Transparent
    }
}
