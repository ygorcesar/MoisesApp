package com.example.moiseschallenge.ui.model

import androidx.compose.runtime.Immutable
import com.example.moiseschallenge.domain.model.Track

@Immutable
data class TrackUiModel(
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

    fun toDomain(): Track = Track(
        id = id,
        trackName = trackName,
        artistName = artistName,
        collectionName = collectionName,
        artworkUrl100 = artworkUrl100,
        previewUrl = previewUrl,
        trackTimeMillis = trackTimeMillis,
        collectionId = collectionId,
        artistId = artistId,
        trackNumber = trackNumber,
        discNumber = discNumber,
        releaseDate = releaseDate,
        primaryGenreName = primaryGenreName,
        isExplicit = isExplicit
    )

    companion object {
        fun from(track: Track): TrackUiModel = TrackUiModel(
            id = track.id,
            trackName = track.trackName,
            artistName = track.artistName,
            collectionName = track.collectionName,
            artworkUrl100 = track.artworkUrl100,
            previewUrl = track.previewUrl,
            trackTimeMillis = track.trackTimeMillis,
            collectionId = track.collectionId,
            artistId = track.artistId,
            trackNumber = track.trackNumber,
            discNumber = track.discNumber,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            isExplicit = track.isExplicit
        )
    }
}

fun Track.toUiModel(): TrackUiModel = TrackUiModel.from(this)
