package caios.android.kanade.feature.playlist.export

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.common.network.util.ToastUtil
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.ui.AsyncNoLoadContents

@Composable
internal fun ExportPlaylistRoute(
    playlistId: Long,
    terminate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExportPlaylistViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(playlistId) {
        viewModel.fetch(playlistId)
    }

    AsyncNoLoadContents(
        modifier = modifier,
        screenState = screenState,
        cornerShape = RoundedCornerShape(16.dp),
    ) {
        ExportPlaylistDialog(
            playlist = it?.playlist,
            onExport = viewModel::export,
            onTerminate = terminate,
        )
    }
}

@Composable
private fun ExportPlaylistDialog(
    playlist: Playlist?,
    onExport: (Playlist) -> Unit,
    onTerminate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.playlist_export_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.playlist_export_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(4.dp),
                onClick = { onTerminate.invoke() },
            ) {
                Text(
                    text = stringResource(R.string.common_cancel),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Button(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(4.dp),
                onClick = {
                    if (playlist != null) {
                        onExport.invoke(playlist)
                        onTerminate.invoke()

                        ToastUtil.show(context, R.string.playlist_export_toast)
                    }
                },
            ) {
                Text(
                    text = stringResource(R.string.playlist_export_action),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}
