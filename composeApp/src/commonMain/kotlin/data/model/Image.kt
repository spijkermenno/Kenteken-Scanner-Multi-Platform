package data.model

import kotlinx.serialization.Serializable

@Serializable
data class Image(
    private val file_path: String
) {
    fun getFilePath(): String = file_path.replace("http", "https")
}