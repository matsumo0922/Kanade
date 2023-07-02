package caios.android.kanade.core.database.playlist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
abstract class PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPlaylist(entity: PlaylistEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPlaylistItem(vararg entity: PlaylistItemEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updatePlaylist(vararg entity: PlaylistEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updatePlaylistItem(vararg entity: PlaylistItemEntity)

    @Delete
    abstract fun deletePlaylist(vararg entity: PlaylistEntity)

    @Delete
    abstract fun deletePlaylistItem(vararg entity: PlaylistItemEntity)

    @Transaction
    @Query("DELETE FROM playlist WHERE id = :playlistId")
    abstract fun delete(playlistId: Long)

    @Transaction
    @Query("DELETE FROM playlist_item WHERE id = :playlistItemId")
    abstract fun deleteItem(playlistItemId: Long)

    @Transaction
    @Query("SELECT * FROM playlist")
    abstract fun loadAll(): List<PlaylistModel>

    @Transaction
    @Query("SELECT * FROM playlist WHERE id = :playlistId")
    abstract fun load(playlistId: Long): PlaylistModel?

    @Transaction
    @Query("SELECT * FROM playlist_item WHERE id = :playlistId")
    abstract fun loadItem(playlistId: Long): PlaylistItemEntity

    @Transaction
    open fun changeIndexTransaction(playlistId: Long, fromIndex: Int, toIndex: Int) {
        val playlist = load(playlistId) ?: return
        val items = playlist.items.toMutableList().apply {
            add(toIndex, removeAt(fromIndex))
        }

        val model = PlaylistModel().apply {
            this.playlist = playlist.playlist
            this.items = items.mapIndexed { index, entity -> entity.copy(id = 0, index = index) }
        }

        delete(playlistId)

        insertPlaylist(model.playlist).also {
            insertPlaylistItem(*model.items.map { it.copy(playlistId = playlistId) }.toTypedArray())
        }
    }
}
