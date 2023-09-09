package caios.android.kanade.core.music.di

import android.content.Context
import caios.android.kanade.core.common.network.di.ApplicationScope
import caios.android.kanade.core.music.MusicController
import caios.android.kanade.core.music.MusicControllerImpl
import caios.android.kanade.core.music.QueueManager
import caios.android.kanade.core.music.QueueManagerImpl
import caios.android.kanade.core.repository.MusicRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MusicModule {

    @Provides
    @Singleton
    fun provideMusicController(
        musicRepository: MusicRepository,
        musicQueue: QueueManager,
        @ApplicationContext context: Context,
        @ApplicationScope scope: CoroutineScope,
    ): MusicController {
        return MusicControllerImpl(musicRepository, musicQueue, context, scope)
    }

    @Provides
    @Singleton
    fun provideQueueManager(
        musicRepository: MusicRepository,
    ): QueueManager {
        return QueueManagerImpl(musicRepository)
    }
}
