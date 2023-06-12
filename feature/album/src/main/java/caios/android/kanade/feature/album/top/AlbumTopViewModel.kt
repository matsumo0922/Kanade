package caios.android.kanade.feature.album.top

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AlbumTopViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
) : ViewModel() {

    val screenState = musicRepository.config.map {
        musicRepository.fetchAlbums(it)
        musicRepository.fetchAlbumArtwork()
        ScreenState.Idle(musicRepository.sortedAlbums(it))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ScreenState.Loading,
    )
}
