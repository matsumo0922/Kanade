package caios.android.kanade.core.ui.controller.items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.model.music.Artwork
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.ui.music.Artwork

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun MainControllerArtworkSection(
    songs: List<Song>,
    index: Int,
    onSwipeArtwork: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dummyPagerCount = Int.MAX_VALUE
    val queueSize = if (songs.isEmpty()) 1 else songs.size
    val pagerState = rememberPagerState(initialPage = (dummyPagerCount / queueSize / 2) * songs.size)

    LaunchedEffect(index) {
        if (songs.isEmpty()) return@LaunchedEffect

        val currentPage = pagerState.currentPage % songs.size
        val targetPage = if (index >= currentPage) {
            currentPage - index
        } else {
            songs.size - (currentPage - index)
        }

        pagerState.scrollToPage(pagerState.currentPage - targetPage)
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect {
            if (index != 0) {
                onSwipeArtwork.invoke(it)
            }
        }
    }

    HorizontalPager(
        modifier = modifier,
        pageCount = dummyPagerCount,
        state = pagerState,
    ) { dummyIndex ->
        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .aspectRatio(1f),
            shape = RoundedCornerShape(16.dp),
        ) {
            Box(Modifier.fillMaxSize()) {
                Artwork(
                    modifier = Modifier.fillMaxSize(),
                    artwork = if (songs.isNotEmpty()) songs[dummyIndex % songs.size].artwork else Artwork.Unknown,
                )

                Text(
                    modifier = Modifier.padding(8.dp),
                    text = if (songs.isNotEmpty()) "${dummyIndex % songs.size}, $dummyIndex [${songs[dummyIndex % songs.size].title}]" else "Unknown",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainControllerArtworkSectionPreview() {
    KanadeBackground(Modifier.wrapContentSize()) {
        MainControllerArtworkSection(
            songs = Song.dummies(10),
            index = 3,
            onSwipeArtwork = { },
        )
    }
}
