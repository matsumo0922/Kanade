package caios.android.kanade.core.ui.controller

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.palette.graphics.Palette
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.design.theme.Blue40
import caios.android.kanade.core.design.theme.Green40
import caios.android.kanade.core.design.theme.Orange40
import caios.android.kanade.core.design.theme.Purple40
import caios.android.kanade.core.design.theme.Teal40
import caios.android.kanade.core.model.music.Artwork
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.RepeatMode
import caios.android.kanade.core.model.player.ShuffleMode
import caios.android.kanade.core.music.MusicUiState
import caios.android.kanade.core.music.MusicUiState.Companion.isDarkMode
import caios.android.kanade.core.ui.amlv.FadingEdge
import caios.android.kanade.core.ui.amlv.LyricsView
import caios.android.kanade.core.ui.amlv.LyricsViewState
import caios.android.kanade.core.ui.amlv.rememberLyricsViewState
import caios.android.kanade.core.ui.controller.items.MainControllerArtworkSection
import caios.android.kanade.core.ui.controller.items.MainControllerBottomButtonSection
import caios.android.kanade.core.ui.controller.items.MainControllerControlButtonSection
import caios.android.kanade.core.ui.controller.items.MainControllerEmptyLyricsItem
import caios.android.kanade.core.ui.controller.items.MainControllerInfoSection
import caios.android.kanade.core.ui.controller.items.MainControllerTextSection
import caios.android.kanade.core.ui.controller.items.MainControllerToolBarSection
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun MainController(
    uiState: MusicUiState,
    onClickClose: () -> Unit,
    onClickSearch: () -> Unit,
    onClickMenuAddPlaylist: (Long) -> Unit,
    onClickMenuArtist: (Long) -> Unit,
    onClickMenuAlbum: (Long) -> Unit,
    onClickMenuEqualizer: () -> Unit,
    onClickMenuEdit: () -> Unit,
    onClickMenuAnalyze: () -> Unit,
    onClickMenuDetailInfo: () -> Unit,
    onClickPlay: () -> Unit,
    onClickPause: () -> Unit,
    onClickSkipToNext: () -> Unit,
    onClickSkipToPrevious: () -> Unit,
    onClickSkipToQueue: (Int) -> Unit,
    onClickShuffle: (ShuffleMode) -> Unit,
    onClickRepeat: (RepeatMode) -> Unit,
    onClickSeek: (Float) -> Unit,
    onClickLyrics: (Long) -> Unit,
    onClickFavorite: () -> Unit,
    onClickSleepTimer: () -> Unit,
    onClickQueue: () -> Unit,
    onClickKaraoke: () -> Unit,
    onFetchFavorite: suspend (Song) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val view = LocalView.current
    val scope = rememberCoroutineScope()
    val isDarkMode = uiState.userData?.isDarkMode()
    var isFavorite by remember { mutableStateOf(false) }
    var isLyricsVisible by remember { mutableStateOf(false) }
    var artworkColor by remember { mutableStateOf(Color.Transparent) }
    val gradientColor by animateColorAsState(
        targetValue = artworkColor,
        animationSpec = tween(300, 0, LinearEasing),
        label = "gradientColor",
    )

    val state = rememberLyricsViewState(
        uiState.lyrics,
        object : LyricsViewState.Listener {
            override fun onSeek(position: Long) {
                onClickSeek((position).coerceAtLeast(0).toFloat() / (uiState.song?.duration ?: 1))
            }
        },
    )

    when (uiState.isPlaying) {
        true -> state.play()
        false -> state.pause()
    }

    state.position = uiState.progress

    LaunchedEffect(uiState.song) {
        val song = uiState.song ?: return@LaunchedEffect
        val artwork = song.albumArtwork

        artworkColor = getArtworkColor(context, artwork, isDarkMode ?: false)
        isFavorite = onFetchFavorite.invoke(song)
    }

    LaunchedEffect(isLyricsVisible) {
        view.keepScreenOn = (isLyricsVisible && uiState.lyrics != null)
    }

    Column(
        modifier = modifier.background(
            brush = Brush.verticalGradient(
                listOf(gradientColor, Color.Transparent),
            ),
        ),
    ) {
        MainControllerToolBarSection(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .wrapContentHeight(),
            onClickClose = onClickClose,
            onClickSearch = {
                onClickClose.invoke()
                onClickSearch.invoke()
            },
            onClickMenuAddPlaylist = { uiState.song?.id?.let { onClickMenuAddPlaylist.invoke(it) } },
            onClickMenuArtist = {
                onClickClose.invoke()
                uiState.song?.artistId?.let { onClickMenuArtist.invoke(it) }
            },
            onClickMenuAlbum = {
                onClickClose.invoke()
                uiState.song?.albumId?.let { onClickMenuAlbum.invoke(it) }
            },
            onClickMenuEqualizer = onClickMenuEqualizer,
            onClickMenuEdit = onClickMenuEdit,
            onClickMenuAnalyze = onClickMenuAnalyze,
            onClickMenuDetailInfo = onClickMenuDetailInfo,
        )

        Box(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .weight(1f),
        ) {
            androidx.compose.animation.AnimatedVisibility(
                modifier = Modifier.fillMaxSize(),
                visible = isLyricsVisible,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                if (uiState.lyrics != null) {
                    LyricsView(
                        state = state,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(
                            top = 24.dp,
                            bottom = 32.dp,
                            start = 16.dp,
                            end = 16.dp,
                        ),
                        darkTheme = isDarkMode ?: false,
                        fadingEdge = FadingEdge(top = 32.dp, bottom = 64.dp),
                        fontSize = 28.sp,
                    )
                } else {
                    MainControllerEmptyLyricsItem(
                        modifier = Modifier.fillMaxWidth(),
                        onClickSetLyrics = {
                            scope.launch {
                                uiState.song?.let {
                                    onClickClose.invoke()
                                    onClickLyrics.invoke(it.id)
                                }
                            }
                        },
                    )
                }
            }

            androidx.compose.animation.AnimatedVisibility(
                modifier = Modifier.fillMaxSize(),
                visible = !isLyricsVisible,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Column {
                    MainControllerArtworkSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .weight(1f),
                        songs = uiState.queueItems.toImmutableList(),
                        index = uiState.queueIndex,
                        onSwipeArtwork = onClickSkipToQueue,
                    )

                    MainControllerTextSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        song = uiState.song,
                    )
                }
            }
        }

        MainControllerInfoSection(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            uiState = uiState,
            onSeek = onClickSeek,
        )

        MainControllerControlButtonSection(
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    bottom = 24.dp,
                )
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
                .padding(
                    top = 4.dp,
                    bottom = 24.dp,
                )
                .fillMaxWidth()
                .wrapContentSize(),
            isFavorite = isFavorite,
            onClickLyrics = { isLyricsVisible = !isLyricsVisible },
            onClickLyricsEdit = {
                scope.launch {
                    uiState.song?.let {
                        onClickClose.invoke()
                        onClickLyrics.invoke(it.id)
                    }
                }
            },
            onClickFavorite = {
                isFavorite = !isFavorite
                onClickFavorite.invoke()
            },
            onClickSleepTimer = onClickSleepTimer,
            onClickQueue = onClickQueue,
            onClickKaraoke = onClickKaraoke,
        )

        Spacer(Modifier.height(12.dp))
    }
}

private suspend fun getArtworkColor(
    context: Context,
    artwork: Artwork,
    isDarkMode: Boolean
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
            palette.lightVibrantSwatch,
            palette.vibrantSwatch,
            palette.darkVibrantSwatch,
            palette.darkMutedSwatch,
        )

        return Color((if (isDarkMode) list.last() else list.first()).rgb)
    } catch (e: Throwable) {
        Timber.e(e)
        return Color.Transparent
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    KanadeBackground {
        MainController(
            uiState = MusicUiState().copy(
                song = Song.dummy(),
                queueItems = Song.dummies(20),
                queueIndex = 3,
                progress = 200238,
                shuffleMode = ShuffleMode.ON,
                repeatMode = RepeatMode.ALL,
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
            onClickSkipToQueue = { },
            onClickShuffle = { },
            onClickRepeat = { },
            onClickSeek = { },
            onClickLyrics = { },
            onClickFavorite = { },
            onClickSleepTimer = { },
            onClickQueue = { },
            onClickKaraoke = { },
            onFetchFavorite = { true },
        )
    }
}
