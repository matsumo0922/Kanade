package caios.android.kanade.core.database.album_detail

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface AlbumDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlbumDetail(vararg entity: AlbumDetailEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(vararg entity: AlbumTrackEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTag(vararg entity: AlbumTagEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAlbumDetail(vararg entity: AlbumDetailEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTrack(vararg entity: AlbumTrackEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTag(vararg entity: AlbumTagEntity)

    @Transaction
    @Query("DELETE FROM album_detail WHERE album_id = :albumId")
    fun delete(albumId: Long)

    @Delete
    fun deleteAlbumDetail(vararg entity: AlbumDetailEntity)

    @Delete
    fun deleteTrack(vararg entity: AlbumTrackEntity)

    @Delete
    fun deleteTag(vararg entity: AlbumTagEntity)

    @Transaction
    @Query("SELECT * FROM album_detail")
    fun loadAll(): List<AlbumDetailModel>

    @Transaction
    @Query("SELECT * FROM album_detail WHERE album_id = :albumId")
    fun load(albumId: Long): AlbumDetailModel?
}
