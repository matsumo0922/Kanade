package caios.android.kanade.feature.information.song

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.music.Volume
import caios.android.kanade.core.ui.AsyncLoadContents

@Composable
internal fun SongInformationRoute(
    songId: Long,
    terminate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SongInformationViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(songId) {
        viewModel.fetch(songId)
    }

    AsyncLoadContents(
        modifier = modifier,
        screenState = screenState,
        cornerShape = RoundedCornerShape(16.dp),
        retryAction = terminate,
    ) {
        if (it != null) {
            SongInformationDialog(
                song = it.song,
                volume = it.volume,
                playCount = it.playCount,
                isFavorite = it.isFavorite,
            )
        }
    }
}

@Composable
private fun SongInformationDialog(
    song: Song,
    volume: Volume?,
    playCount: Int,
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.song_information_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        InformationItem(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.song_information_song_title),
            content = song.title,
        )

        InformationItem(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.song_information_song_artist),
            content = song.artist,
        )

        InformationItem(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.song_information_song_album),
            content = song.album,
        )

        InformationItem(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.song_information_path),
            content = song.data,
        )

        InformationItem(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.song_information_uri),
            content = song.uri.toString(),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                InformationItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.song_information_size),
                    content = stringResource(R.string.song_information_not_analyzed_yet),
                )

                InformationItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.song_information_id),
                    content = song.id.toString(),
                )

                InformationItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.song_information_year),
                    content = song.year.toString(),
                )

                InformationItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.song_information_track),
                    content = song.track.toString(),
                )

                InformationItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.song_information_play_count),
                    content = playCount.toString(),
                )

                InformationItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.song_information_favorite),
                    content = isFavorite.toString(),
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                InformationItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.song_information_duration),
                    content = song.durationString,
                )

                InformationItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.song_information_mime),
                    content = song.mimeType,
                )

                InformationItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.song_information_extension),
                    content = stringResource(R.string.song_information_not_analyzed_yet),
                )

                InformationItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.song_information_encoder),
                    content = volume?.encoder ?: stringResource(R.string.song_information_not_analyzed_yet),
                )

                InformationItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.song_information_max_volume),
                    content = volume?.maxVolume?.let { "$it db" } ?: stringResource(R.string.song_information_not_analyzed_yet),
                )

                InformationItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.song_information_mean_volume),
                    content = volume?.maxVolume?.let { "$it db" } ?: stringResource(R.string.song_information_not_analyzed_yet),
                )
            }
        }
    }
}

@Composable
private fun InformationItem(
    title: String,
    content: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = content,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
