package caios.android.kanade.core.datastore

import androidx.datastore.core.DataStore
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.model.Order
import caios.android.kanade.core.model.ThemeColorConfig
import caios.android.kanade.core.model.ThemeConfig
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.model.music.LastQueue
import caios.android.kanade.core.model.player.MusicConfig
import caios.android.kanade.core.model.player.MusicOrder
import caios.android.kanade.core.model.player.MusicOrderOption
import caios.android.kanade.core.model.player.RepeatMode
import caios.android.kanade.core.model.player.ShuffleMode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class KanadePreferencesDataStore @Inject constructor(
    private val userPreference: DataStore<UserPreference>,
    private val musicPreference: DataStore<MusicPreference>,
    private val queuePreference: DataStore<QueuePreference>,
    @Dispatcher(KanadeDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
) {
    val userData = userPreference.data
        .map {
            UserData(
                kanadeId = it.kanadeId,
                themeConfig = when (it.themeConfig) {
                    ThemeConfigProto.THEME_CONFIG_LIGHT -> ThemeConfig.Light
                    ThemeConfigProto.THEME_CONFIG_DARK -> ThemeConfig.Dark
                    else -> ThemeConfig.System
                },
                themeColorConfig = when (it.themeColorConfig) {
                    ThemeColorConfigProto.THEME_COLOR_CONFIG_BLUE -> ThemeColorConfig.Blue
                    ThemeColorConfigProto.THEME_COLOR_CONFIG_BROWN -> ThemeColorConfig.Brown
                    ThemeColorConfigProto.THEME_COLOR_CONFIG_GREEN -> ThemeColorConfig.Green
                    ThemeColorConfigProto.THEME_COLOR_CONFIG_PURPLE -> ThemeColorConfig.Purple
                    ThemeColorConfigProto.THEME_COLOR_CONFIG_PINK -> ThemeColorConfig.Pink
                    else -> ThemeColorConfig.Blue
                },
                isDynamicColor = if (it.hasIsUseDynamicColor()) it.isUseDynamicColor else false,
                isDeveloperMode = if (it.hasIsDeveloperMode()) it.isDeveloperMode else false,
                isPlusMode = if (it.hasIsPlusMode()) it.isPlusMode else false,
                isDynamicNormalizer = if (it.hasIsUseDynamicNormalizer()) it.isUseDynamicNormalizer else false,
                isOneStepBack = if (it.hasIsUseOneStepBack()) it.isUseOneStepBack else true,
                isKeepAudioFocus = if (it.hasIsUseKeepAudioFocus()) it.isUseKeepAudioFocus else false,
                isStopWhenTaskkill = if (it.hasIsUseStopWhenTaskkill()) it.isUseStopWhenTaskkill else false,
                isIgnoreShortMusic = if (it.hasIsUseIgnoreShortMusic()) it.isUseIgnoreShortMusic else true,
                isIgnoreNotMusic = if (it.hasIsUseIgnoreNotMusic()) it.isUseIgnoreNotMusic else true,
                isAgreedPrivacyPolicy = if (it.hasIsAgreedPrivacyPolicy()) it.isAgreedPrivacyPolicy else false,
                isAgreedTermsOfService = if (it.hasIsAgreedTermsOfService()) it.isAgreedTermsOfService else false,
                isEnableYTMusic = if (it.hasIsEnableYtmusic()) it.isEnableYtmusic else false,
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

    suspend fun setKanadeId(id: String) = withContext(ioDispatcher) {
        userPreference.updateData {
            it.copy {
                this.kanadeId = id
            }
        }
    }

    suspend fun setThemeConfig(themeConfig: ThemeConfig) = withContext(ioDispatcher) {
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

    suspend fun setThemeColorConfig(themeColorConfig: ThemeColorConfig) = withContext(ioDispatcher) {
        userPreference.updateData {
            it.copy {
                this.themeColorConfig = when (themeColorConfig) {
                    ThemeColorConfig.Blue -> ThemeColorConfigProto.THEME_COLOR_CONFIG_BLUE
                    ThemeColorConfig.Brown -> ThemeColorConfigProto.THEME_COLOR_CONFIG_BROWN
                    ThemeColorConfig.Green -> ThemeColorConfigProto.THEME_COLOR_CONFIG_GREEN
                    ThemeColorConfig.Pink -> ThemeColorConfigProto.THEME_COLOR_CONFIG_PINK
                    ThemeColorConfig.Purple -> ThemeColorConfigProto.THEME_COLOR_CONFIG_PURPLE
                    ThemeColorConfig.Default -> ThemeColorConfigProto.THEME_COLOR_CONFIG_DEFAULT
                    else -> ThemeColorConfigProto.THEME_COLOR_CONFIG_BLUE
                }
            }
        }
    }

    suspend fun setUseDynamicColor(useDynamicColor: Boolean) = withContext(ioDispatcher) {
        userPreference.updateData {
            it.copy {
                this.isUseDynamicColor = useDynamicColor
            }
        }
    }

    suspend fun setDeveloperMode(isDeveloperMode: Boolean) = withContext(ioDispatcher) {
        userPreference.updateData {
            it.copy {
                this.isDeveloperMode = isDeveloperMode
            }
        }
    }

    suspend fun setPlusMode(isPlusMode: Boolean) = withContext(ioDispatcher) {
        userPreference.updateData {
            it.copy {
                this.isPlusMode = isPlusMode
            }
        }
    }

    suspend fun setUseDynamicNormalizer(useDynamicNormalizer: Boolean) = withContext(ioDispatcher) {
        userPreference.updateData {
            it.copy {
                this.isUseDynamicNormalizer = useDynamicNormalizer
            }
        }
    }

    suspend fun setUseOneStepBack(useOneStepBack: Boolean) = withContext(ioDispatcher) {
        userPreference.updateData {
            it.copy {
                this.isUseOneStepBack = useOneStepBack
            }
        }
    }

    suspend fun setUseKeepAudioFocus(useKeepAudioFocus: Boolean) = withContext(ioDispatcher) {
        userPreference.updateData {
            it.copy {
                this.isUseKeepAudioFocus = useKeepAudioFocus
            }
        }
    }

    suspend fun setUseStopWhenTaskkill(useStopWhenTaskkill: Boolean) = withContext(ioDispatcher) {
        userPreference.updateData {
            it.copy {
                this.isUseStopWhenTaskkill = useStopWhenTaskkill
            }
        }
    }

    suspend fun setUseIgnoreShortMusic(useIgnoreShortMusic: Boolean) = withContext(ioDispatcher) {
        userPreference.updateData {
            it.copy {
                this.isUseIgnoreShortMusic = useIgnoreShortMusic
            }
        }
    }

    suspend fun setUseIgnoreNotMusic(useIgnoreNotMusic: Boolean) = withContext(ioDispatcher) {
        userPreference.updateData {
            it.copy {
                this.isUseIgnoreNotMusic = useIgnoreNotMusic
            }
        }
    }

    suspend fun setAgreedPrivacyPolicy(isAgreedPrivacyPolicy: Boolean) = withContext(ioDispatcher) {
        userPreference.updateData {
            it.copy {
                this.isAgreedPrivacyPolicy = isAgreedPrivacyPolicy
            }
        }
    }

    suspend fun setAgreedTermsOfService(isAgreedTermsOfService: Boolean) = withContext(ioDispatcher) {
        userPreference.updateData {
            it.copy {
                this.isAgreedTermsOfService = isAgreedTermsOfService
            }
        }
    }

    suspend fun setEnableYTMusic(isEnableYTMusic: Boolean) = withContext(ioDispatcher) {
        userPreference.updateData {
            it.copy {
                this.isEnableYtmusic = isEnableYTMusic
            }
        }
    }

    suspend fun setShuffleMode(shuffleMode: ShuffleMode) = withContext(ioDispatcher) {
        musicPreference.updateData {
            it.copy {
                this.shuffleMode = when (shuffleMode) {
                    ShuffleMode.ON -> ShuffleModeProto.SHUFFLE_ON
                    ShuffleMode.OFF -> ShuffleModeProto.SHUFFLE_OFF
                }
            }
        }
    }

    suspend fun setRepeatMode(repeatMode: RepeatMode) = withContext(ioDispatcher) {
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

    suspend fun setSongOrder(musicOrder: MusicOrder) = withContext(ioDispatcher) {
        musicPreference.updateData {
            it.copy {
                this.songOrder = when (musicOrder.order) {
                    Order.ASC -> when (musicOrder.option) {
                        MusicOrderOption.Song.NAME -> SongOrderProto.SONG_ASC_NAME
                        MusicOrderOption.Song.ARTIST -> SongOrderProto.SONG_ASC_ARTIST
                        MusicOrderOption.Song.ALBUM -> SongOrderProto.SONG_ASC_ALBUM
                        MusicOrderOption.Song.DURATION -> SongOrderProto.SONG_ASC_DURATION
                        MusicOrderOption.Song.YEAR -> SongOrderProto.SONG_ASC_YEAR
                        MusicOrderOption.Song.TRACK -> SongOrderProto.SONG_ASC_TRACK
                        else -> error("Invalid song order option. $musicOrder")
                    }

                    Order.DESC -> when (musicOrder.option) {
                        MusicOrderOption.Song.NAME -> SongOrderProto.SONG_DESC_NAME
                        MusicOrderOption.Song.ARTIST -> SongOrderProto.SONG_DESC_ARTIST
                        MusicOrderOption.Song.ALBUM -> SongOrderProto.SONG_DESC_ALBUM
                        MusicOrderOption.Song.DURATION -> SongOrderProto.SONG_DESC_DURATION
                        MusicOrderOption.Song.YEAR -> SongOrderProto.SONG_DESC_YEAR
                        MusicOrderOption.Song.TRACK -> SongOrderProto.SONG_DESC_TRACK
                        else -> error("Invalid song order option. $musicOrder")
                    }
                }
            }
        }
    }

    suspend fun setArtistOrder(musicOrder: MusicOrder) = withContext(ioDispatcher) {
        musicPreference.updateData {
            it.copy {
                this.artistOrder = when (musicOrder.order) {
                    Order.ASC -> when (musicOrder.option) {
                        MusicOrderOption.Artist.NAME -> ArtistOrderProto.ARTIST_ASC_NAME
                        MusicOrderOption.Artist.TRACKS -> ArtistOrderProto.ARTIST_ASC_NUM_TRACKS
                        MusicOrderOption.Artist.ALBUMS -> ArtistOrderProto.ARTIST_ASC_NUM_ALBUMS
                        else -> error("Invalid artist order option. $musicOrder")
                    }

                    Order.DESC -> when (musicOrder.option) {
                        MusicOrderOption.Artist.NAME -> ArtistOrderProto.ARTIST_DESC_NAME
                        MusicOrderOption.Artist.TRACKS -> ArtistOrderProto.ARTIST_DESC_NUM_TRACKS
                        MusicOrderOption.Artist.ALBUMS -> ArtistOrderProto.ARTIST_DESC_NUM_ALBUMS
                        else -> error("Invalid artist order option. $musicOrder")
                    }
                }
            }
        }
    }

    suspend fun setAlbumOrder(musicOrder: MusicOrder) = withContext(ioDispatcher) {
        musicPreference.updateData {
            it.copy {
                this.albumOrder = when (musicOrder.order) {
                    Order.ASC -> when (musicOrder.option) {
                        MusicOrderOption.Album.NAME -> AlbumOrderProto.ALBUM_ASC_NAME
                        MusicOrderOption.Album.ARTIST -> AlbumOrderProto.ALBUM_ASC_ARTIST
                        MusicOrderOption.Album.TRACKS -> AlbumOrderProto.ALBUM_ASC_NUM_TRACKS
                        MusicOrderOption.Album.YEAR -> AlbumOrderProto.ALBUM_ASC_YEAR
                        else -> error("Invalid album order option. $musicOrder")
                    }
                    Order.DESC -> when (musicOrder.option) {
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

    suspend fun setPlaylistOrder(musicOrder: MusicOrder) = withContext(ioDispatcher) {
        musicPreference.updateData {
            it.copy {
                this.playlistOrder = when (musicOrder.order) {
                    Order.ASC -> when (musicOrder.option) {
                        MusicOrderOption.Playlist.NAME -> PlaylistOrderProto.PLAYLIST_ASC_NAME
                        MusicOrderOption.Playlist.TRACKS -> PlaylistOrderProto.PLAYLIST_ASC_NUM_TRACKS
                        else -> error("Invalid playlist order option. $musicOrder")
                    }
                    Order.DESC -> when (musicOrder.option) {
                        MusicOrderOption.Playlist.NAME -> PlaylistOrderProto.PLAYLIST_DESC_NAME
                        MusicOrderOption.Playlist.TRACKS -> PlaylistOrderProto.PLAYLIST_DESC_NUM_TRACKS
                        else -> error("Invalid playlist order option. $musicOrder")
                    }
                }
            }
        }
    }

    suspend fun setLastQueue(
        currentItems: List<Long>,
        originalItems: List<Long>,
        index: Int,
    ) = withContext(ioDispatcher) {
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

    suspend fun setLastQueueProgress(progress: Long) = withContext(ioDispatcher) {
        queuePreference.updateData {
            it.copy {
                this.progress = progress
            }
        }
    }
}
