package caios.android.kanade.core.repository.util

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> HttpResponse.parse(
    allowRange: IntRange = 200..299,
    f: ((T?) -> (Unit)) = {},
): T? {
    return (if (this.status.value in allowRange) this.body<T>() else null).also(f)
}

fun HttpResponse.isSuccess(allowRange: IntRange = 200..299): Boolean {
    return (this.status.value in allowRange)
}
