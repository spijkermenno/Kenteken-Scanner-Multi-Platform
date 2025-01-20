package domain.ktor

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error<out T>(val exception: Throwable, val message: String? = null) : Result<T>()
}