package caios.android.kanade.core.music.di

import caios.android.kanade.core.music.MusicController
import caios.android.kanade.core.music.MusicControllerImpl
import caios.android.kanade.core.music.QueueManager
import caios.android.kanade.core.music.QueueManagerImpl
import caios.android.kanade.core.music.YTMusic
import caios.android.kanade.core.music.YTMusicImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface MusicModule {

    @Binds
    @Singleton
    fun bindsMusicController(musicController: MusicControllerImpl): MusicController

    @Binds
    @Singleton
    fun bindsQueueManager(queueManager: QueueManagerImpl): QueueManager

    @Binds
    @Singleton
    fun bindsYTMusic(ytMusic: YTMusicImpl): YTMusic
}
