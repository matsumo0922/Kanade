package caios.android.kanade.feature.tag

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagEditViewModel @Inject constructor(
    private val songRepository: SongRepository,
) : ViewModel() {

    val screenState = MutableStateFlow<ScreenState<TagEditUiState>>(ScreenState.Loading)

    fun fetch(songId: Long) {
        viewModelScope.launch {
            val song = songRepository.get(songId)

            if (song != null) {
                screenState.value = ScreenState.Idle(TagEditUiState(song))
            } else {
                screenState.value = ScreenState.Error(
                    message = R.string.error_no_data,
                    retryTitle = R.string.common_close,
                )
            }
        }
    }
}

@Stable
data class TagEditUiState(
    val song: Song,
)
