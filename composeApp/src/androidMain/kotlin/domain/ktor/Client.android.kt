package domain.ktor

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

actual val client: HttpClient
    get() = HttpClient(OkHttp) {
        install(HttpTimeout) {
            socketTimeoutMillis = 60_000
            requestTimeoutMillis = 60_000
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("HttpClient", message)
                }
            }
            level = LogLevel.ALL
        }

        defaultRequest {
            header("Content-Type", "application/json")
            header("Authorization", "")
            url("https://pixelwonders.nl/api/")
        }

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        HttpResponseValidator {
            validateResponse { response: HttpResponse ->
                val statusCode = response.status.value
                if (statusCode !in 200..299) {
                    val errorBody = response.bodyAsText()
                    Log.e("HttpClient", "Error response: $statusCode, body: $errorBody") }
            }
        }
    }