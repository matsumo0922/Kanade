package caios.android.kanade.core.model

import java.time.LocalDate

data class Version(
    val name: String,
    val code: Int,
    val message: String,
    val date: LocalDate,
)
