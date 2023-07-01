package caios.android.kanade.core.repository.di

import caios.android.kanade.core.repository.AlbumRepository
import caios.android.kanade.core.repository.ArtistRepository
import caios.android.kanade.core.repository.ArtworkRepository
import caios.android.kanade.core.repository.DefaultAlbumRepository
import caios.android.kanade.core.repository.DefaultArtistRepository
import caios.android.kanade.core.repository.DefaultArtworkRepository
import caios.android.kanade.core.repository.DefaultLyricsRepository
import caios.android.kanade.core.repository.DefaultMusicRepository
import caios.android.kanade.core.repository.DefaultPlaylistRepository
import caios.android.kanade.core.repository.DefaultSongRepository
import caios.android.kanade.core.repository.DefaultUserDataRepository
import caios.android.kanade.core.repository.LyricsRepository
import caios.android.kanade.core.repository.MusicRepository
import caios.android.kanade.core.repository.PlaylistRepository
import caios.android.kanade.core.repository.SongRepository
import caios.android.kanade.core.repository.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindUserDataRepository(
        userDataRepository: DefaultUserDataRepository,
    ): UserDataRepository

    @Singleton
    @Binds
    fun bindMusicRepository(
        musicRepository: DefaultMusicRepository,
    ): MusicRepository

    @Singleton
    @Binds
    fun bindSongRepository(
        songRepository: DefaultSongRepository,
    ): SongRepository

    @Singleton
    @Binds
    fun bindArtistRepository(
        artistRepository: DefaultArtistRepository,
    ): ArtistRepository

    @Singleton
    @Binds
    fun bindAlbumRepository(
        albumRepository: DefaultAlbumRepository,
    ): AlbumRepository

    @Singleton
    @Binds
    fun bindPlaylistRepository(
        playlistRepository: DefaultPlaylistRepository,
    ): PlaylistRepository

    @Singleton
    @Binds
    fun bindArtworkRepository(
        artworkRepository: DefaultArtworkRepository,
    ): ArtworkRepository

    @Singleton
    @Binds
    fun bindLyricsRepository(
        lyricsRepository: DefaultLyricsRepository,
    ): LyricsRepository
}
