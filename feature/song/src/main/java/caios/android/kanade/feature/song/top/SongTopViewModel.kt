package caios.android.kanade.feature.song.top

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
class SongTopViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
) : ViewModel() {

    var screenState = musicRepository.config.map {
        musicRepository.fetchSongs(it)
        ScreenState.Idle(musicRepository.sortedSongs(it))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ScreenState.Loading,
    )
}
