package caios.android.kanade

import android.content.Context
import caios.android.kanade.core.common.network.di.ApplicationScope
import caios.android.kanade.core.model.player.PlayerState
import caios.android.kanade.core.music.MusicController
import caios.android.kanade.core.repository.UserDataRepository
import caios.android.kanade.feature.widget.horizontal.HorizontalControllerWidgetReceiver
import caios.android.kanade.feature.widget.square.SquareControllerWidgetReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

class WidgetUpdater @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val musicController: MusicController,
    @ApplicationContext private val context: Context,
    @ApplicationScope private val scope: CoroutineScope,
) {
    init {
        scope.launch {
            combine(
                userDataRepository.userData,
                musicController.currentSong,
                musicController.playerState,
            ) { userData, song, playerState ->
                Triple(userData, song, playerState)
            }.collectLatest { (userData, _, playerState) ->
                context.sendBroadcast(
                    HorizontalControllerWidgetReceiver.createUpdateIntent(
                        context = context,
                        isPlaying = playerState == PlayerState.Playing,
                        isPlusUser = userData.hasPrivilege,
                    ),
                )

                context.sendBroadcast(
                    SquareControllerWidgetReceiver.createUpdateIntent(
                        context = context,
                        isPlaying = playerState == PlayerState.Playing,
                        isPlusUser = userData.hasPrivilege,
                    ),
                )
            }
        }
    }
}
