package caios.android.kanade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import caios.android.kanade.core.model.ScreenState
import caios.android.kanade.core.repository.MusicRepository
import caios.android.kanade.core.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val musicRepository: MusicRepository,
) : ViewModel() {

    val screenState = userDataRepository.userData.map {
        ScreenState.Idle(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ScreenState.Loading,
    )

    fun fetchMusic() {
        viewModelScope.launch(Dispatchers.IO) {
            musicRepository.fetchSongs()
            musicRepository.fetchArtists()
            musicRepository.fetchAlbums()
        }
    }
}
