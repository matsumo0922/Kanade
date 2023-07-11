package caios.android.kanade.feature.artist.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.ArtistDetail
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.ui.FullAsyncLoadContents
import caios.android.kanade.core.ui.music.AlbumDetailHeader
import caios.android.kanade.core.ui.music.AlbumHolder
import caios.android.kanade.core.ui.music.SongDetailHeader
import caios.android.kanade.core.ui.music.SongHolder
import caios.android.kanade.core.ui.view.CoordinatorData
import caios.android.kanade.core.ui.view.CoordinatorScaffold
import caios.android.kanade.feature.artist.detail.items.ArtistDetailBiography

@Composable
internal fun ArtistDetailRoute(
    artistId: Long,
    navigateToSongDetail: (String, List<Long>) -> Unit,
    navigateToAlbumDetail: (Long) -> Unit,
    navigateToArtistMenu: (Artist) -> Unit,
    navigateToSongMenu: (Song) -> Unit,
    navigateToAlbumMenu: (Album) -> Unit,
    terminate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ArtistDetailViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(artistId) {
        viewModel.fetch(artistId)
    }

    FullAsyncLoadContents(
        modifier = modifier,
        screenState = screenState,
        retryAction = { terminate.invoke() },
    ) {
        if (it != null) {
            ArtistDetailScreen(
                modifier = Modifier.fillMaxSize(),
                artist = it.artist,
                artistDetail = it.artistDetail,
                onClickSeeAll = navigateToSongDetail,
                onClickSongHolder = viewModel::onNewPlay,
                onClickSongMenu = navigateToSongMenu,
                onClickAlbumHolder = navigateToAlbumDetail,
                onClickAlbumMenu = navigateToAlbumMenu,
                onClickMenu = navigateToArtistMenu,
                onClickShuffle = viewModel::onShufflePlay,
                onTerminate = terminate,
            )
        }
    }
}

@Composable
private fun ArtistDetailScreen(
    artist: Artist,
    artistDetail: ArtistDetail?,
    onClickSeeAll: (String, List<Long>) -> Unit,
    onClickSongHolder: (List<Song>, Int) -> Unit,
    onClickSongMenu: (Song) -> Unit,
    onClickAlbumHolder: (Long) -> Unit,
    onClickAlbumMenu: (Album) -> Unit,
    onClickMenu: (Artist) -> Unit,
    onClickShuffle: (List<Song>) -> Unit,
    onTerminate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isVisibleFAB by remember { mutableStateOf(false) }
    val coordinatorData = remember { CoordinatorData.Artist(artist.artist, artist.artist, artist.artwork) }

    LaunchedEffect(artist) {
        isVisibleFAB = true
    }

    Box(modifier) {
        CoordinatorScaffold(
            modifier = Modifier.fillMaxSize(),
            data = coordinatorData,
            onClickNavigateUp = onTerminate,
            onClickMenu = { onClickMenu.invoke(artist) },
        ) {
            item {
                SongDetailHeader(
                    modifier = Modifier.fillMaxWidth(),
                    onClickSeeAll = { onClickSeeAll.invoke(artist.artist, artist.songs.map { it.id }) },
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

            item {
                Spacer(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                )
            }

            item {
                AlbumDetailHeader(
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    contentPadding = PaddingValues(horizontal = 8.dp),
                ) {
                    items(
                        items = artist.albums,
                        key = { it.albumId },
                    ) { album ->
                        AlbumHolder(
                            modifier = Modifier.width(172.dp),
                            album = album,
                            onClickHolder = { onClickAlbumHolder.invoke(album.albumId) },
                            onClickPlay = { onClickSongHolder.invoke(album.songs, 0) },
                            onClickMenu = { onClickAlbumMenu.invoke(album) },
                        )
                    }
                }
            }

            item {
                Spacer(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                )
            }

            artistDetail?.biography?.also {
                item {
                    ArtistDetailBiography(
                        modifier = Modifier.fillMaxWidth(),
                        biography = it,
                    )
                }
            }

            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(128.dp),
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd),
            visible = isVisibleFAB,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
        ) {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                onClick = { onClickShuffle.invoke(artist.songs) },
            ) {
                Icon(
                    imageVector = Icons.Default.Shuffle,
                    contentDescription = null,
                )
            }
        }
    }
}
