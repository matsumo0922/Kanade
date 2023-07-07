package caios.android.kanade.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import caios.android.kanade.core.database.album_detail.AlbumDetailDao
import caios.android.kanade.core.database.album_detail.AlbumDetailEntity
import caios.android.kanade.core.database.album_detail.AlbumTagEntity
import caios.android.kanade.core.database.album_detail.AlbumTrackEntity
import caios.android.kanade.core.database.artist_detail.ArtistDetailDao
import caios.android.kanade.core.database.artist_detail.ArtistDetailEntity
import caios.android.kanade.core.database.artist_detail.ArtistTagEntity
import caios.android.kanade.core.database.artist_detail.SimilarArtistEntity
import caios.android.kanade.core.database.artwork.ArtworkDao
import caios.android.kanade.core.database.artwork.ArtworkEntity
import caios.android.kanade.core.database.history.PlayHistoryDao
import caios.android.kanade.core.database.history.PlayHistoryEntity
import caios.android.kanade.core.database.playlist.PlaylistDao
import caios.android.kanade.core.database.playlist.PlaylistEntity
import caios.android.kanade.core.database.playlist.PlaylistItemEntity

@Database(
    entities = [
        ArtistDetailEntity::class,
        ArtistTagEntity::class,
        SimilarArtistEntity::class,
        AlbumDetailEntity::class,
        AlbumTrackEntity::class,
        AlbumTagEntity::class,
        ArtworkEntity::class,
        PlaylistEntity::class,
        PlaylistItemEntity::class,
        PlayHistoryEntity::class,
    ],
    version = 1,
)
abstract class KanadeDataBase : RoomDatabase() {
    abstract fun artistDetailDao(): ArtistDetailDao
    abstract fun albumDetailDao(): AlbumDetailDao
    abstract fun artworkDao(): ArtworkDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun playHistoryDao(): PlayHistoryDao
}
