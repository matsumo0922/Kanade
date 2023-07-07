package caios.android.kanade.core.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.design.theme.applyTonalElevation
import caios.android.kanade.core.design.theme.bold
import caios.android.kanade.core.design.theme.center
import caios.android.kanade.core.model.music.Artwork
import caios.android.kanade.core.ui.music.Artwork
import caios.android.kanade.core.ui.music.MultiArtwork
import caios.android.kanade.core.ui.util.marquee
import kotlinx.collections.immutable.toImmutableList

@Composable
fun CoordinatorScaffold(
    data: CoordinatorData,
    onClickNavigateUp: () -> Unit,
    onClickMenu: () -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    color: Color = MaterialTheme.colorScheme.surface,
    content: LazyListScope.() -> Unit,
) {
    var appBarAlpha by remember { mutableFloatStateOf(0f) }
    var topSectionHeight by remember { mutableIntStateOf(100) }

    Box(modifier.background(color)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
        ) {
            item {
                when (data) {
                    is CoordinatorData.Album -> {
                        AlbumArtworkSection(
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .onGloballyPositioned { topSectionHeight = it.size.height },
                            data = data,
                            color = color,
                            alpha = 1f - appBarAlpha,
                        )
                    }
                    is CoordinatorData.Artist -> {
                        ArtistArtworkSection(
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .onGloballyPositioned { topSectionHeight = it.size.height },
                            data = data,
                            color = color,
                            alpha = 1f - appBarAlpha,
                        )
                    }
                    is CoordinatorData.Playlist -> {
                        PlaylistArtworkSection(
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .onGloballyPositioned { topSectionHeight = it.size.height },
                            data = data,
                            color = color,
                            alpha = 1f - appBarAlpha,
                        )
                    }
                }
            }

            content(this)
        }

        CoordinatorToolBar(
            modifier = Modifier.fillMaxWidth(),
            title = when (data) {
                is CoordinatorData.Album -> data.title
                is CoordinatorData.Artist -> data.title
                is CoordinatorData.Playlist -> data.title
            },
            color = MaterialTheme.colorScheme.applyTonalElevation(
                backgroundColor = MaterialTheme.colorScheme.surface,
                elevation = 3.dp,
            ),
            backgroundAlpha = appBarAlpha,
            onClickNavigateUp = onClickNavigateUp,
            onClickMenu = onClickMenu,
        )
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }.collect {
            val index = listState.firstVisibleItemIndex
            val disableArea = topSectionHeight * 0.4
            val alpha = if (index == 0) (listState.firstVisibleItemScrollOffset.toDouble() - disableArea) / (topSectionHeight - disableArea) else 1

            appBarAlpha = (alpha.toFloat() * 3).coerceIn(0f..1f)
        }
    }
}

@Composable
private fun AlbumArtworkSection(
    data: CoordinatorData.Album,
    alpha: Float,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val titleStyle = MaterialTheme.typography.headlineSmall
    val summaryStyle = MaterialTheme.typography.bodyMedium

    Box(modifier) {
        Artwork(
            modifier = Modifier
                .blur(16.dp)
                .fillMaxWidth(),
            artwork = data.artwork,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .align(Alignment.TopCenter)
                .background(Brush.verticalGradient(listOf(Color.Transparent, color))),
        )

        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .size(224.dp)
                .aspectRatio(1f)
                .alpha(alpha),
            shape = RoundedCornerShape(8.dp),
            elevation = 4.dp,
        ) {
            Artwork(
                modifier = Modifier.fillMaxWidth(),
                artwork = data.artwork,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .marquee()
                    .alpha(alpha),
                text = data.title,
                style = titleStyle.center().bold(),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .alpha(alpha),
                text = data.summary,
                style = summaryStyle.center(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun ArtistArtworkSection(
    data: CoordinatorData.Artist,
    alpha: Float,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val titleStyle = MaterialTheme.typography.headlineSmall
    val summaryStyle = MaterialTheme.typography.bodyMedium

    Box(modifier) {
        Artwork(
            modifier = Modifier.fillMaxWidth(),
            artwork = data.artwork,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .align(Alignment.TopCenter)
                .background(Brush.verticalGradient(listOf(Color.Transparent, color))),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .marquee()
                    .alpha(alpha),
                text = data.title,
                style = titleStyle.center().bold(),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .alpha(alpha),
                text = data.summary,
                style = summaryStyle.center(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun PlaylistArtworkSection(
    data: CoordinatorData.Playlist,
    alpha: Float,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val titleStyle = MaterialTheme.typography.headlineSmall
    val summaryStyle = MaterialTheme.typography.bodyMedium

    Box(modifier) {
        MultiArtwork(
            modifier = Modifier.fillMaxWidth(),
            artworks = data.artworks.toImmutableList(),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .align(Alignment.TopCenter)
                .background(Brush.verticalGradient(listOf(Color.Transparent, color))),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .marquee()
                    .alpha(alpha),
                text = data.title,
                style = titleStyle.center().bold(),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .alpha(alpha),
                text = data.summary,
                style = summaryStyle.center(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun CoordinatorToolBar(
    title: String,
    color: Color,
    backgroundAlpha: Float,
    onClickNavigateUp: () -> Unit,
    onClickMenu: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = color.copy(backgroundAlpha),
        contentColor = MaterialTheme.colorScheme.onSurface,
        elevation = if (backgroundAlpha > 0.9f) 4.dp else 0.dp,
    ) {
        TopAppBar(
            modifier = Modifier.statusBarsPadding(),
            backgroundColor = Color.Transparent,
            contentPadding = PaddingValues(vertical = 4.dp),
            elevation = 0.dp,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                    Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(50))
                            .clickable { onClickNavigateUp() }
                            .padding(8.dp),
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                    )
                }

                ProvideTextStyle(value = androidx.compose.material.MaterialTheme.typography.h6) {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                        Text(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .weight(1f)
                                .alpha(backgroundAlpha),
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }

                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                    Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(50))
                            .clickable { onClickMenu.invoke() }
                            .padding(8.dp),
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun FillSectionPreview1() {
    KanadeBackground {
        AlbumArtworkSection(
            data = CoordinatorData.Album(
                title = "UNDERTALE",
                summary = "toby fox",
                artwork = Artwork.Internal("UNDERTALE"),
            ),
            alpha = 1f,
            color = Color.Black,
        )
    }
}

@Preview
@Composable
private fun FillSectionPreview2() {
    KanadeBackground {
        ArtistArtworkSection(
            data = CoordinatorData.Artist(
                title = "toby fox",
                summary = "UNDERTALE",
                artwork = Artwork.Internal("UNDERTALE"),
            ),
            alpha = 1f,
            color = Color.Black,
        )
    }
}

@Preview
@Composable
private fun FillSectionPreview3() {
    KanadeBackground {
        PlaylistArtworkSection(
            data = CoordinatorData.Playlist(
                title = "toby fox",
                summary = "UNDERTALE",
                artworks = Artwork.dummies(),
            ),
            alpha = 1f,
            color = Color.Black,
        )
    }
}
