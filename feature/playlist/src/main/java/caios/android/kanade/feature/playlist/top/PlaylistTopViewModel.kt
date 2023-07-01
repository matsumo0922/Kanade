package caios.android.kanade.feature.playlist.top

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.player.MusicOrder
import caios.android.kanade.core.music.MusicController
import caios.android.kanade.core.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlaylistTopViewModel @Inject constructor(
    private val musicController: MusicController,
    private val musicRepository: MusicRepository,
) : ViewModel() {

    val screenState = musicRepository.config.map {
        ScreenState.Idle(
            PlaylistTopUiState(
                sortOrder = it.playlistOrder
            ),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ScreenState.Loading,
    )
}

@Stable
data class PlaylistTopUiState(
    val sortOrder: MusicOrder,
)
