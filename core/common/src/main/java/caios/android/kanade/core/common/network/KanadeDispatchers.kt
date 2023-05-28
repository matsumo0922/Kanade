package caios.android.kanade.core.common.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val kanadeDispatcher: KanadeDispatcher)

enum class KanadeDispatcher {
    Default,
    IO,
}
