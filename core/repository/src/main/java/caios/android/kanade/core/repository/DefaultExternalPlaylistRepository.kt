package caios.android.kanade.core.repository

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import caios.android.kanade.core.model.music.ExternalPlaylist
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.model.music.PlaylistItem
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import javax.inject.Inject

class DefaultExternalPlaylistRepository @Inject constructor(
    private val songRepository: SongRepository,
    private val playlistRepository: PlaylistRepository,
    @ApplicationContext private val context: Context,
) : ExternalPlaylistRepository {

    private val contentResolver get() = context.contentResolver

    override suspend fun getExternalPlaylists(): List<ExternalPlaylist> {
        val uri = MediaStore.Audio.Playlists.getContentUri("external")
        val cursor = contentResolver.query(uri, null, null, null, "")

        if (cursor == null || !cursor.moveToFirst()) return emptyList()

        val playlistNameColumn = cursor.getColumnIndex(MediaStore.Audio.Playlists.NAME)
        val playlistIdColumn = cursor.getColumnIndex(MediaStore.Audio.Playlists._ID)
        val playlistDataList = mutableListOf<ExternalPlaylist>()

        do {
            val playlistName = cursor.getString(playlistNameColumn)
            val playlistId = cursor.getLong(playlistIdColumn)

            val playlistUri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId)
            val playlistCursor = contentResolver.query(playlistUri, null, null, null, "")

            if (playlistCursor != null && playlistCursor.moveToFirst()) {
                val songIds = mutableListOf<Long>()
                val idColumn = playlistCursor.getColumnIndex(MediaStore.Audio.Playlists.Members.AUDIO_ID)

                do {
                    val id = playlistCursor.getInt(idColumn)
                    songIds.add(id.toLong())
                } while (playlistCursor.moveToNext())

                playlistDataList.add(ExternalPlaylist(playlistId, playlistName, songIds))
            } else {
                playlistDataList.add(ExternalPlaylist(playlistId, playlistName, emptyList()))
            }

            playlistCursor?.close()
        } while (cursor.moveToNext())

        cursor.close()

        return playlistDataList
    }

    override suspend fun export(playlist: Playlist) {
        val beforePlaylists = getExternalPlaylists()

        val uri = MediaStore.Audio.Playlists.getContentUri("external")
        val contentValue = ContentValues().apply {
            put(MediaStore.Audio.Playlists.NAME, playlist.name)
        }

        contentResolver.insert(uri, contentValue)

        val afterPlaylists = getExternalPlaylists()
        val playlistId = (afterPlaylists - beforePlaylists.toSet()).elementAtOrNull(0)?.id ?: return

        addMusic(playlistId, playlist.songs.map { it.id })
    }

    override suspend fun import(externalPlaylistId: Long) {
        val externalPlaylist = getExternalPlaylists().find { it.id == externalPlaylistId }!!
        val songs = externalPlaylist.songIds.mapNotNull { songRepository.get(it) }
        val items = songs.mapIndexed { index, song -> PlaylistItem(0, song, index) }
        val playlist = Playlist(0, externalPlaylist.name, items.toSet(), createdAt = LocalDateTime.now())

        playlistRepository.create(playlist)
    }

    private fun addMusic(playlistId: Long, songIds: List<Long>) {
        var nextOrder = getMaxPlayOrder(playlistId)?.plus(1) ?: 0
        val playlistUri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId)

        for (musicId in songIds) {
            contentResolver.insert(playlistUri, ContentValues().apply {
                put(MediaStore.Audio.Playlists.Members.AUDIO_ID, musicId)
                put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, nextOrder)
            })

            nextOrder += 1
        }
    }

    private fun getMaxPlayOrder(playlistId: Long): Int? {
        val playlistUri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId)
        val playlistCursor = contentResolver.query(playlistUri, null, null, null, "")

        if (playlistCursor == null || !playlistCursor.moveToFirst()) return null

        val orderList = mutableListOf<Int>()
        val orderColumn = playlistCursor.getColumnIndex(MediaStore.Audio.Playlists.Members.PLAY_ORDER)

        do {
            val order = playlistCursor.getInt(orderColumn)
            orderList.add(order)
        } while (playlistCursor.moveToNext())

        playlistCursor.close()

        return orderList.maxOrNull()
    }
}
