package caios.android.kanade.feature.menu.delete

import android.app.Activity
import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import caios.android.kanade.core.common.network.util.ToastUtil
import caios.android.kanade.core.design.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
internal fun DeleteSongDialog(
    songIds: ImmutableList<Long>,
    terminate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DeleteSongViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    scope.launch {
                        viewModel.fetchSong()
                        terminate.invoke()
                        ToastUtil.show(context, R.string.song_delete_toast)
                    }
                } else {
                    try {
                        scope.launch {
                            val contentResolver = context.contentResolver
                            val uris = songIds.map { ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, it) }

                            for (uri in uris) contentResolver.delete(uri, null, null)

                            viewModel.fetchSong()
                            terminate.invoke()
                            ToastUtil.show(context, R.string.song_delete_toast)
                        }
                    } catch (e: Throwable) {
                        Timber.e(e)
                        ToastUtil.show(context, R.string.song_delete_error_toast)
                    }
                }
            }
        },
    )

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.song_delete_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.song_delete_description, songIds.size),
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
                onClick = { terminate.invoke() },
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
                    val contentResolver = context.contentResolver
                    val uris = songIds.map { ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, it) }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        val pendingIntent = MediaStore.createDeleteRequest(contentResolver, uris)
                        launcher.launch(IntentSenderRequest.Builder(pendingIntent.intentSender).build())
                    } else {
                        kotlin.runCatching {
                            for (uri in uris) contentResolver.delete(uri, null, null)
                        }.fold(
                            onSuccess = {
                                scope.launch {
                                    viewModel.fetchSong()
                                    terminate.invoke()
                                    ToastUtil.show(context, R.string.song_delete_toast)
                                }
                            },
                            onFailure = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && it is RecoverableSecurityException) {
                                    launcher.launch(IntentSenderRequest.Builder(it.userAction.actionIntent.intentSender).build())
                                } else {
                                    ToastUtil.show(context, R.string.song_delete_error_toast)
                                }
                            },
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.errorContainer),
            ) {
                Text(
                    text = stringResource(R.string.common_delete),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        }
    }
}
