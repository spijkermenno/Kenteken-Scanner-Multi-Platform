package domain.ktor

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess

suspend inline fun <reified T> HttpResponse.validate(): Result<T> {
    return if (this.status.isSuccess()) {
        val data = this.body<T>()
        Result.Success(data)
    } else {
        val errorBody = this.bodyAsText()
        Result.Error(Exception("HTTP error ${this.status.value}"), errorBody)
    }
}