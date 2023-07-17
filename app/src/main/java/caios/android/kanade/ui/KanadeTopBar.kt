package caios.android.kanade.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.feature.search.SearchRoute
import caios.android.kanade.feature.search.SearchViewModel

@Suppress("ViewModelInjection")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationGraphicsApi::class)
@Composable
fun KanadeTopBar(
    active: Boolean,
    yOffset: Dp,
    onChangeActive: (Boolean) -> Unit,
    onClickDrawerMenu: () -> Unit,
    navigateToArtistDetail: (Long) -> Unit,
    navigateToAlbumDetail: (Long) -> Unit,
    navigateToPlaylistDetail: (Long) -> Unit,
    navigateToSongMenu: (Song) -> Unit,
    navigateToArtistMenu: (Artist) -> Unit,
    navigateToAlbumMenu: (Album) -> Unit,
    navigateToPlaylistMenu: (Playlist) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val searchViewModel = hiltViewModel<SearchViewModel>()

    val image = AnimatedImageVector.animatedVectorResource(R.drawable.av_drawer_to_arrow)
    var atEnd by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }

    val toolBarTopPadding = with(density) { TopAppBarDefaults.windowInsets.getTop(density).toFloat().toDp() }
    val toolbarPadding by animateDpAsState(
        targetValue = if (active) 0.dp else toolBarTopPadding,
        label = "toolbarPadding",
        animationSpec = tween(400),
    )

    LaunchedEffect(active) {
        atEnd = active
        query = ""
    }

    LaunchedEffect(query) {
        searchViewModel.search(listOf(query))
    }

    Column(
        modifier = modifier
            .offset(y = yOffset)
            .padding(vertical = toolbarPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(toolBarTopPadding)
                .alpha(1f - toolbarPadding / toolBarTopPadding)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        )

        SearchBar(
            modifier = Modifier,
            query = query,
            onQueryChange = { query = it },
            onSearch = { },
            active = active,
            onActiveChange = onChangeActive,
            colors = SearchBarDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
            windowInsets = WindowInsets(0, 0, 0, 0),
            placeholder = { Text(stringResource(R.string.search_title)) },
            leadingIcon = {
                Icon(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .padding(6.dp)
                        .clickable { onClickDrawerMenu.invoke() },
                    painter = rememberAnimatedVectorPainter(image, atEnd),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
        ) {
            SearchRoute(
                modifier = Modifier.fillMaxWidth(),
                viewModel = searchViewModel,
                navigateToArtistDetail = navigateToArtistDetail,
                navigateToAlbumDetail = navigateToAlbumDetail,
                navigateToPlaylistDetail = navigateToPlaylistDetail,
                navigateToSongMenu = navigateToSongMenu,
                navigateToArtistMenu = navigateToArtistMenu,
                navigateToAlbumMenu = navigateToAlbumMenu,
                navigateToPlaylistMenu = navigateToPlaylistMenu,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 1)
@Composable
private fun Preview() {
    KanadeTopBar(
        active = false,
        yOffset = 0.dp,
        onChangeActive = { },
        onClickDrawerMenu = { },
        navigateToArtistDetail = { },
        navigateToAlbumDetail = { },
        navigateToPlaylistDetail = { },
        navigateToSongMenu = { },
        navigateToArtistMenu = { },
        navigateToAlbumMenu = { },
        navigateToPlaylistMenu = { },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SearchBarSample() {
    var text by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter),
            query = text,
            onQueryChange = { text = it },
            onSearch = { active = false },
            active = active,
            onActiveChange = {
                active = it
            },
            placeholder = { Text("Hinted search text") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = null) },
        ) {
            repeat(4) { idx ->
                val resultText = "Suggestion $idx"
                ListItem(
                    headlineContent = { Text(resultText) },
                    supportingContent = { Text("Additional info") },
                    leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                    modifier = Modifier
                        .clickable {
                            text = resultText
                            active = false
                        }
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, top = 72.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val list = List(100) { "Text $it" }
            items(count = list.size) {
                Text(
                    list[it],
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
            }
        }
    }
}
