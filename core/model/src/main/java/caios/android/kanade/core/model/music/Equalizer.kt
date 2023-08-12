package caios.android.kanade.core.model.music

data class Equalizer(
    val hzs: Map<Int, Float>,
    val bassBoost: Float,
)
