package caios.android.kanade.feature.lyrics.top

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.music.Lyrics
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.core.ui.view.KanadeTopAppBar

@Composable
internal fun LyricsTopRoute(
    songId: Long,
    terminate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LyricsTopViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(songId) {
        viewModel.fetch(songId)
    }

    AsyncLoadContents(
        modifier = modifier,
        screenState = screenState,
        retryAction = { terminate.invoke() },
    ) {
        if (it != null) {
            LyricsTopScreen(
                modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                song = it.song,
                lyrics = it.lyrics,
                onFetchSyncedLyrics = viewModel::fetchLyrics,
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
    onTerminate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberTopAppBarState()
    val behavior = TopAppBarDefaults.pinnedScrollBehavior(state)

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
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = lyrics?.text ?: "no data",
                style = MaterialTheme.typography.bodyMedium,
            )

            Button(
                modifier = Modifier.padding(16.dp),
                onClick = { onFetchSyncedLyrics.invoke(song) },
            ) {
                Text(stringResource(R.string.lyrics_datastore_musixmatch))
            }
        }
    }
}
