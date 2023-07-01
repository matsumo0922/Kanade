package caios.android.kanade.core.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Singleton
    @Provides
    fun provideKanadeDatabase(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(context, KanadeDataBase::class.java, "kanade.db").build()

    @Singleton
    @Provides
    fun provideArtistDetailDao(db: KanadeDataBase) = db.artistDetailDao()

    @Singleton
    @Provides
    fun provideAlbumDetailDao(db: KanadeDataBase) = db.albumDetailDao()

    @Singleton
    @Provides
    fun provideArtworkDao(db: KanadeDataBase) = db.artworkDao()

    @Singleton
    @Provides
    fun providePlaylistDao(db: KanadeDataBase) = db.playlistDao()
}
