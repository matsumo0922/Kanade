package caios.android.kanade.core.ui.music

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.model.music.Artwork
import caios.android.kanade.core.ui.util.extraSize
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.random.Random

@Composable
fun MultiArtwork(
    artworks: ImmutableList<Artwork>,
    modifier: Modifier = Modifier,
    space: Dp = 8.dp,
    isRandom: Boolean = true,
) {
    val useArtworks = remember { getUselessArtworks(artworks.toImmutableList(), isRandom) }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clipToBounds(),
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .extraSize(0.3f, 0.3f)
                .rotate(30f),
            columns = GridCells.Fixed(3),
            userScrollEnabled = false,
        ) {
            items(useArtworks) {
                Card(
                    modifier = Modifier
                        .padding(space / 2)
                        .aspectRatio(1f),
                    shape = RoundedCornerShape(space * 1.5f),
                    elevation = 3.dp,
                ) {
                    Artwork(it, Modifier.fillMaxSize())
                }
            }
        }
    }
}

private fun getUselessArtworks(artworks: ImmutableList<Artwork>, isRandom: Boolean): List<Artwork> {
    fun Random.getRandomIndex(next: Int): Int {
        return if (next == 0) 0 else nextInt(next)
    }

    val random = Random(System.currentTimeMillis())
    val shuffledArtworks = if (isRandom) artworks.shuffled(random) else artworks
    val imageArtworks: MutableList<Artwork> = shuffledArtworks.filter { it !is Artwork.Internal }.toMutableList()
    val internalArtworks: MutableList<Artwork> = shuffledArtworks.filterIsInstance<Artwork.Internal>().toMutableList()
    val resultList = mutableListOf<Artwork>()

    if (internalArtworks.isEmpty()) {
        internalArtworks.add(Artwork.Unknown)
    }

    for (i in 0 until 10) {
        val artwork = imageArtworks.elementAtOrElse(i) { internalArtworks[random.getRandomIndex(internalArtworks.size - 1)] }
        val index = if (i % 2 == 0) 0 else resultList.lastIndex

        resultList.add(index, artwork)
    }

    return resultList
}

@Preview
@Composable
private fun MultiArtworkPreview() {
    KanadeBackground {
        MultiArtwork(
            artworks = Artwork.dummies().toImmutableList(),
        )
    }
}
