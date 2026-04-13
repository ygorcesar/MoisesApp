package com.example.moiseschallenge.domain.model

data class Album(
    val id: Long,
    val collectionName: String,
    val artistName: String,
    val artworkUrl100: String?,
    val trackCount: Int?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val collectionPrice: Double?,
    val currency: String?
) {
    val artworkUrlHighRes: String?
        get() = artworkUrl100?.replace("100x100", "600x600")
}
