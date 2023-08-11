package caios.android.kanade.feature.download.format

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.common.network.util.StringUtil.toHttpsUrl
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.download.VideoInfo
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.core.ui.view.KanadeTopAppBar
import caios.android.kanade.feature.download.format.items.DownloadFormatItem
import caios.android.kanade.feature.download.format.items.DownloadFormatSubTitle
import caios.android.kanade.feature.download.format.items.DownloadFormatVideoPreviewSection

@Composable
internal fun DownloadFormatRoute(
    videoInfo: VideoInfo,
    terminate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DownloadFormatViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(videoInfo) {
        viewModel.fetchVideoInfo(videoInfo)
    }

    AsyncLoadContents(
        modifier = modifier,
        screenState = screenState,
        cornerShape = RoundedCornerShape(16.dp),
    ) {
        if (it != null) {
            DownloadFormatScreen(
                modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                videoInfo = it.videoInfo,
                savePath = it.savePath,
                onUpdateSavePath = viewModel::updateSavePath,
                onTerminate = terminate,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DownloadFormatScreen(
    videoInfo: VideoInfo,
    savePath: String,
    onUpdateSavePath: (String) -> Unit,
    onTerminate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (videoInfo.formats.isNullOrEmpty()) {
        onTerminate.invoke()
        return
    }

    val state = rememberTopAppBarState()
    val behavior = TopAppBarDefaults.pinnedScrollBehavior(state)

    var selectedFormat by remember { mutableStateOf<SelectedItem>(SelectedItem.Suggested) }

    val audioFormats = videoInfo.formats!!.filter { it.acodec != "none" && it.vcodec == "none" }.reversed()
    val videoFormats = videoInfo.formats!!.filter { it.acodec != "none" && it.vcodec != "none" }.reversed()
    val suggestFormat = audioFormats.maxByOrNull { it.tbr ?: 0.0 }

    Scaffold(
        modifier = modifier.nestedScroll(behavior.nestedScrollConnection),
        topBar = {
            KanadeTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.download_format_title),
                behavior = behavior,
                onTerminate = { onTerminate.invoke() },
            )
        },
    ) { paddingValues ->
        LazyVerticalGrid(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            columns = GridCells.Adaptive(144.dp),
            contentPadding = PaddingValues(16.dp),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                DownloadFormatVideoPreviewSection(
                    title = videoInfo.title,
                    author = videoInfo.uploader ?: videoInfo.channel,
                    duration = videoInfo.duration?.toLong(),
                    thumbnail = videoInfo.thumbnail?.toHttpsUrl(),
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                OutlinedTextField(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    value = savePath,
                    onValueChange = onUpdateSavePath,
                    singleLine = true,
                    label = { Text(stringResource(R.string.download_format_save_path)) },
                    trailingIcon = {
                        IconButton(onClick = { onUpdateSavePath.invoke("") }) {
                            Icon(
                                imageVector = Icons.Filled.OpenInNew,
                                contentDescription = null,
                            )
                        }
                    },
                )
            }

            if (suggestFormat != null) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    DownloadFormatSubTitle(
                        modifier = Modifier.padding(
                            top = 16.dp,
                            bottom = 4.dp,
                        ),
                        title = R.string.download_format_suggestion,
                    )
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    DownloadFormatItem(
                        modifier = Modifier.fillMaxWidth(),
                        format = suggestFormat,
                        isSelect = selectedFormat is SelectedItem.Suggested,
                        onSelect = { selectedFormat = SelectedItem.Suggested },
                    )
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                DownloadFormatSubTitle(
                    modifier = Modifier.padding(
                        top = 16.dp,
                        bottom = 4.dp,
                    ),
                    title = R.string.download_format_audio,
                )
            }

            itemsIndexed(audioFormats) { index, format ->
                DownloadFormatItem(
                    modifier = Modifier.fillMaxWidth(),
                    format = format,
                    isSelect = (selectedFormat as? SelectedItem.Audio)?.index == index,
                    onSelect = { selectedFormat = SelectedItem.Audio(index) },
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                DownloadFormatSubTitle(
                    modifier = Modifier.padding(
                        top = 16.dp,
                        bottom = 4.dp,
                    ),
                    title = R.string.download_format_video,
                )
            }

            itemsIndexed(videoFormats) { index, format ->
                DownloadFormatItem(
                    modifier = Modifier.fillMaxWidth(),
                    format = format,
                    isSelect = (selectedFormat as? SelectedItem.Video)?.index == index,
                    onSelect = { selectedFormat = SelectedItem.Video(index) },
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(128.dp),
                )
            }
        }
    }
}

private sealed interface SelectedItem {
    data object Suggested : SelectedItem
    data class Audio(val index: Int) : SelectedItem
    data class Video(val index: Int) : SelectedItem
}
