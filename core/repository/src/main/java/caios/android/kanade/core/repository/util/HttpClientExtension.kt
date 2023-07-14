package caios.android.kanade.core.repository.util

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import timber.log.Timber

suspend inline fun <reified T> HttpResponse.parse(
    allowRange: IntRange = 200..299,
    f: ((T?) -> (Unit)) = {},
): T? {
    val requestUrl = request.url
    val isOK = this.status.value in allowRange

    Timber.d("[${if (isOK) "SUCCESS" else "FAILED"}] Ktor Request: $requestUrl")

    return (if (isOK) this.body<T>() else null).also(f)
}

fun HttpResponse.isSuccess(allowRange: IntRange = 200..299): Boolean {
    return (this.status.value in allowRange)
}
