package caios.android.kanade.feature.tag

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.common.network.util.ToastUtil
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.music.Tag
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.core.ui.view.KanadeTopAppBar
import caios.android.kanade.feature.tag.items.TagEditSongSection
import kotlinx.coroutines.launch

@Composable
internal fun TagEditRoute(
    songId: Long,
    terminate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TagEditViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(songId) {
        viewModel.fetch(songId)
    }

    AsyncLoadContents(
        modifier = modifier,
        screenState = screenState,
        retryAction = terminate,
    ) {
        TagEditScreen(
            song = it.song,
            onClickSave = viewModel::edit,
            onTerminate = terminate,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TagEditScreen(
    song: Song,
    onClickSave: suspend (Context, Song, Tag) -> Boolean,
    onTerminate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state = rememberTopAppBarState()
    val behavior = TopAppBarDefaults.pinnedScrollBehavior(state)

    var title by remember { mutableStateOf(song.title) }
    var artist by remember { mutableStateOf(song.artist) }
    var album by remember { mutableStateOf(song.album) }
    var composer by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var year by remember { mutableStateOf(song.year.toString()) }
    var trackNumber by remember { mutableStateOf("") }
    var trackTotal by remember { mutableStateOf("") }
    var discNumber by remember { mutableStateOf("") }
    var discTotal by remember { mutableStateOf("") }

    var isVisibleFAB by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {
            if (it.resultCode == Activity.RESULT_OK) {
                scope.launch {
                    val result = onClickSave.invoke(
                        context,
                        song,
                        Tag(
                            title = title,
                            artist = artist,
                            album = album,
                            composer = composer,
                            genre = genre,
                            year = year,
                            track = trackNumber,
                            trackTotal = trackTotal,
                            disc = discNumber,
                            discTotal = discTotal,
                        ),
                    )

                    ToastUtil.show(context, if (result) R.string.tag_edit_toast_success else R.string.tag_edit_toast_failure)
                }
            }
        },
    )

    LaunchedEffect(true) {
        isVisibleFAB = true
    }

    Box(modifier) {
        Scaffold(
            modifier = Modifier.nestedScroll(behavior.nestedScrollConnection),
            topBar = {
                KanadeTopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.lyrics_edit_title),
                    behavior = behavior,
                    onTerminate = { onTerminate.invoke() },
                )
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                TagEditSongSection(
                    modifier = Modifier.fillMaxWidth(),
                    song = song,
                )

                OutlinedTextField(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.tag_edit_title)) },
                    singleLine = true,
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = artist,
                    onValueChange = { artist = it },
                    label = { Text(stringResource(R.string.tag_edit_artist)) },
                    singleLine = true,
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = album,
                    onValueChange = { album = it },
                    label = { Text(stringResource(R.string.tag_edit_album)) },
                    singleLine = true,
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = composer,
                    onValueChange = { composer = it },
                    label = { Text(stringResource(R.string.tag_edit_composer)) },
                    singleLine = true,
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = genre,
                    onValueChange = { genre = it },
                    label = { Text(stringResource(R.string.tag_edit_genre)) },
                    singleLine = true,
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = year,
                    onValueChange = { year = it },
                    label = { Text(stringResource(R.string.tag_edit_year)) },
                    singleLine = true,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = trackNumber,
                            onValueChange = { trackNumber = it },
                            label = { Text(stringResource(R.string.tag_edit_track)) },
                            singleLine = true,
                        )

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = trackTotal,
                            onValueChange = { trackTotal = it },
                            label = { Text(stringResource(R.string.tag_edit_track_total)) },
                            singleLine = true,
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = discNumber,
                            onValueChange = { discNumber = it },
                            label = { Text(stringResource(R.string.tag_edit_disc)) },
                            singleLine = true,
                        )

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = discTotal,
                            onValueChange = { discTotal = it },
                            label = { Text(stringResource(R.string.tag_edit_disc_total)) },
                            singleLine = true,
                        )
                    }
                }

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
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        val uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, song.id)
                        val pendingIntent = MediaStore.createWriteRequest(context.contentResolver, listOf(uri))

                        launcher.launch(IntentSenderRequest.Builder(pendingIntent.intentSender).build())
                    } else {
                        scope.launch {
                            val result = onClickSave.invoke(
                                context,
                                song,
                                Tag(
                                    title = title,
                                    artist = artist,
                                    album = album,
                                    composer = composer,
                                    genre = genre,
                                    year = year,
                                    track = trackNumber,
                                    trackTotal = trackTotal,
                                    disc = discNumber,
                                    discTotal = discTotal,
                                ),
                            )

                            ToastUtil.show(context, if (result) R.string.tag_edit_toast_success else R.string.tag_edit_toast_failure)
                        }
                    }
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                )
            }
        }
    }
}
