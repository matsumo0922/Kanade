package caios.android.kanade.core.ui.controller

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.atLeast
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.design.theme.DarkDefaultColorScheme
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
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@Composable
fun MainController(
    uiState: MusicUiState,
    windowSize: WindowSizeClass,
    isFavorite: Boolean,
    isLyricsVisible: Boolean,
    lyricsAlpha: Float,
    gradientColor: Color,
    onClickClose: () -> Unit,
    onClickSearch: () -> Unit,
    onClickMenuAddPlaylist: (Long) -> Unit,
    onClickMenuArtist: (Long) -> Unit,
    onClickMenuAlbum: (Long) -> Unit,
    onClickMenuEqualizer: () -> Unit,
    onClickMenuEdit: () -> Unit,
    onClickMenuDetailInfo: (Long) -> Unit,
    onClickPlay: () -> Unit,
    onClickPause: () -> Unit,
    onClickSkipToNext: () -> Unit,
    onClickSkipToPrevious: () -> Unit,
    onClickSkipToQueue: (Int) -> Unit,
    onClickShuffle: (ShuffleMode) -> Unit,
    onClickRepeat: (RepeatMode) -> Unit,
    onClickSeek: (Float) -> Unit,
    onClickLyrics: () -> Unit,
    onClickLyricsEdit: (Long) -> Unit,
    onClickFavorite: () -> Unit,
    onClickQueue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val scope = rememberCoroutineScope()
    val isDarkMode = uiState.userData?.isDarkMode()
    val isLargeDevice = windowSize.heightSizeClass == WindowHeightSizeClass.Expanded

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

    MainControllerBackground {
        ConstraintLayout(
            modifier = modifier
                .background(Brush.verticalGradient(listOf(gradientColor, Color.Transparent)))
                .navigationBarsPadding()
                .statusBarsPadding(),
        ) {
            val (
                toolbar,
                artwork,
                text,
                info,
                control,
                button,
                lyrics,
            ) = createRefs()

            createVerticalChain(
                artwork,
                text,
                info,
                control,
                button,
                chainStyle = ChainStyle.Spread,
            )

            if (isLargeDevice) {
                MainControllerToolBarSection(
                    modifier = Modifier.constrainAs(toolbar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                        width = Dimension.fillToConstraints
                    },
                    onClickClose = onClickClose,
                    onClickSearch = {
                        onClickClose.invoke()
                        onClickSearch.invoke()
                    },
                    onClickMenuAddPlaylist = {
                        uiState.song?.id?.let {
                            onClickMenuAddPlaylist.invoke(
                                it,
                            )
                        }
                    },
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
                    onClickMenuDetailInfo = { uiState.song?.id?.let { onClickMenuDetailInfo.invoke(it) } },
                )
            }

            MainControllerArtworkSection(
                modifier = Modifier
                    .alpha(1f - lyricsAlpha)
                    .constrainAs(artwork) {
                        top.linkTo(if (isLargeDevice) toolbar.bottom else parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(info.top)

                        width = Dimension.fillToConstraints
                    }
                    .padding(top = if (isLargeDevice) 32.dp else 0.dp),
                songs = uiState.queueItems.toImmutableList(),
                index = uiState.queueIndex,
                onSwipeArtwork = onClickSkipToQueue,
            )

            MainControllerTextSection(
                modifier = Modifier
                    .alpha(1f - lyricsAlpha)
                    .constrainAs(text) {
                        top.linkTo(artwork.bottom)
                        bottom.linkTo(info.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                        width = Dimension.fillToConstraints
                    },
                song = uiState.song,
            )

            AnimatedVisibility(
                modifier = Modifier.constrainAs(lyrics) {
                    top.linkTo(artwork.top)
                    bottom.linkTo(text.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
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
                                    onClickLyricsEdit.invoke(it.id)
                                }
                            }
                        },
                    )
                }
            }

            MainControllerInfoSection(
                modifier = Modifier.constrainAs(info) {
                    top.linkTo(text.bottom)
                    bottom.linkTo(control.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                    width = Dimension.fillToConstraints
                },
                uiState = uiState,
                onSeek = onClickSeek,
            )

            MainControllerControlButtonSection(
                modifier = Modifier.constrainAs(control) {
                    top.linkTo(info.bottom)
                    bottom.linkTo(button.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                    width =
                        Dimension.preferredWrapContent.atLeast(configuration.screenWidthDp.dp * 0.8f)
                },
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
                    .constrainAs(button) {
                        top.linkTo(control.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                        width = Dimension.wrapContent
                    }
                    .padding(top = 8.dp),
                isFavorite = isFavorite,
                onClickLyrics = onClickLyrics,
                onClickLyricsEdit = {
                    scope.launch {
                        uiState.song?.let {
                            onClickClose.invoke()
                            onClickLyricsEdit.invoke(it.id)
                        }
                    }
                },
                onClickFavorite = onClickFavorite,
                onClickQueue = onClickQueue,
            )
        }
    }
}

@Composable
private fun MainControllerBackground(content: @Composable () -> Unit) {
    val colorScheme = DarkDefaultColorScheme.copy(
        primary = Color.White,
        surfaceVariant = Color.DarkGray,
    )

    MaterialTheme(colorScheme = colorScheme) {
        CompositionLocalProvider(LocalContentColor provides Color.White) {
            content.invoke()
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true)
@Composable
private fun Preview() {
    KanadeBackground(background = Color(0xFF121212)) {
        MainController(
            uiState = MusicUiState().copy(
                song = Song.dummy(),
                queueItems = Song.dummies(20),
                queueIndex = 3,
                progress = 200238,
                shuffleMode = ShuffleMode.ON,
                repeatMode = RepeatMode.ALL,
            ),
            windowSize = WindowSizeClass.calculateFromSize(DpSize.Zero),
            isFavorite = false,
            isLyricsVisible = false,
            lyricsAlpha = 0f,
            gradientColor = Color(0xFF121212),
            onClickClose = { },
            onClickSearch = { },
            onClickMenuAddPlaylist = { },
            onClickMenuArtist = { },
            onClickMenuAlbum = { },
            onClickMenuEqualizer = { },
            onClickMenuEdit = { },
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
            onClickLyricsEdit = { },
            onClickFavorite = { },
            onClickQueue = { },
        )
    }
}
