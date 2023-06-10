package caios.android.kanade.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.common.network.di.ApplicationScope
import caios.android.kanade.core.datastore.MusicPreference
import caios.android.kanade.core.datastore.MusicPreferenceSerializer
import caios.android.kanade.core.datastore.QueuePreference
import caios.android.kanade.core.datastore.QueuePreferenceSerializer
import caios.android.kanade.core.datastore.UserPreference
import caios.android.kanade.core.datastore.UserPreferenceSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providesUserPreferencesDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(KanadeDispatcher.IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        userPreferenceSerializer: UserPreferenceSerializer,
    ): DataStore<UserPreference> {
        return DataStoreFactory.create(
            serializer = userPreferenceSerializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
            produceFile = { context.dataStoreFile("user_preference.pb") },
        )
    }

    @Provides
    @Singleton
    fun providesMusicPreferencesDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(KanadeDispatcher.IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        musicPreferenceSerializer: MusicPreferenceSerializer,
    ): DataStore<MusicPreference> {
        return DataStoreFactory.create(
            serializer = musicPreferenceSerializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
            produceFile = { context.dataStoreFile("music_preference.pb") },
        )
    }

    @Provides
    @Singleton
    fun providesQueuePreferencesDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(KanadeDispatcher.IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        queuePreferenceSerializer: QueuePreferenceSerializer,
    ): DataStore<QueuePreference> {
        return DataStoreFactory.create(
            serializer = queuePreferenceSerializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
            produceFile = { context.dataStoreFile("queue_preference.pb") },
        )
    }
}
