package caios.android.kanade.feature.playlist.rename

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import caios.android.kanade.core.ui.AsyncLoadContents
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun RenamePlaylistDialog(
    playlistId: Long,
    terminate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RenamePlaylistViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(playlistId) {
        viewModel.fetch(playlistId)
    }

    AsyncLoadContents(
        modifier = modifier,
        screenState = screenState,
    ) {
        RenamePlaylistDialog(
            playlist = it?.playlist ?: Playlist.dummy(),
            playlists = it?.playlists?.toImmutableList() ?: persistentListOf(),
            onRegister = viewModel::rename,
            onTerminate = terminate,
        )
    }
}

@Composable
private fun RenamePlaylistDialog(
    playlist: Playlist,
    playlists: ImmutableList<Playlist>,
    onRegister: (Playlist, String) -> Unit,
    onTerminate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var isNameError by remember { mutableStateOf(false) }

    LaunchedEffect(name) {
        isNameError = playlists.any { it.name == name }
    }

    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.playlist_rename_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(R.string.playlist_name)) },
            singleLine = true,
            isError = isNameError,
            supportingText = {
                if (isNameError) {
                    Text(stringResource(R.string.playlist_error_existed))
                }
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
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
                    onTerminate.invoke()
                    onRegister.invoke(playlist, name)

                    ToastUtil.show(context, R.string.playlist_created_toast)
                },
            ) {
                Text(
                    text = stringResource(R.string.common_ok),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isNameError) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}
