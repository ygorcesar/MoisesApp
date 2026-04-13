package com.example.moiseschallenge.domain.model

data class Track(
    val id: Long,
    val trackName: String,
    val artistName: String,
    val collectionName: String?,
    val artworkUrl100: String?,
    val previewUrl: String?,
    val trackTimeMillis: Long?,
    val collectionId: Long?,
    val artistId: Long?,
    val trackNumber: Int?,
    val discNumber: Int?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val isExplicit: Boolean = false
) {
    val artworkUrlHighRes: String?
        get() = artworkUrl100?.replace("100x100", "600x600")
}
