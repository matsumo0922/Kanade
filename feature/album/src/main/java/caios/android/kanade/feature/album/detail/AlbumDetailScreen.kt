package caios.android.kanade.feature.album.detail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.core.ui.music.SongHolder
import caios.android.kanade.core.ui.view.CoordinatorScaffold

@Composable
internal fun AlbumDetailRoute(
    albumId: Long,
    modifier: Modifier = Modifier,
    viewModel: AlbumDetailViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(albumId) {
        viewModel.fetch(albumId)
    }

    AsyncLoadContents(screenState) {
        AlbumDetailScreen(
            modifier = modifier,
            album = it?.album ?: Album.dummy(),
        )
    }
}

@Composable
private fun AlbumDetailScreen(
    album: Album,
    modifier: Modifier = Modifier,
) {
    CoordinatorScaffold(
        modifier = modifier,
        title = album.album,
        summary = album.artist,
        artwork = album.artwork,
        onClickNavigateUp = { /*TODO*/ },
    ) {
        items(
            items = album.songs,
            key = { it.id },
        ) { song ->
            SongHolder(
                modifier = Modifier.fillMaxWidth(),
                song = song,
                onClickHolder = { /*TODO*/ },
                onClickMenu = { /*TODO*/ },
            )
        }
    }
}