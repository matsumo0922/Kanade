package caios.android.kanade.feature.lyrics.top

import android.content.Intent
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.music.Lyrics
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.core.ui.view.KanadeTopAppBar
import caios.android.kanade.feature.lyrics.top.items.LyricsTopButtonSection
import caios.android.kanade.feature.lyrics.top.items.LyricsTopSongSection

@Composable
internal fun LyricsTopRoute(
    songId: Long,
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
                onFetchSyncedLyrics = viewModel::fetchLyrics,
                navigateToLyricsExplore = {
                    val query = "${it.title} ${it.artist} lyrics".replace(" ", "+")
                    val uri = "https://www.google.com/search?q=$query".toUri()

                    context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                },
                navigateToLyricsDownload = {},
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
    onFetchSyncedLyrics: (Song) -> Unit,
    navigateToLyricsExplore: (Song) -> Unit,
    navigateToLyricsDownload: (Song) -> Unit,
    onTerminate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberTopAppBarState()
    val behavior = TopAppBarDefaults.pinnedScrollBehavior(state)
    val clipboard = LocalClipboardManager.current
    var value by remember { mutableStateOf(TextFieldValue(lyrics?.lrc ?: "")) }

    Scaffold(
        modifier = modifier.nestedScroll(behavior.nestedScrollConnection),
        topBar = {
            KanadeTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.lyrics_edit_title),
                behavior = behavior,
                onClickMenuPlayNext = { },
                onClickMenuAddToQueue = { },
                onClickMenuAddToPlaylist = { },
                onTerminate = onTerminate,
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

            BasicTextField(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
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
                    color = MaterialTheme.colorScheme.onSurface,
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primaryContainer),
            )

            LyricsTopButtonSection(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                onClickExplore = { navigateToLyricsExplore.invoke(song) },
                onClickDownload = { navigateToLyricsDownload.invoke(song) },
                onClickPaste = { value = value.copy(text = clipboard.getText()?.toString() ?: "") },
                onClickSelectAll = {
                    value = value.copy(selection = TextRange(0, value.text.length))
                },
            )
        }
    }
}
