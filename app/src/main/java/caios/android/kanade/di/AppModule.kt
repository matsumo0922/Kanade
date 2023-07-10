package caios.android.kanade.di

import caios.android.kanade.BuildConfig
import caios.android.kanade.core.common.network.KanadeConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
            isDebug = BuildConfig.DEBUG,
            lastFmApiKey = BuildConfig.LAST_FM_API_KEY,
            lastFmApiSecret = BuildConfig.LAST_FM_API_SECRET,
        )
    }
}
