package caios.android.kanade.core.repository

import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.database.playlist.PlaylistDao
import caios.android.kanade.core.database.playlist.PlaylistEntity
import caios.android.kanade.core.database.playlist.PlaylistItemEntity
import caios.android.kanade.core.database.playlist.PlaylistModel
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.model.music.PlaylistItem
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.MusicConfig
import caios.android.kanade.core.model.player.MusicOrderOption
import caios.android.kanade.core.repository.util.sortList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class DefaultPlaylistRepository @Inject constructor(
    private val songRepository: SongRepository,
    private val playlistDao: PlaylistDao,
    @Dispatcher(KanadeDispatcher.IO) private val dispatcher: CoroutineDispatcher,
) : PlaylistRepository {

    private val cache = ConcurrentHashMap<Long, Playlist>()
    private val _data = MutableStateFlow(emptyList<Playlist>())

    override val data: SharedFlow<List<Playlist>> = _data.asSharedFlow()

    override fun get(playlistId: Long): Playlist? = cache[playlistId]

    override fun gets(): List<Playlist> = cache.values.toList()

    private suspend fun fetchPlaylist() = withContext(dispatcher) {
        cache.clear()

        playlistDao.loadAll().map { it.toData() }.onEach {
            cache[it.id] = it
            _data.value = cache.values.toList()
        }
    }

    override suspend fun playlist(playlistId: Long): Playlist? = withContext(dispatcher) {
        playlistDao.load(playlistId)?.toData()?.also {
            cache[playlistId] = it
            _data.value = cache.values.toList()
        }
    }

    override suspend fun playlists(musicConfig: MusicConfig): List<Playlist> = withContext(dispatcher) {
        val playlists = playlistDao.loadAll().map { it.toData() }.onEach {
            cache[it.id] = it
            _data.value = cache.values.toList()
        }
        playlistSort(playlists, musicConfig)
    }

    override suspend fun create(playlist: Playlist): Unit = withContext(dispatcher) {
        val model = playlist.toModel()
        val playlistId = playlistDao.insertPlaylist(model.playlist)

        playlistDao.insertPlaylistItem(*model.items.map { it.copy(playlistId = playlistId) }.toTypedArray())
        fetchPlaylist()
    }

    override suspend fun delete(playlist: Playlist): Unit = withContext(dispatcher) {
        playlistDao.deletePlaylist(playlist.toModel().playlist)
        fetchPlaylist()
    }

    override suspend fun addItems(playlistId: Long, songs: List<Song>) = withContext(dispatcher) {
        val playlist = playlistDao.load(playlistId) ?: return@withContext
        val items = songs.mapIndexed { index, song ->
            PlaylistItemEntity(
                id = 0,
                playlistId = playlistId,
                index = playlist.items.size + index,
                songId = song.id,
            )
        }

        playlistDao.insertPlaylistItem(*items.toTypedArray())
        fetchPlaylist()
    }

    override suspend fun removeItem(playlistId: Long, index: Int) = withContext(dispatcher) {
        val playlist = playlistDao.load(playlistId) ?: return@withContext
        val items = playlist.items.toMutableList().apply {
            removeIf { it.index == index }
            sortedBy { it.index }
            mapIndexed { i, item -> item.copy(index = i) }
        }

        playlistDao.deleteItem(playlist.items.find { it.index == index }!!.id)
        playlistDao.updatePlaylistItem(*items.toTypedArray())
        fetchPlaylist()
    }

    override suspend fun isFavorite(song: Song): Boolean {
        val favorite = cache.values.find { it.isSystemPlaylist } ?: return false
        return favorite.items.any { it.song.id == song.id }
    }

    override suspend fun addToFavorite(song: Song) {
        val favorite = cache.values.find { it.isSystemPlaylist }

        if (favorite != null) {
            addItems(favorite.id, listOf(song))
        } else {
            createFavoritePlaylist(song)
        }
    }

    override suspend fun removeFromFavorite(song: Song) {
        val favorite = cache.values.find { it.isSystemPlaylist } ?: return
        removeItem(favorite.id, favorite.items.find { it.song.id == song.id }!!.index)
    }

    override fun playlistSort(playlists: List<Playlist>, musicConfig: MusicConfig): List<Playlist> {
        val order = musicConfig.playlistOrder
        val option = order.musicOrderOption

        require(option is MusicOrderOption.Playlist) { "MusicOrderOption is not Playlist" }

        return when (option) {
            MusicOrderOption.Playlist.NAME -> playlists.sortList({ it.name }, order = order.order)
            MusicOrderOption.Playlist.TRACKS -> playlists.sortList({ it.items.size }, order = order.order)
        }
    }

    private suspend fun createFavoritePlaylist(song: Song): Playlist {
        val favorite = Playlist(
            id = 0,
            name = "Favorite",
            items = setOf(
                PlaylistItem(
                    id = 0,
                    song = song,
                    index = 0,
                )
            ),
            isSystemPlaylist = true,
        )

        create(favorite)

        return favorite
    }

    private fun PlaylistModel.toData(): Playlist {
        return Playlist(
            id = playlist.id,
            name = playlist.name,
            items = items.mapNotNull { item ->
                songRepository.get(item.songId)?.let {
                    PlaylistItem(
                        id = item.id,
                        song = it,
                        index = item.index,
                    )
                }
            }.toSet(),
            isSystemPlaylist = playlist.isSystemPlaylist,
        )
    }

    private fun Playlist.toModel(): PlaylistModel {
        return PlaylistModel().apply {
            playlist = PlaylistEntity(
                id = id,
                name = name,
                isSystemPlaylist = isSystemPlaylist,
            )
            items = this@toModel.items.map {
                PlaylistItemEntity(
                    id = it.id,
                    playlistId = id,
                    index = it.index,
                    songId = it.song.id,
                )
            }
        }
    }
}
