package caios.android.kanade.core.model.entity


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MusixmatchSongsEntity(
    @SerialName("message")
    val message: Message
) {
    @Serializable
    data class Message(
        @SerialName("body")
        val body: Body,
        @SerialName("header")
        val header: Header
    ) {
        @Serializable
        data class Body(
            @SerialName("track_list")
            val trackList: List<Track>
        ) {
            @Serializable
            data class Track(
                @SerialName("track")
                val track: Track
            ) {
                @Serializable
                data class Track(
                    @SerialName("album_coverart_100x100")
                    val albumCoverart100x100: String,
                    @SerialName("album_coverart_350x350")
                    val albumCoverart350x350: String,
                    @SerialName("album_coverart_500x500")
                    val albumCoverart500x500: String,
                    @SerialName("album_coverart_800x800")
                    val albumCoverart800x800: String,
                    @SerialName("album_id")
                    val albumId: Long,
                    @SerialName("album_name")
                    val albumName: String,
                    @SerialName("artist_id")
                    val artistId: Long,
                    @SerialName("artist_mbid")
                    val artistMbid: String,
                    @SerialName("artist_name")
                    val artistName: String,
                    @SerialName("commontrack_id")
                    val commontrackId: Long,
                    @SerialName("commontrack_isrcs")
                    val commontrackIsrcs: List<List<String>>,
                    @SerialName("commontrack_spotify_ids")
                    val commontrackSpotifyIds: List<String>,
                    @SerialName("commontrack_vanity_id")
                    val commontrackVanityId: String,
                    @SerialName("explicit")
                    val explicit: Int,
                    @SerialName("first_release_date")
                    val firstReleaseDate: String,
                    @SerialName("has_lyrics")
                    val hasLyrics: Int,
                    @SerialName("has_lyrics_crowd")
                    val hasLyricsCrowd: Int,
                    @SerialName("has_richsync")
                    val hasRichsync: Int,
                    @SerialName("has_subtitles")
                    val hasSubtitles: Int,
                    @SerialName("has_track_structure")
                    val hasTrackStructure: Int,
                    @SerialName("instrumental")
                    val instrumental: Int,
                    @SerialName("lyrics_id")
                    val lyricsId: Long,
                    @SerialName("num_favourite")
                    val numFavourite: Int,
                    @SerialName("primary_genres")
                    val primaryGenres: PrimaryGenres,
                    @SerialName("restricted")
                    val restricted: Int,
                    @SerialName("secondary_genres")
                    val secondaryGenres: SecondaryGenres,
                    @SerialName("subtitle_id")
                    val subtitleId: Long,
                    @SerialName("track_edit_url")
                    val trackEditUrl: String,
                    @SerialName("track_id")
                    val trackId: Long,
                    @SerialName("track_isrc")
                    val trackIsrc: String,
                    @SerialName("track_length")
                    val trackLength: Int,
                    @SerialName("track_mbid")
                    val trackMbid: String,
                    @SerialName("track_name")
                    val trackName: String,
                    @SerialName("track_name_translation_list")
                    val trackNameTranslationList: List<TrackNameTranslation>,
                    @SerialName("track_rating")
                    val trackRating: Int,
                    @SerialName("track_share_url")
                    val trackShareUrl: String,
                    @SerialName("track_soundcloud_id")
                    val trackSoundcloudId: Long,
                    @SerialName("track_spotify_id")
                    val trackSpotifyId: String,
                    @SerialName("track_xboxmusic_id")
                    val trackXboxmusicId: String,
                    @SerialName("updated_time")
                    val updatedTime: String
                ) {
                    @Serializable
                    data class PrimaryGenres(
                        @SerialName("music_genre_list")
                        val musicGenreList: List<MusicGenre>
                    ) {
                        @Serializable
                        data class MusicGenre(
                            @SerialName("music_genre")
                            val musicGenre: MusicGenre
                        ) {
                            @Serializable
                            data class MusicGenre(
                                @SerialName("music_genre_id")
                                val musicGenreId: Long,
                                @SerialName("music_genre_name")
                                val musicGenreName: String,
                                @SerialName("music_genre_name_extended")
                                val musicGenreNameExtended: String,
                                @SerialName("music_genre_parent_id")
                                val musicGenreParentId: Long,
                                @SerialName("music_genre_vanity")
                                val musicGenreVanity: String
                            )
                        }
                    }

                    @Serializable
                    data class SecondaryGenres(
                        @SerialName("music_genre_list")
                        val musicGenreList: List<MusicGenre>
                    ) {
                        @Serializable
                        data class MusicGenre(
                            @SerialName("music_genre")
                            val musicGenre: MusicGenre
                        ) {
                            @Serializable
                            data class MusicGenre(
                                @SerialName("music_genre_id")
                                val musicGenreId: Long,
                                @SerialName("music_genre_name")
                                val musicGenreName: String,
                                @SerialName("music_genre_name_extended")
                                val musicGenreNameExtended: String,
                                @SerialName("music_genre_parent_id")
                                val musicGenreParentId: Long,
                                @SerialName("music_genre_vanity")
                                val musicGenreVanity: String
                            )
                        }
                    }

                    @Serializable
                    data class TrackNameTranslation(
                        @SerialName("track_name_translation")
                        val trackNameTranslation: TrackNameTranslation
                    ) {
                        @Serializable
                        data class TrackNameTranslation(
                            @SerialName("language")
                            val language: String,
                            @SerialName("translation")
                            val translation: String
                        )
                    }
                }
            }
        }

        @Serializable
        data class Header(
            @SerialName("available")
            val available: Int,
            @SerialName("execute_time")
            val executeTime: Double,
            @SerialName("status_code")
            val statusCode: Int
        )
    }
}
