package caios.android.kanade.core.model

sealed class ScreenState<out T> {
    data object Loading : ScreenState<Nothing>()

    data class Error(
        val message: Int,
        val retryTitle: Int? = null,
    ) : ScreenState<Nothing>()

    data class Idle<T>(
        var data: T,
    ) : ScreenState<T>()
}
