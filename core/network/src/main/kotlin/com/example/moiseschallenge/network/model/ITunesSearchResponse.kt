package com.example.moiseschallenge.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ITunesSearchResponse(
    @SerialName("resultCount")
    val resultCount: Int,
    @SerialName("results")
    val results: List<ITunesResult>
)

@Serializable
data class ITunesResult(
    @SerialName("wrapperType")
    val wrapperType: String? = null,
    @SerialName("kind")
    val kind: String? = null,
    @SerialName("trackId")
    val trackId: Long? = null,
    @SerialName("artistId")
    val artistId: Long? = null,
    @SerialName("collectionId")
    val collectionId: Long? = null,
    @SerialName("trackName")
    val trackName: String? = null,
    @SerialName("artistName")
    val artistName: String? = null,
    @SerialName("collectionName")
    val collectionName: String? = null,
    @SerialName("collectionCensoredName")
    val collectionCensoredName: String? = null,
    @SerialName("trackCensoredName")
    val trackCensoredName: String? = null,
    @SerialName("artistViewUrl")
    val artistViewUrl: String? = null,
    @SerialName("collectionViewUrl")
    val collectionViewUrl: String? = null,
    @SerialName("trackViewUrl")
    val trackViewUrl: String? = null,
    @SerialName("previewUrl")
    val previewUrl: String? = null,
    @SerialName("artworkUrl30")
    val artworkUrl30: String? = null,
    @SerialName("artworkUrl60")
    val artworkUrl60: String? = null,
    @SerialName("artworkUrl100")
    val artworkUrl100: String? = null,
    @SerialName("collectionPrice")
    val collectionPrice: Double? = null,
    @SerialName("trackPrice")
    val trackPrice: Double? = null,
    @SerialName("releaseDate")
    val releaseDate: String? = null,
    @SerialName("collectionExplicitness")
    val collectionExplicitness: String? = null,
    @SerialName("trackExplicitness")
    val trackExplicitness: String? = null,
    @SerialName("discCount")
    val discCount: Int? = null,
    @SerialName("discNumber")
    val discNumber: Int? = null,
    @SerialName("trackCount")
    val trackCount: Int? = null,
    @SerialName("trackNumber")
    val trackNumber: Int? = null,
    @SerialName("trackTimeMillis")
    val trackTimeMillis: Long? = null,
    @SerialName("country")
    val country: String? = null,
    @SerialName("currency")
    val currency: String? = null,
    @SerialName("primaryGenreName")
    val primaryGenreName: String? = null,
    @SerialName("isStreamable")
    val isStreamable: Boolean? = null
) {
    val isTrack: Boolean
        get() = wrapperType == "track" && (kind == "song" || kind == "music-video")

    val isAlbum: Boolean
        get() = wrapperType == "collection" && collectionId != null

    val isExplicit: Boolean
        get() = trackExplicitness == "explicit" || collectionExplicitness == "explicit"
}
