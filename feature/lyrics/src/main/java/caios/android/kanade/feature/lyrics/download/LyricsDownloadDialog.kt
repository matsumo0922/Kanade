package caios.android.kanade.feature.lyrics.download

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.common.network.util.ToastUtil
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.ui.AsyncLoadContents

@Composable
internal fun LyricsDownloadRoute(
    songId: Long,
    terminate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LyricsDownloadViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(songId) {
        viewModel.fetch(songId)
    }

    AsyncLoadContents(
        modifier = modifier,
        screenState = screenState,
        retryAction = { terminate.invoke() },
        cornerShape = RoundedCornerShape(16.dp),
    ) { uiState ->
        LaunchedEffect(uiState.state) {
            if (uiState.state == LyricsDownloadUiState.State.Downloaded) {
                terminate.invoke()
            }
        }

        LyricsDownloadDialog(
            song = uiState.song,
            token = uiState.token,
            state = uiState.state,
            onClickTokenWebPage = { context.startActivity(Intent(Intent.ACTION_VIEW, it.toUri())) },
            onClickDownload = viewModel::download,
            onTerminate = terminate,
        )
    }
}

@Composable
private fun LyricsDownloadDialog(
    song: Song?,
    token: String?,
    state: LyricsDownloadUiState.State,
    onClickTokenWebPage: (String) -> Unit,
    onClickDownload: (Song, Boolean, String?) -> Unit,
    onTerminate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var isUseMusixmatch by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var artist by remember { mutableStateOf("") }
    var apikey by remember { mutableStateOf("") }
    var isMusixmatchError by remember { mutableStateOf(false) }
    var isKugouError by remember { mutableStateOf(false) }

    LaunchedEffect(song, token) {
        song?.title?.let { title = it }
        song?.artist?.let { artist = it }
        token?.let { apikey = it }
    }

    LaunchedEffect(title, artist, token, isUseMusixmatch) {
        isMusixmatchError = false
        isKugouError = false
    }

    LaunchedEffect(state) {
        if (state == LyricsDownloadUiState.State.Error) {
            if (isUseMusixmatch) {
                isMusixmatchError = true
            } else {
                ToastUtil.show(context, R.string.lyrics_download_dialog_kugou_error)
                isKugouError = true
            }
        }
    }

    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.lyrics_download_dialog_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        OutlinedTextField(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            value = title,
            onValueChange = { title = it },
            label = { Text(stringResource(R.string.music_title)) },
            singleLine = true,
            isError = isKugouError,
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = artist,
            onValueChange = { artist = it },
            label = { Text(stringResource(R.string.music_artist)) },
            singleLine = true,
            isError = isKugouError,
        )

        if (isUseMusixmatch) {
            val url = "https://spicetify.app/docs/faq/#:~:text=It%20should%20look%20like%20this"
            val annotatedString = buildAnnotatedString {
                withStyle(MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant).toSpanStyle()) {
                    append(stringResource(R.string.lyrics_download_dialog_musixmatch_token_hint1) + " ")
                }

                pushStringAnnotation(
                    tag = "link",
                    annotation = url,
                )

                withStyle(MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary).toSpanStyle()) {
                    append(stringResource(R.string.lyrics_download_dialog_musixmatch_token_hint2))
                }

                pop()

                withStyle(MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant).toSpanStyle()) {
                    append(" " + stringResource(R.string.lyrics_download_dialog_musixmatch_token_hint3))
                }
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = apikey,
                onValueChange = { apikey = it },
                label = { Text(stringResource(R.string.lyrics_download_dialog_musixmatch_token)) },
                singleLine = true,
                isError = isMusixmatchError,
                supportingText = {
                    if (isMusixmatchError) {
                        Text(stringResource(R.string.lyrics_download_dialog_musixmatch_error))
                    }
                },
            )

            ClickableText(
                modifier = Modifier.fillMaxWidth(),
                text = annotatedString,
                style = MaterialTheme.typography.bodySmall,
                onClick = {
                    annotatedString.getStringAnnotations("link", it, it).firstOrNull()?.let { annotation ->
                        onClickTokenWebPage.invoke(url)
                    }
                },
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Checkbox(
                checked = isUseMusixmatch,
                onCheckedChange = { isUseMusixmatch = it },
            )

            Text(
                text = stringResource(R.string.lyrics_download_dialog_musixmatch_label),
                style = MaterialTheme.typography.bodyMedium,
            )
        }

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
                onClick = { song?.let { onClickDownload.invoke(it, isUseMusixmatch, apikey) } },
                enabled = (!isUseMusixmatch || apikey.isNotBlank()) && !isMusixmatchError && !isKugouError,
            ) {
                Text(
                    text = stringResource(R.string.common_ok),
                    style = MaterialTheme.typography.labelMedium,
                    color = if ((!isUseMusixmatch || apikey.isNotBlank()) && !isMusixmatchError && !isKugouError) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LyricsDownloadDialogPreview() {
    KanadeBackground {
        LyricsDownloadDialog(
            song = Song.dummy(),
            token = "729cnajc9amca9nco",
            state = LyricsDownloadUiState.State.Error,
            onClickTokenWebPage = { },
            onClickDownload = { _, _, _ -> },
            onTerminate = { },
        )
    }
}
