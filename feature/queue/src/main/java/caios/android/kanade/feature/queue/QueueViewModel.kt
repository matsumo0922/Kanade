package caios.android.kanade.feature.queue

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.PlayerEvent
import caios.android.kanade.core.music.MusicController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class QueueViewModel @Inject constructor(
    private val musicController: MusicController,
) : ViewModel() {

    val screenState = musicController.currentQueue.map {
        if (it != null) {
            ScreenState.Idle(
                QueueUiState(
                    queue = it.items,
                    index = it.index,
                    isPlaying = false,
                ),
            )
        } else {
            ScreenState.Error(message = R.string.error_no_data)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ScreenState.Loading,
    )

    fun onSkipToQueue(index: Int) {
        musicController.playerEvent(
            PlayerEvent.SkipToQueue(index),
        )
    }

    fun onQueueChanged(fromIndex: Int, toIndex: Int) {
        musicController.moveQueue(fromIndex, toIndex)
    }

    fun onDeleteItem(index: Int) {
        musicController.removeFromQueue(index)
    }
}

@Stable
data class QueueUiState(
    val queue: List<Song>,
    val index: Int,
    val isPlaying: Boolean,
)
