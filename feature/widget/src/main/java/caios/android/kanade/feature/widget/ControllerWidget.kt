package caios.android.kanade.feature.widget

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import caios.android.kanade.core.music.MusicController
import caios.android.kanade.core.repository.MusicRepository
import caios.android.kanade.core.repository.UserDataRepository
import caios.android.kanade.feature.widget.items.ControllerWidgetScreen
import timber.log.Timber

class ControllerWidget(
    private val userDataRepository: UserDataRepository,
    private val musicController: MusicController,
    private val musicRepository: MusicRepository,
) : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val userData by userDataRepository.userData.collectAsState(initial = null)
            val currentSong by musicController.currentSong.collectAsState(initial = null)
            val playerState by musicController.playerState.collectAsState(initial = null)

            Timber.d("provideGlance: $userData, $currentSong, $playerState")

            GlanceTheme {
                if (userData != null && currentSong != null && playerState != null) {
                    ControllerWidgetScreen(
                        userData = userData!!,
                        currentSong = currentSong,
                        playerState = playerState!!,
                    )
                }
            }
        }
    }
}
