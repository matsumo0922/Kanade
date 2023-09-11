package caios.android.kanade.core.music.upgrade

import android.content.Context
import caios.android.kanade.core.repository.MusicRepository
import caios.android.kanade.core.repository.PlaylistRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

class BackwardCompatibleChecker @Inject constructor(
    musicRepository: MusicRepository,
    playlistRepository: PlaylistRepository,
    @ApplicationContext private val context: Context,
) {
    private val oldPlaylistLoader = OldPlaylistLoader(
        context = context,
        musicRepository = musicRepository,
        playlistRepository = playlistRepository,
    )

    private val oldQueueLoader = OldQueueLoader(
        context = context,
        musicRepository = musicRepository,
    )

    suspend fun check() {
        if (oldPlaylistLoader.hasOldItems()) {
            Timber.d("Upgrade old playlist.")
            oldPlaylistLoader.upgrade()
        }

        if (oldQueueLoader.hasOldItems()) {
            Timber.d("Upgrade old queue.")
            oldQueueLoader.upgrade()
        }
    }
}
