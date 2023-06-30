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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.model.music.Artwork
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.ui.music.Artwork
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun MainControllerArtworkSection(
    songs: ImmutableList<Song>,
    index: Int,
    onSwipeArtwork: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dummyPagerCount = songs.size + 2
    val pagerState = rememberPagerState(initialPage = index + 1) { dummyPagerCount }
    var lastPlayedIndex by remember { mutableIntStateOf(index) }

    LaunchedEffect(index) {
        snapshotFlow { index }.collect {
            lastPlayedIndex = it
            pagerState.animateScrollToPage(it + 1)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect {
            if (songs.isEmpty()) return@collect

            val realIndex = when (it) {
                0 -> {
                    pagerState.scrollToPage(dummyPagerCount - 2)
                    dummyPagerCount - 3
                }
                dummyPagerCount - 1 -> {
                    pagerState.scrollToPage(1)
                    0
                }
                else -> {
                    it - 1
                }
            }

            if (realIndex != lastPlayedIndex && realIndex != index) {
                lastPlayedIndex = realIndex
                onSwipeArtwork.invoke(realIndex)
            }
        }
    }

    HorizontalPager(
        modifier = modifier,
        state = pagerState,
    ) { dummyIndex ->
        val realIndex = when (dummyIndex) {
            0 -> dummyPagerCount - 3
            dummyPagerCount - 1 -> 0
            else -> dummyIndex - 1
        }

        val song = songs.elementAtOrElse(realIndex) { Song.dummy() }

        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .aspectRatio(1f)
                .graphicsLayer {
                    val offset = ((pagerState.currentPage - dummyIndex) + pagerState.currentPageOffsetFraction).absoluteValue

                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - offset.coerceIn(0f, 1f),
                    )

                    lerp(
                        start = 0.8f,
                        stop = 1f,
                        fraction = 1f - (offset / 2).coerceIn(0f, 1f),
                    ).also {
                        scaleX = it
                        scaleY = it
                    }
                },
            shape = RoundedCornerShape(16.dp),
        ) {
            Box(Modifier.fillMaxSize()) {
                Artwork(
                    modifier = Modifier.fillMaxWidth(),
                    artwork = if (songs.isNotEmpty()) song.artwork else Artwork.Unknown,
                )

                Text(
                    modifier = Modifier.padding(8.dp),
                    text = if (songs.isNotEmpty()) "$realIndex [${song.title}]" else "Unknown",
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
            songs = Song.dummies(10).toImmutableList(),
            index = 3,
            onSwipeArtwork = { },
        )
    }
}
