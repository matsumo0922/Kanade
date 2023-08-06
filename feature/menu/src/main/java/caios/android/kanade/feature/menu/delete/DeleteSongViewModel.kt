package caios.android.kanade.feature.menu.delete

import androidx.lifecycle.ViewModel
import caios.android.kanade.core.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeleteSongViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
) : ViewModel() {

    suspend fun fetchSong() {
        musicRepository.clear()
        musicRepository.fetchSongs()
        musicRepository.fetchArtists()
        musicRepository.fetchAlbums()
        musicRepository.fetchPlaylist()
        musicRepository.fetchAlbumArtwork()
        musicRepository.fetchArtistArtwork()
        musicRepository.refresh()
    }
}
