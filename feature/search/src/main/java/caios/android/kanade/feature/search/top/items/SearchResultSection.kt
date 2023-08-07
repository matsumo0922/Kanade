package caios.android.kanade.feature.search.top.items

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.feature.search.top.SearchUiState

@Composable
internal fun SearchResultSection(
    uiState: SearchUiState,
    onClickSong: (List<Song>, Int) -> Unit,
    onClickArtist: (Artist) -> Unit,
    onClickAlbum: (Album) -> Unit,
    onClickPlaylist: (Playlist) -> Unit,
    onClickSongMenu: (Song) -> Unit,
    onClickArtistMenu: (Artist) -> Unit,
    onClickAlbumMenu: (Album) -> Unit,
    onClickPlaylistMenu: (Playlist) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 8.dp),
    ) {
        if (uiState.resultSongs.isNotEmpty()) {
            item {
                SearchHeaderItem(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    type = HeaderItemType.Song,
                    size = uiState.resultSongs.size,
                )
            }

            itemsIndexed(
                items = uiState.resultSongs,
                key = { _, song -> song.id },
            ) { index, song ->
                SearchSongHolder(
                    modifier = Modifier.fillMaxWidth(),
                    song = song,
                    range = uiState.resultSongsRangeMap[song.id] ?: 0..0,
                    onClickHolder = { onClickSong.invoke(uiState.resultSongs, index) },
                    onClickMenu = onClickSongMenu,
                )
            }
        }

        if (uiState.resultArtists.isNotEmpty()) {
            item {
                SearchHeaderItem(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    type = HeaderItemType.Artist,
                    size = uiState.resultArtists.size,
                )
            }

            items(
                items = uiState.resultArtists,
                key = { artist -> artist.artistId },
            ) {
                SearchArtistHolder(
                    modifier = Modifier.fillMaxWidth(),
                    artist = it,
                    range = uiState.resultArtistsRangeMap[it.artistId] ?: 0..0,
                    onClickHolder = { onClickArtist.invoke(it) },
                    onClickMenu = onClickArtistMenu,
                )
            }
        }

        if (uiState.resultAlbums.isNotEmpty()) {
            item {
                SearchHeaderItem(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    type = HeaderItemType.Album,
                    size = uiState.resultAlbums.size,
                )
            }

            items(
                items = uiState.resultAlbums,
                key = { album -> album.albumId },
            ) {
                SearchAlbumHolder(
                    modifier = Modifier.fillMaxWidth(),
                    album = it,
                    range = uiState.resultAlbumsRangeMap[it.albumId] ?: 0..0,
                    onClickHolder = { onClickAlbum.invoke(it) },
                    onClickMenu = onClickAlbumMenu,
                )
            }
        }

        if (uiState.resultPlaylists.isNotEmpty()) {
            item {
                SearchHeaderItem(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    type = HeaderItemType.Playlist,
                    size = uiState.resultPlaylists.size,
                )
            }

            items(
                items = uiState.resultPlaylists,
                key = { playlist -> playlist.id },
            ) {
                SearchPlaylistHolder(
                    modifier = Modifier.fillMaxWidth(),
                    playlist = it,
                    range = uiState.resultPlaylistsRangeMap[it.id] ?: 0..0,
                    onClickHolder = { onClickPlaylist.invoke(it) },
                    onClickMenu = onClickPlaylistMenu,
                )
            }
        }
    }
}
