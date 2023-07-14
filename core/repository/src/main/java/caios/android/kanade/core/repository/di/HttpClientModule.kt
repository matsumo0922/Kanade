package caios.android.kanade.core.repository.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HttpClientModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
            install(Logging) {
                level = LogLevel.HEADERS
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.d(message)
                    }
                }
            }

            install(HttpCookies) {
                storage = AcceptAllCookiesStorage()
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        isLenient = true
                        prettyPrint = true
                        ignoreUnknownKeys = true
                        coerceInputValues = true
                        encodeDefaults = true
                    },
                )
            }
        }
    }
}
