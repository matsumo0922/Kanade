package caios.android.kanade.feature.lyrics.top

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.common.network.util.ToastUtil
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.music.Lyrics
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.repository.util.parseLrc
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.core.ui.dialog.SimpleAlertDialog
import caios.android.kanade.core.ui.view.KanadeTopAppBar
import caios.android.kanade.feature.lyrics.top.items.LyricsTopButtonSection
import caios.android.kanade.feature.lyrics.top.items.LyricsTopSongSection

@Composable
internal fun LyricsTopRoute(
    songId: Long,
    navigateToLyricsDownload: (Long) -> Unit,
    terminate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LyricsTopViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(songId) {
        viewModel.fetch(songId)
    }

    AsyncLoadContents(
        modifier = modifier,
        screenState = screenState,
        retryAction = { terminate.invoke() },
    ) { uiState ->
        if (uiState != null) {
            LyricsTopScreen(
                modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                song = uiState.song,
                lyrics = uiState.lyrics,
                navigateToLyricsExplore = {
                    val query = "${it.title} ${it.artist} lyrics".replace(" ", "+")
                    val uri = "https://www.google.com/search?q=$query".toUri()

                    context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                },
                navigateToLyricsDownload = navigateToLyricsDownload,
                onSaveLyrics = viewModel::save,
                onTerminate = terminate,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LyricsTopScreen(
    song: Song,
    lyrics: Lyrics?,
    navigateToLyricsExplore: (Song) -> Unit,
    navigateToLyricsDownload: (Long) -> Unit,
    onSaveLyrics: (Lyrics) -> Unit,
    onTerminate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val state = rememberTopAppBarState()
    val behavior = TopAppBarDefaults.pinnedScrollBehavior(state)
    val clipboard = LocalClipboardManager.current
    var value by remember { mutableStateOf(TextFieldValue(lyrics?.lrc ?: "")) }
    var isError by remember { mutableStateOf(false) }
    var isNotSaved by remember { mutableStateOf(false) }
    var isShowNotSaveDialog by remember { mutableStateOf(false) }

    LaunchedEffect(value.text) {
        isError = false
        isNotSaved = (lyrics?.lrc ?: "") != value.text
    }

    LaunchedEffect(lyrics) {
        if (lyrics != null) {
            value = TextFieldValue(lyrics.lrc)
        }
    }

    BackHandler {
        if (isNotSaved) {
            isShowNotSaveDialog = true
        } else {
            onTerminate.invoke()
        }
    }

    if (isShowNotSaveDialog) {
        SimpleAlertDialog(
            title = R.string.common_warning,
            message = R.string.lyrics_edit_not_saved_message,
            positiveText = R.string.common_save,
            negativeText = R.string.lyrics_edit_not_save,
            onPositiveClick = {
                parseLrc(song, value.text)?.let { lyrics ->
                    isNotSaved = false
                    onSaveLyrics.invoke(lyrics)
                    onTerminate.invoke()
                } ?: run {
                    isError = true
                }
            },
            onNegativeClick = {
                onTerminate.invoke()
            },
            onDismiss = {
                isShowNotSaveDialog = false
            },
        )
    }

    Scaffold(
        modifier = modifier.nestedScroll(behavior.nestedScrollConnection),
        topBar = {
            KanadeTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.lyrics_edit_title),
                behavior = behavior,
                onTerminate = {
                    if (isNotSaved) {
                        isShowNotSaveDialog = true
                    } else {
                        onTerminate.invoke()
                    }
                },
                isVisibleMenu = false,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LyricsTopSongSection(
                modifier = Modifier.fillMaxWidth(),
                song = song,
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .animateContentSize(),
            ) {
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = 2.dp,
                            color = if (isError) MaterialTheme.colorScheme.error else Color.Transparent,
                            shape = RoundedCornerShape(8.dp),
                        )
                        .weight(1f)
                        .horizontalScroll(rememberScrollState())
                        .padding(16.dp),
                    value = value,
                    onValueChange = { value = it },
                    decorationBox = { innerTextField ->
                        if (value.text.isEmpty()) {
                            Text(
                                text = stringResource(R.string.lyrics_edit_placeholder),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        } else {
                            innerTextField.invoke()
                        }
                    },
                    textStyle = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primaryContainer),
                )

                if (isError) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        text = stringResource(R.string.lyrics_edit_error),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }

            LyricsTopButtonSection(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                onClickExplore = { navigateToLyricsExplore.invoke(song) },
                onClickDownload = { navigateToLyricsDownload.invoke(song.id) },
                onClickPaste = {
                    value = value.copy(text = clipboard.getText()?.toString() ?: "")
                },
                onClickSelectAll = {
                    value = value.copy(selection = TextRange(0, value.text.length))
                },
                onClickSave = {
                    parseLrc(song, value.text)?.let { lyrics ->
                        isNotSaved = false
                        onSaveLyrics.invoke(lyrics)
                        ToastUtil.show(context, R.string.lyrics_edit_toast_saved)
                    } ?: kotlin.run {
                        isError = true
                    }
                },
            )
        }
    }
}
