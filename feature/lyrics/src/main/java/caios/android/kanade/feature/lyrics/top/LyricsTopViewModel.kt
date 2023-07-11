package caios.android.kanade.feature.lyrics.top

import androidx.lifecycle.ViewModel
import caios.android.kanade.core.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LyricsTopViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
): ViewModel(){

}
