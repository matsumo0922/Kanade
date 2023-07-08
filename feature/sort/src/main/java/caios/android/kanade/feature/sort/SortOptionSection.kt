package caios.android.kanade.feature.sort

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.player.MusicOrder
import caios.android.kanade.core.model.player.MusicOrderOption

@Composable
internal fun SortOptionSection(
    order: MusicOrder,
    options: List<MusicOrderOption>,
    onClickOption: (MusicOrderOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        for (option in options) {
            SortItemSection(
                modifier = Modifier.fillMaxWidth(),
                titleRes = when (option) {
                    is MusicOrderOption.Song -> when (option) {
                        MusicOrderOption.Song.NAME -> R.string.sort_option_title
                        MusicOrderOption.Song.ARTIST -> R.string.sort_option_artist
                        MusicOrderOption.Song.ALBUM -> R.string.sort_option_album
                        MusicOrderOption.Song.DURATION -> R.string.sort_option_duration
                        MusicOrderOption.Song.YEAR -> R.string.sort_option_year
                        MusicOrderOption.Song.TRACK -> R.string.sort_option_track
                    }
                    is MusicOrderOption.Album -> when (option) {
                        MusicOrderOption.Album.NAME -> R.string.sort_option_title
                        MusicOrderOption.Album.TRACKS -> R.string.sort_option_tracks
                        MusicOrderOption.Album.ARTIST -> R.string.sort_option_artist
                        MusicOrderOption.Album.YEAR -> R.string.sort_option_year
                    }
                    is MusicOrderOption.Artist -> when (option) {
                        MusicOrderOption.Artist.NAME -> R.string.sort_option_title
                        MusicOrderOption.Artist.TRACKS -> R.string.sort_option_tracks
                        MusicOrderOption.Artist.ALBUMS -> R.string.sort_option_albums
                    }
                    is MusicOrderOption.Playlist -> when (option) {
                        MusicOrderOption.Playlist.NAME -> R.string.sort_option_title
                        MusicOrderOption.Playlist.TRACKS -> R.string.sort_option_tracks
                    }
                },
                isSelected = order.option == option,
                onClick = { onClickOption.invoke(option) },
            )
        }
    }
}
