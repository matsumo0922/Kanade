package caios.android.kanade.di

import android.content.Context
import caios.android.kanade.BuildConfig
import caios.android.kanade.WidgetUpdater
import caios.android.kanade.core.common.network.KanadeConfig
import caios.android.kanade.core.common.network.di.ApplicationScope
import caios.android.kanade.core.music.MusicController
import caios.android.kanade.core.repository.UserDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideKanadeConfig(): KanadeConfig {
        return KanadeConfig(
            applicationId = BuildConfig.APPLICATION_ID,
            buildType = BuildConfig.BUILD_TYPE,
            versionCode = BuildConfig.VERSION_CODE,
            versionName = BuildConfig.VERSION_NAME,
            isDebug = BuildConfig.DEBUG,
            developerPassword = BuildConfig.DEVELOPER_PASSWORD,
            lastFmApiKey = BuildConfig.LAST_FM_API_KEY,
            lastFmApiSecret = BuildConfig.LAST_FM_API_SECRET,
            musixmatchApiKey = BuildConfig.MUSIXMATCH_API_KEY,
            ytMusicApiKey = BuildConfig.YTMUSIC_CLIENT_ID,
            ytMusicApiSecret = BuildConfig.YTMUSIC_CLIENT_SECRET,
        )
    }

    @Provides
    @Singleton
    fun provideWidgetUpdater(
        userDataRepository: UserDataRepository,
        musicController: MusicController,
        @ApplicationContext context: Context,
        @ApplicationScope scope: CoroutineScope,
    ): WidgetUpdater {
        return WidgetUpdater(
            userDataRepository = userDataRepository,
            musicController = musicController,
            context = context,
            scope = scope,
        )
    }
}
