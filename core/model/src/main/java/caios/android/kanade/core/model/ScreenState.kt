package caios.android.kanade.core.model

sealed class ScreenState<out T> {
    object Loading : ScreenState<Nothing>()

    data class Error(
        val message: Int,
        val action: (() -> Unit)? = null,
    ) : ScreenState<Nothing>()

    data class Idle<T>(
        val data: T,
    ) : ScreenState<T>()
}
