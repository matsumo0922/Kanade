package caios.android.kanade.core.repository.di

import caios.android.kanade.core.repository.AlbumRepository
import caios.android.kanade.core.repository.AlbumRepositoryImpl
import caios.android.kanade.core.repository.ArtistRepository
import caios.android.kanade.core.repository.ArtistRepositoryImpl
import caios.android.kanade.core.repository.ArtworkRepository
import caios.android.kanade.core.repository.ArtworkRepositoryImpl
import caios.android.kanade.core.repository.ExternalPlaylistRepository
import caios.android.kanade.core.repository.ExternalPlaylistRepositoryImpl
import caios.android.kanade.core.repository.KugouLyricsRepository
import caios.android.kanade.core.repository.LastFmRepository
import caios.android.kanade.core.repository.LastFmRepositoryImpl
import caios.android.kanade.core.repository.LyricsRepository
import caios.android.kanade.core.repository.MusicRepository
import caios.android.kanade.core.repository.MusicRepositoryImpl
import caios.android.kanade.core.repository.MusixmatchLyricsRepository
import caios.android.kanade.core.repository.PlayHistoryRepository
import caios.android.kanade.core.repository.PlayHistoryRepositoryImpl
import caios.android.kanade.core.repository.PlaylistRepository
import caios.android.kanade.core.repository.PlaylistRepositoryImpl
import caios.android.kanade.core.repository.SongRepository
import caios.android.kanade.core.repository.SongRepositoryImpl
import caios.android.kanade.core.repository.UserDataRepository
import caios.android.kanade.core.repository.UserDataRepositoryImpl
import caios.android.kanade.core.repository.YTMusicRepository
import caios.android.kanade.core.repository.YTMusicRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LyricsKugou

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LyricsMusixmatch

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindUserDataRepository(
        userDataRepository: UserDataRepositoryImpl,
    ): UserDataRepository

    @Singleton
    @Binds
    fun bindMusicRepository(
        musicRepository: MusicRepositoryImpl,
    ): MusicRepository

    @Singleton
    @Binds
    fun bindSongRepository(
        songRepository: SongRepositoryImpl,
    ): SongRepository

    @Singleton
    @Binds
    fun bindArtistRepository(
        artistRepository: ArtistRepositoryImpl,
    ): ArtistRepository

    @Singleton
    @Binds
    fun bindAlbumRepository(
        albumRepository: AlbumRepositoryImpl,
    ): AlbumRepository

    @Singleton
    @Binds
    fun bindPlaylistRepository(
        playlistRepository: PlaylistRepositoryImpl,
    ): PlaylistRepository

    @Singleton
    @Binds
    fun bindExternalPlaylistRepository(
        externalPlaylistRepository: ExternalPlaylistRepositoryImpl,
    ): ExternalPlaylistRepository

    @Singleton
    @Binds
    fun bindArtworkRepository(
        artworkRepository: ArtworkRepositoryImpl,
    ): ArtworkRepository

    @LyricsKugou
    @Singleton
    @Binds
    fun bindKugouLyricsRepository(
        lyricsRepository: KugouLyricsRepository,
    ): LyricsRepository

    @LyricsMusixmatch
    @Singleton
    @Binds
    fun bindMusixmatchLyricsRepository(
        lyricsRepository: MusixmatchLyricsRepository,
    ): LyricsRepository

    @Singleton
    @Binds
    fun bindPlayHistoryRepository(
        playHistoryRepository: PlayHistoryRepositoryImpl,
    ): PlayHistoryRepository

    @Singleton
    @Binds
    fun bindLastFmRepository(
        lastFmRepository: LastFmRepositoryImpl,
    ): LastFmRepository

    @Singleton
    @Binds
    fun bindYTMusicRepository(
        yTMusicRepository: YTMusicRepositoryImpl,
    ): YTMusicRepository
}
