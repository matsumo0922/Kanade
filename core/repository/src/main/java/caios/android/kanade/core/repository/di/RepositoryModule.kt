package caios.android.kanade.core.repository.di

import caios.android.kanade.core.repository.AlbumRepository
import caios.android.kanade.core.repository.ArtistRepository
import caios.android.kanade.core.repository.DefaultAlbumRepository
import caios.android.kanade.core.repository.DefaultArtistRepository
import caios.android.kanade.core.repository.DefaultMusicRepository
import caios.android.kanade.core.repository.DefaultSongRepository
import caios.android.kanade.core.repository.DefaultUserDataRepository
import caios.android.kanade.core.repository.MusicRepository
import caios.android.kanade.core.repository.SongRepository
import caios.android.kanade.core.repository.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindUserDataRepository(
        userDataRepository: DefaultUserDataRepository,
    ): UserDataRepository

    @Binds
    fun bindMusicRepository(
        musicRepository: DefaultMusicRepository,
    ): MusicRepository

    @Binds
    fun bindSongRepository(
        songRepository: DefaultSongRepository,
    ): SongRepository

    @Binds
    fun bindArtistRepository(
        artistRepository: DefaultArtistRepository,
    ): ArtistRepository

    @Binds
    fun bindAlbumRepository(
        albumRepository: DefaultAlbumRepository,
    ): AlbumRepository
}
