package caios.android.kanade.feature.artist.detail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.core.ui.music.SongDetailHeader
import caios.android.kanade.core.ui.music.SongHolder
import caios.android.kanade.core.ui.view.CoordinatorScaffold

@Composable
internal fun ArtistDetailRoute(
    artistId: Long,
    navigateToArtistMenu: (Artist) -> Unit,
    navigateToSongMenu: (Song) -> Unit,
    terminate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ArtistDetailViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(artistId) {
        viewModel.fetch(artistId)
    }

    AsyncLoadContents(screenState) {
        if (it != null) {
            ArtistDetailScreen(
                modifier = modifier,
                artist = it.artist,
                onClickSongHolder = viewModel::onNewPlay,
                onClickSongMenu = navigateToSongMenu,
                onClickMenu = navigateToArtistMenu,
                onTerminate = terminate,
            )
        }
    }
}

@Composable
private fun ArtistDetailScreen(
    artist: Artist,
    onClickSongHolder: (List<Song>, Int) -> Unit,
    onClickSongMenu: (Song) -> Unit,
    onClickMenu: (Artist) -> Unit,
    onTerminate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CoordinatorScaffold(
        modifier = modifier,
        title = artist.artist,
        summary = artist.artist,
        artwork = artist.artwork,
        shouldUseBlur = false,
        onClickNavigateUp = onTerminate,
        onClickMenu = { onClickMenu.invoke(artist) },
    ) {
        item {
            SongDetailHeader(
                modifier = Modifier.fillMaxWidth(),
                onClickSeeAll = { /*TODO*/ },
            )
        }

        itemsIndexed(
            items = artist.songs.take(6),
            key = { _, song -> song.id },
        ) { index, song ->
            SongHolder(
                modifier = Modifier.fillMaxWidth(),
                song = song,
                onClickHolder = { onClickSongHolder.invoke(artist.songs, index) },
                onClickMenu = { onClickSongMenu.invoke(song) },
            )
        }
    }
}
