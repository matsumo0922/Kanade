package caios.android.kanade.core.ui.music

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import caios.android.kanade.core.model.music.Artwork
import caios.android.kanade.core.model.music.Song
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Suppress("ModifierReused")
@Composable
fun MultiArtwork(
    songs: ImmutableList<Song>,
    modifier: Modifier = Modifier,
) {
    val artworks = songs.distinctBy { it.album }.map { it.artwork }
    val a1 = artworks.elementAtOrNull(0)
    val a2 = artworks.elementAtOrNull(1)
    val a3 = artworks.elementAtOrNull(2)
    val a4 = artworks.elementAtOrNull(3)

    if (a1 == null) {
        Artwork(
            modifier = modifier,
            artwork = Artwork.Unknown,
        )

        return
    }

    ConstraintLayout(modifier.aspectRatio(1f)) {
        val (artwork1, artwork2, artwork3, artwork4) = createRefs()

        Artwork(
            modifier = Modifier.constrainAs(artwork1) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(artwork2.start)
                bottom.linkTo(artwork3.top)

                height = Dimension.fillToConstraints
                width = Dimension.fillToConstraints
            },
            artwork = a1,
            isLockAspect = false,
        )

        if (a2 != null) {
            Artwork(
                modifier = Modifier.constrainAs(artwork2) {
                    start.linkTo(artwork1.end)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(artwork4.top)

                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                },
                artwork = a2,
                isLockAspect = false,
            )
        } else {
            Box(
                Modifier.constrainAs(artwork2) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
            )
        }

        if (a3 != null) {
            Artwork(
                modifier = Modifier.constrainAs(artwork3) {
                    start.linkTo(parent.start)
                    top.linkTo(artwork1.bottom)
                    end.linkTo(artwork4.start)
                    bottom.linkTo(parent.bottom)

                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                },
                artwork = a3,
                isLockAspect = false,
            )
        } else {
            Box(
                Modifier.constrainAs(artwork3) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                },
            )
        }

        if (a4 != null) {
            Artwork(
                modifier = Modifier.constrainAs(artwork4) {
                    start.linkTo(artwork3.end)
                    top.linkTo(artwork2.bottom)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)

                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                },
                artwork = a4,
                isLockAspect = false,
            )
        } else {
            Box(
                Modifier.constrainAs(artwork4) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            )
        }
    }
}

@Preview
@Composable
private fun MultiArtworkPreview1() {
    MultiArtwork(
        modifier = Modifier.size(512.dp),
        songs = Song.dummies(1).toImmutableList(),
    )
}

@Preview
@Composable
private fun MultiArtworkPreview2() {
    MultiArtwork(
        modifier = Modifier.size(512.dp),
        songs = Song.dummies(2).toImmutableList(),
    )
}

@Preview
@Composable
private fun MultiArtworkPreview3() {
    MultiArtwork(
        modifier = Modifier.size(512.dp),
        songs = Song.dummies(3).toImmutableList(),
    )
}

@Preview
@Composable
private fun MultiArtworkPreview4() {
    MultiArtwork(
        modifier = Modifier.size(512.dp),
        songs = Song.dummies(4).toImmutableList(),
    )
}
