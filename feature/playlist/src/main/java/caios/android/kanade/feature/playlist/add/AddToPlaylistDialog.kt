package caios.android.kanade.feature.playlist.add

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.common.network.util.ToastUtil
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.core.ui.music.GridArtwork
import caios.android.kanade.core.ui.util.marquee
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun AddToPlaylistDialog(
    songIds: ImmutableList<Long>,
    navigateToCreatePlaylist: (List<Long>) -> Unit,
    onTerminate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddToPlaylistViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    AsyncLoadContents(
        modifier = modifier,
        screenState = screenState,
    ) { uiState ->
        if (uiState != null) {
            AddToPlaylistDialog(
                modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                playlists = uiState.playlists.toImmutableList(),
                onCreate = { navigateToCreatePlaylist.invoke(songIds) },
                onRegister = {
                    val songs = songIds.mapNotNull { id -> uiState.songs.find { song -> song.id == id } }
                    viewModel.register(it, songs)
                },
                onTerminate = onTerminate,
            )
        }
    }
}

@Composable
private fun AddToPlaylistDialog(
    playlists: ImmutableList<Playlist>,
    onCreate: () -> Unit,
    onRegister: (Playlist) -> Unit,
    onTerminate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 24.dp,
                    start = 24.dp,
                    end = 24.dp,
                ),
            text = stringResource(R.string.playlist_add_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        LazyColumn(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onTerminate.invoke()
                            onCreate.invoke()
                        }
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Icon(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(8.dp),
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                    )

                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.playlist_create_new),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            items(
                items = playlists,
                key = { it.id },
            ) {
                MiniPlaylistHolder(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            ToastUtil.show(context, R.string.playlist_add_toast)

                            onRegister.invoke(it)
                            onTerminate.invoke()
                        },
                    playlist = it,
                )
            }
        }
    }
}

@Composable
private fun MiniPlaylistHolder(
    playlist: Playlist,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier) {
        val (artwork, title, artist) = createRefs()

        createVerticalChain(
            title.withChainParams(bottomMargin = 2.dp),
            artist.withChainParams(topMargin = 2.dp),
            chainStyle = ChainStyle.Packed,
        )

        Card(
            modifier = Modifier.constrainAs(artwork) {
                top.linkTo(parent.top, 8.dp)
                start.linkTo(parent.start, 16.dp)
                bottom.linkTo(parent.bottom, 8.dp)
            },
            shape = RoundedCornerShape(4.dp),
        ) {
            GridArtwork(
                modifier = Modifier.size(48.dp),
                songs = playlist.songs.toImmutableList(),
            )
        }

        Text(
            modifier = Modifier
                .marquee()
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(artwork.end, 8.dp)
                    end.linkTo(parent.end, 16.dp)
                    bottom.linkTo(artist.top)

                    width = Dimension.fillToConstraints
                },
            text = playlist.name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            modifier = Modifier
                .marquee()
                .constrainAs(artist) {
                    top.linkTo(title.bottom)
                    start.linkTo(artwork.end, 8.dp)
                    end.linkTo(parent.end, 16.dp)
                    bottom.linkTo(parent.bottom)

                    width = Dimension.fillToConstraints
                },
            text = stringResource(R.string.unit_song, playlist.items.size),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
private fun AddToPlaylistDialogPreview() {
    KanadeBackground {
        AddToPlaylistDialog(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            playlists = Playlist.dummies(3).toImmutableList(),
            onCreate = {},
            onRegister = {},
            onTerminate = {},
        )
    }
}
