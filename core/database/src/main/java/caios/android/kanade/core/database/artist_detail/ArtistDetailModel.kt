package caios.android.kanade.core.database.artist_detail

import androidx.room.Embedded
import androidx.room.Relation

class ArtistDetailModel {
    @Embedded
    lateinit var artistDetail: ArtistDetailEntity

    @Relation(
        parentColumn = "artist_id",
        entityColumn = "artist_id",
    )
    lateinit var similarArtists: List<SimilarArtistEntity>

    @Relation(
        parentColumn = "artist_id",
        entityColumn = "artist_id",
    )
    lateinit var tags: List<ArtistTagEntity>
}