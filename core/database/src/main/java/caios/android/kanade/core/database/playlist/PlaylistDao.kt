package caios.android.kanade.core.database.playlist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(entity: PlaylistEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylistItem(vararg entity: PlaylistItemEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updatePlaylist(vararg entity: PlaylistEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updatePlaylistItem(vararg entity: PlaylistItemEntity)

    @Delete
    fun deletePlaylist(vararg entity: PlaylistEntity)

    @Delete
    fun deletePlaylistItem(vararg entity: PlaylistItemEntity)

    @Transaction
    @Query("DELETE FROM playlist WHERE id = :playlistId")
    fun delete(playlistId: Long)

    @Transaction
    @Query("DELETE FROM playlist_item WHERE id = :playlistItemId")
    fun deleteItem(playlistItemId: Long)

    @Transaction
    @Query("SELECT * FROM playlist")
    fun loadAll(): List<PlaylistModel>

    @Transaction
    @Query("SELECT * FROM playlist WHERE id = :playlistId")
    fun load(playlistId: Long): PlaylistModel?

    @Transaction
    @Query("SELECT * FROM playlist_item WHERE id = :playlistId")
    fun loadItem(playlistId: Long): PlaylistItemEntity
}
