package caios.android.kanade.feature.download.format

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.common.network.util.StringUtil.toHttpsUrl
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.download.VideoInfo
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.core.ui.view.KanadeTopAppBar
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
                onTerminate = terminate,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DownloadFormatScreen(
    videoInfo: VideoInfo,
    onTerminate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (videoInfo.formats.isNullOrEmpty()) return

    val context = LocalContext.current
    val state = rememberTopAppBarState()
    val behavior = TopAppBarDefaults.pinnedScrollBehavior(state)

    var selectedVideoAudioFormat by remember { mutableIntStateOf(-1) }
    var selectedVideoOnlyFormat by remember { mutableIntStateOf(-1) }
    var selectedAudioOnlyFormat by remember { mutableIntStateOf(-1) }

    val videoOnlyFormats = videoInfo.formats!!.filter { it.vcodec != "none" && it.acodec == "none" }.reversed()
    val audioOnlyFormats = videoInfo.formats!!.filter { it.acodec != "none" && it.vcodec == "none" }.reversed()
    val videoAudioFormats = videoInfo.formats!!.filter { it.acodec != "none" && it.vcodec != "none" }.reversed()

    val formatList: List<VideoInfo.Format> by remember {
        derivedStateOf {
            mutableListOf<VideoInfo.Format>().apply {
                audioOnlyFormats.getOrNull(selectedAudioOnlyFormat)?.let { add(it) }
                videoAudioFormats.getOrNull(selectedVideoAudioFormat)?.let { add(it) }
                videoOnlyFormats.getOrNull(selectedVideoOnlyFormat)?.let { add(it) }
            }
        }
    }

    Scaffold(
        modifier = modifier.nestedScroll(behavior.nestedScrollConnection),
        topBar = {
            KanadeTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.lyrics_edit_title),
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
            columns = GridCells.Adaptive(192.dp),
            contentPadding = PaddingValues(8.dp),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                DownloadFormatVideoPreviewSection(
                    title = videoInfo.title,
                    author = videoInfo.uploader ?: videoInfo.channel,
                    duration = videoInfo.duration?.toLong(),
                    thumbnail = videoInfo.thumbnail?.toHttpsUrl(),
                )
            }
        }
    }
}
