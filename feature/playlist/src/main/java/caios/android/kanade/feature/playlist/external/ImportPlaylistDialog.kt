package caios.android.kanade.feature.playlist.external

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.common.network.util.ToastUtil
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.music.ExternalPlaylist
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.ui.AsyncLoadContentsWithoutAnimation
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun ImportPlaylistRoute(
    terminate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ImportPlaylistViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    AsyncLoadContentsWithoutAnimation(
        modifier = modifier,
        screenState = screenState,
        cornerShape = RoundedCornerShape(16.dp),
        retryAction = terminate,
    ) {
        ImportPlaylistDialog(
            playlists = it.playlists.toImmutableList(),
            externalPlaylists = it.externalPlaylists.toImmutableList(),
            onClickImport = viewModel::import,
            onTerminate = terminate,
        )
    }
}

@Composable
private fun ImportPlaylistDialog(
    playlists: ImmutableList<Playlist>,
    externalPlaylists: ImmutableList<ExternalPlaylist>,
    onClickImport: (ExternalPlaylist) -> Unit,
    onTerminate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 24.dp,
                    start = 24.dp,
                    end = 24.dp,
                ),
            text = stringResource(R.string.playlist_import_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            text = stringResource(R.string.playlist_import_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        LazyColumn(
            modifier = Modifier
                .padding(bottom = 24.dp)
                .fillMaxWidth(),
        ) {
            items(
                items = externalPlaylists,
                key = { it.id },
            ) { externalPlaylist ->
                Row(
                    Modifier
                        .clickable {
                            if (playlists.find { it.name == externalPlaylist.name } == null) {
                                onClickImport.invoke(externalPlaylist)
                                onTerminate.invoke()

                                ToastUtil.show(context, R.string.playlist_import_toast)
                            } else {
                                ToastUtil.show(context, R.string.playlist_import_toast_error)
                            }
                        }
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = externalPlaylist.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Text(
                        text = stringResource(R.string.unit_song, externalPlaylist.songIds.size),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}
