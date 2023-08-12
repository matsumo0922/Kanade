package caios.android.kanade.core.model.music

data class Equalizer(
    val bands: List<Band>,
    val bassBoost: Float,
    val preset: Preset,
) {
    data class Band(
        val hz: Int,
        val band: Int,
        val value: Float,
        val maxLevel: Short,
        val minLevel: Short,
    )

    enum class Preset(val gains: List<Int>) {
        NONE(listOf(0, 0, 0, 0, 0)),
        CLASSICAL(listOf(5, 3, -2, 4, 4)),
        DANCE(listOf(6, 0, 2, 4, 1)),
        HEAVY_METAL(listOf(4, 1, 9, 3, 0)),
        HIP_HOP(listOf(5, 3, 0, 1, 3)),
        JAZZ(listOf(4, 2, -2, 2, 5)),
        POP(listOf(-1, 2, 5, 1, -2)),
        ROCK(listOf(5, 3, -1, 3, 5)),
        CUSTOM(listOf(0, 0, 0, 0, 0))
    }
}
