package caios.android.kanade.core.database.artwork

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface ArtworkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: ArtworkEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg entity: ArtworkEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(entity: ArtworkEntity)

    @Delete
    fun delete(entity: ArtworkEntity)

    @Transaction
    @Query("SELECT * FROM artwork")
    fun loadAll(): List<ArtworkEntity>

    @Transaction
    @Query("SELECT * FROM artwork WHERE artist_id IS NOT NULL")
    fun loadArtists(): List<ArtworkEntity>

    @Transaction
    @Query("SELECT * FROM artwork WHERE album_id IS NOT NULL")
    fun loadAlbums(): List<ArtworkEntity>

    @Transaction
    @Query("SELECT * FROM artwork WHERE artist_id = :artistId")
    fun loadArtist(artistId: Long): ArtworkEntity?

    @Transaction
    @Query("SELECT * FROM artwork WHERE album_id = :albumId")
    fun loadAlbum(albumId: Long): ArtworkEntity?
}