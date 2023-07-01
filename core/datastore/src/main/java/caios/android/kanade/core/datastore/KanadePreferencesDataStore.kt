package caios.android.kanade.core.datastore

import androidx.datastore.core.DataStore
import caios.android.kanade.core.model.Order
import caios.android.kanade.core.model.ThemeConfig
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.model.music.LastQueue
import caios.android.kanade.core.model.player.MusicConfig
import caios.android.kanade.core.model.player.MusicOrder
import caios.android.kanade.core.model.player.MusicOrderOption
import caios.android.kanade.core.model.player.RepeatMode
import caios.android.kanade.core.model.player.ShuffleMode
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class KanadePreferencesDataStore @Inject constructor(
    private val userPreference: DataStore<UserPreference>,
    private val musicPreference: DataStore<MusicPreference>,
    private val queuePreference: DataStore<QueuePreference>,
) {
    val userData = userPreference.data
        .map {
            UserData(
                useDynamicColor = true, // it.useDynamicColor,
                isDeveloperMode = it.isDeveloperMode,
                isPremiumMode = it.isPremiumMode,
                themeConfig = when (it.themeConfig) {
                    ThemeConfigProto.THEME_CONFIG_LIGHT -> ThemeConfig.Light
                    ThemeConfigProto.THEME_CONFIG_DARK -> ThemeConfig.Dark
                    else -> ThemeConfig.System
                },
            )
        }

    val musicConfig = musicPreference.data
        .map {
            MusicConfig(
                shuffleMode = when (it.shuffleMode) {
                    ShuffleModeProto.SHUFFLE_ON -> ShuffleMode.ON
                    ShuffleModeProto.SHUFFLE_OFF -> ShuffleMode.OFF
                    else -> ShuffleMode.OFF
                },
                repeatMode = when (it.repeatMode) {
                    RepeatModeProto.REPEAT_ONE -> RepeatMode.ONE
                    RepeatModeProto.REPEAT_ALL -> RepeatMode.ALL
                    RepeatModeProto.REPEAT_OFF -> RepeatMode.OFF
                    else -> RepeatMode.OFF
                },
                songOrder = when (it.songOrder) {
                    SongOrderProto.SONG_ASC_NAME -> MusicOrder(Order.ASC, MusicOrderOption.Song.NAME)
                    SongOrderProto.SONG_ASC_ARTIST -> MusicOrder(Order.ASC, MusicOrderOption.Song.ARTIST)
                    SongOrderProto.SONG_ASC_ALBUM -> MusicOrder(Order.ASC, MusicOrderOption.Song.ALBUM)
                    SongOrderProto.SONG_ASC_DURATION -> MusicOrder(Order.ASC, MusicOrderOption.Song.DURATION)
                    SongOrderProto.SONG_ASC_YEAR -> MusicOrder(Order.ASC, MusicOrderOption.Song.YEAR)
                    SongOrderProto.SONG_DESC_NAME -> MusicOrder(Order.DESC, MusicOrderOption.Song.NAME)
                    SongOrderProto.SONG_DESC_ARTIST -> MusicOrder(Order.DESC, MusicOrderOption.Song.ARTIST)
                    SongOrderProto.SONG_DESC_ALBUM -> MusicOrder(Order.DESC, MusicOrderOption.Song.ALBUM)
                    SongOrderProto.SONG_DESC_DURATION -> MusicOrder(Order.DESC, MusicOrderOption.Song.DURATION)
                    SongOrderProto.SONG_DESC_YEAR -> MusicOrder(Order.DESC, MusicOrderOption.Song.YEAR)
                    else -> MusicOrder(Order.ASC, MusicOrderOption.Song.NAME)
                },
                artistOrder = when (it.artistOrder) {
                    ArtistOrderProto.ARTIST_ASC_NAME -> MusicOrder(Order.ASC, MusicOrderOption.Artist.NAME)
                    ArtistOrderProto.ARTIST_ASC_NUM_TRACKS -> MusicOrder(Order.ASC, MusicOrderOption.Artist.TRACKS)
                    ArtistOrderProto.ARTIST_ASC_NUM_ALBUMS -> MusicOrder(Order.ASC, MusicOrderOption.Artist.ALBUMS)
                    ArtistOrderProto.ARTIST_DESC_NAME -> MusicOrder(Order.DESC, MusicOrderOption.Artist.NAME)
                    ArtistOrderProto.ARTIST_DESC_NUM_TRACKS -> MusicOrder(Order.DESC, MusicOrderOption.Artist.TRACKS)
                    ArtistOrderProto.ARTIST_DESC_NUM_ALBUMS -> MusicOrder(Order.DESC, MusicOrderOption.Artist.ALBUMS)
                    else -> MusicOrder(Order.ASC, MusicOrderOption.Artist.NAME)
                },
                albumOrder = when (it.albumOrder) {
                    AlbumOrderProto.ALBUM_ASC_NAME -> MusicOrder(Order.ASC, MusicOrderOption.Album.NAME)
                    AlbumOrderProto.ALBUM_ASC_ARTIST -> MusicOrder(Order.ASC, MusicOrderOption.Album.ARTIST)
                    AlbumOrderProto.ALBUM_ASC_NUM_TRACKS -> MusicOrder(Order.ASC, MusicOrderOption.Album.TRACKS)
                    AlbumOrderProto.ALBUM_ASC_YEAR -> MusicOrder(Order.ASC, MusicOrderOption.Album.YEAR)
                    AlbumOrderProto.ALBUM_DESC_NAME -> MusicOrder(Order.DESC, MusicOrderOption.Album.NAME)
                    AlbumOrderProto.ALBUM_DESC_ARTIST -> MusicOrder(Order.DESC, MusicOrderOption.Album.ARTIST)
                    AlbumOrderProto.ALBUM_DESC_NUM_TRACKS -> MusicOrder(Order.DESC, MusicOrderOption.Album.TRACKS)
                    AlbumOrderProto.ALBUM_DESC_YEAR -> MusicOrder(Order.DESC, MusicOrderOption.Album.YEAR)
                    else -> MusicOrder(Order.ASC, MusicOrderOption.Album.NAME)
                },
                playlistOrder = when (it.playlistOrder) {
                    PlaylistOrderProto.PLAYLIST_ASC_NAME -> MusicOrder(Order.ASC, MusicOrderOption.Playlist.NAME)
                    PlaylistOrderProto.PLAYLIST_ASC_NUM_TRACKS -> MusicOrder(Order.ASC, MusicOrderOption.Playlist.TRACKS)
                    PlaylistOrderProto.PLAYLIST_DESC_NAME -> MusicOrder(Order.DESC, MusicOrderOption.Playlist.NAME)
                    PlaylistOrderProto.PLAYLIST_DESC_NUM_TRACKS -> MusicOrder(Order.DESC, MusicOrderOption.Playlist.TRACKS)
                    else -> MusicOrder(Order.ASC, MusicOrderOption.Playlist.NAME)
                },
            )
        }

    val lastQueue = queuePreference.data
        .map {
            LastQueue(
                originalItems = it.originalItemsList ?: emptyList(),
                currentItems = it.currentItemsList ?: emptyList(),
                index = it.index,
                progress = it.progress,
            )
        }

    suspend fun setThemeConfig(themeConfig: ThemeConfig) {
        userPreference.updateData {
            it.copy {
                this.themeConfig = when (themeConfig) {
                    ThemeConfig.Light -> ThemeConfigProto.THEME_CONFIG_LIGHT
                    ThemeConfig.Dark -> ThemeConfigProto.THEME_CONFIG_DARK
                    else -> ThemeConfigProto.THEME_CONFIG_SYSTEM
                }
            }
        }
    }

    suspend fun setUseDynamicColor(useDynamicColor: Boolean) {
        userPreference.updateData {
            it.copy {
                this.useDynamicColor = useDynamicColor
            }
        }
    }

    suspend fun setDeveloperMode(isDeveloperMode: Boolean) {
        userPreference.updateData {
            it.copy {
                this.isDeveloperMode = isDeveloperMode
            }
        }
    }

    suspend fun setPremiumMode(isPremiumMode: Boolean) {
        userPreference.updateData {
            it.copy {
                this.isPremiumMode = isPremiumMode
            }
        }
    }

    suspend fun setShuffleMode(shuffleMode: ShuffleMode) {
        musicPreference.updateData {
            it.copy {
                this.shuffleMode = when (shuffleMode) {
                    ShuffleMode.ON -> ShuffleModeProto.SHUFFLE_ON
                    ShuffleMode.OFF -> ShuffleModeProto.SHUFFLE_OFF
                }
            }
        }
    }

    suspend fun setRepeatMode(repeatMode: RepeatMode) {
        musicPreference.updateData {
            it.copy {
                this.repeatMode = when (repeatMode) {
                    RepeatMode.ONE -> RepeatModeProto.REPEAT_ONE
                    RepeatMode.ALL -> RepeatModeProto.REPEAT_ALL
                    RepeatMode.OFF -> RepeatModeProto.REPEAT_OFF
                }
            }
        }
    }

    suspend fun setSongOrder(musicOrder: MusicOrder) {
        musicPreference.updateData {
            it.copy {
                this.songOrder = when (musicOrder.order) {
                    Order.ASC -> when (musicOrder.musicOrderOption) {
                        MusicOrderOption.Song.NAME -> SongOrderProto.SONG_ASC_NAME
                        MusicOrderOption.Song.ARTIST -> SongOrderProto.SONG_ASC_ARTIST
                        MusicOrderOption.Song.ALBUM -> SongOrderProto.SONG_ASC_ALBUM
                        MusicOrderOption.Song.DURATION -> SongOrderProto.SONG_ASC_DURATION
                        MusicOrderOption.Song.YEAR -> SongOrderProto.SONG_ASC_YEAR
                        else -> error("Invalid song order option. $musicOrder")
                    }

                    Order.DESC -> when (musicOrder.musicOrderOption) {
                        MusicOrderOption.Song.NAME -> SongOrderProto.SONG_DESC_NAME
                        MusicOrderOption.Song.ARTIST -> SongOrderProto.SONG_DESC_ARTIST
                        MusicOrderOption.Song.ALBUM -> SongOrderProto.SONG_DESC_ALBUM
                        MusicOrderOption.Song.DURATION -> SongOrderProto.SONG_DESC_DURATION
                        MusicOrderOption.Song.YEAR -> SongOrderProto.SONG_DESC_YEAR
                        else -> error("Invalid song order option. $musicOrder")
                    }
                }
            }
        }
    }

    suspend fun setArtistOrder(musicOrder: MusicOrder) {
        musicPreference.updateData {
            it.copy {
                this.artistOrder = when (musicOrder.order) {
                    Order.ASC -> when (musicOrder.musicOrderOption) {
                        MusicOrderOption.Artist.NAME -> ArtistOrderProto.ARTIST_ASC_NAME
                        MusicOrderOption.Artist.TRACKS -> ArtistOrderProto.ARTIST_ASC_NUM_TRACKS
                        MusicOrderOption.Artist.ALBUMS -> ArtistOrderProto.ARTIST_ASC_NUM_ALBUMS
                        else -> error("Invalid artist order option. $musicOrder")
                    }

                    Order.DESC -> when (musicOrder.musicOrderOption) {
                        MusicOrderOption.Artist.NAME -> ArtistOrderProto.ARTIST_DESC_NAME
                        MusicOrderOption.Artist.TRACKS -> ArtistOrderProto.ARTIST_DESC_NUM_TRACKS
                        MusicOrderOption.Artist.ALBUMS -> ArtistOrderProto.ARTIST_DESC_NUM_ALBUMS
                        else -> error("Invalid artist order option. $musicOrder")
                    }
                }
            }
        }
    }

    suspend fun setAlbumOrder(musicOrder: MusicOrder) {
        musicPreference.updateData {
            it.copy {
                this.albumOrder = when (musicOrder.order) {
                    Order.ASC -> when (musicOrder.musicOrderOption) {
                        MusicOrderOption.Album.NAME -> AlbumOrderProto.ALBUM_ASC_NAME
                        MusicOrderOption.Album.ARTIST -> AlbumOrderProto.ALBUM_ASC_ARTIST
                        MusicOrderOption.Album.TRACKS -> AlbumOrderProto.ALBUM_ASC_NUM_TRACKS
                        MusicOrderOption.Album.YEAR -> AlbumOrderProto.ALBUM_ASC_YEAR
                        else -> error("Invalid album order option. $musicOrder")
                    }
                    Order.DESC -> when (musicOrder.musicOrderOption) {
                        MusicOrderOption.Album.NAME -> AlbumOrderProto.ALBUM_DESC_NAME
                        MusicOrderOption.Album.ARTIST -> AlbumOrderProto.ALBUM_DESC_ARTIST
                        MusicOrderOption.Album.TRACKS -> AlbumOrderProto.ALBUM_DESC_NUM_TRACKS
                        MusicOrderOption.Album.YEAR -> AlbumOrderProto.ALBUM_DESC_YEAR
                        else -> error("Invalid album order option. $musicOrder")
                    }
                }
            }
        }
    }

    suspend fun setLastQueue(
        currentItems: List<Long>,
        originalItems: List<Long>,
        index: Int,
    ) {
        queuePreference.updateData {
            it.copy {
                this.originalItems.clear()
                this.originalItems.addAll(originalItems)

                this.currentItems.clear()
                this.currentItems.addAll(currentItems)

                this.index = index
            }
        }
    }

    suspend fun setLastQueueProgress(progress: Long) {
        queuePreference.updateData {
            it.copy {
                this.progress = progress
            }
        }
    }
}
