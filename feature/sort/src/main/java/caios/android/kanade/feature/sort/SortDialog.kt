package caios.android.kanade.feature.sort

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.model.player.MusicOrder
import caios.android.kanade.core.model.player.MusicOrderOption
import caios.android.kanade.core.music.MusicViewModel
import caios.android.kanade.core.ui.dialog.showAsButtonSheet
import kotlin.reflect.KClass

@Composable
private fun SortDialog(
    order: MusicOrder,
    onChangedSortOrder: (MusicOrder) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.surface)
            .navigationBarsPadding(),
    ) {
        SortOrderSection(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            order = order.order,
            onClickOrder = { onChangedSortOrder.invoke(order.copy(order = it)) },
        )

        Divider(Modifier.fillMaxWidth())

        SortOptionSection(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            order = order,
            options = when (order.option) {
                is MusicOrderOption.Song -> MusicOrderOption.Song.values()
                is MusicOrderOption.Artist -> MusicOrderOption.Artist.values()
                is MusicOrderOption.Album -> MusicOrderOption.Album.values()
                is MusicOrderOption.Playlist -> MusicOrderOption.Playlist.values()
                else -> throw IllegalArgumentException("Unknown type: ${order.option}")
            }.toList(),
            onClickOption = { onChangedSortOrder.invoke(order.copy(option = it)) },
        )
    }
}

fun Activity.showSortDialog(
    musicViewModel: MusicViewModel,
    userData: UserData?,
    type: KClass<*>,
) {
    showAsButtonSheet(userData, rectCorner = true) { _ ->
        SortDialog(
            modifier = Modifier.fillMaxWidth(),
            order = when (type) {
                MusicOrderOption.Song::class -> musicViewModel.uiState.songOrder
                MusicOrderOption.Artist::class -> musicViewModel.uiState.artistOrder
                MusicOrderOption.Album::class -> musicViewModel.uiState.albumOrder
                MusicOrderOption.Playlist::class -> musicViewModel.uiState.playlistOrder
                else -> throw IllegalArgumentException("Unknown type: $type")
            },
            onChangedSortOrder = musicViewModel::setSortOrder,
        )
    }
}
