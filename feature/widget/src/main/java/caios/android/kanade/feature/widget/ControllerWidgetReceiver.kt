package caios.android.kanade.feature.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import caios.android.kanade.core.music.MusicController
import caios.android.kanade.core.repository.MusicRepository
import caios.android.kanade.core.repository.UserDataRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ControllerWidgetReceiver : GlanceAppWidgetReceiver() {

    @Inject
    lateinit var userDataRepository: UserDataRepository

    @Inject
    lateinit var musicController: MusicController

    @Inject
    lateinit var musicRepository: MusicRepository

    override val glanceAppWidget: GlanceAppWidget
        get() = ControllerWidget(
            userDataRepository = userDataRepository,
            musicController = musicController,
            musicRepository = musicRepository,
        )
}
