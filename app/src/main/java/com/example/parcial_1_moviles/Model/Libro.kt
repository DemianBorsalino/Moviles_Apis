package com.example.parcial_1_moviles.Model

import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Libro(
    @SerializedName("docs") val docs: List<DocDto> = emptyList()
)

data class DocDto(
    @SerializedName("key") val key: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("author_name") val authorName: List<String>? = null,
    @SerializedName("first_publish_year") val firstPublishYear: Int? = null,
    @SerializedName("cover_i") val coverId: Int? = null
)

// Domain model
@Parcelize
data class Book(
    val id: String,
    val title: String,
    val author: String?,
    val year: Int?,
    val coverUrl: String?
) : Parcelable

// Mapper
fun DocDto.toDomain(): Book {
    val workId = this.key?.removePrefix("/works/") ?: (this.coverId?.toString() ?: this.title.orEmpty())
    val cover = this.coverId?.let { "https://covers.openlibrary.org/b/id/$it-M.jpg" }
    return Book(
        id = workId,
        title = this.title.orEmpty(),
        author = this.authorName?.firstOrNull(),
        year = this.firstPublishYear,
        coverUrl = cover
    )
}
