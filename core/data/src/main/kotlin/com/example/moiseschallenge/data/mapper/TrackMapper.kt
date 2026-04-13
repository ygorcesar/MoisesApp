package com.example.moiseschallenge.data.mapper

import com.example.moiseschallenge.domain.model.Album
import com.example.moiseschallenge.domain.model.Track
import com.example.moiseschallenge.network.model.ITunesResult

fun ITunesResult.toTrack(): Track? {
    val trackIdValue = trackId ?: return null
    val trackNameValue = trackName ?: return null
    val artistNameValue = artistName ?: return null

    if (!isTrack) return null

    return Track(
        id = trackIdValue,
        trackName = trackNameValue,
        artistName = artistNameValue,
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
}

fun ITunesResult.toAlbum(): Album? {
    val collectionIdValue = collectionId ?: return null
    val collectionNameValue = collectionName ?: return null
    val artistNameValue = artistName ?: return null

    if (!isAlbum) return null

    return Album(
        id = collectionIdValue,
        collectionName = collectionNameValue,
        artistName = artistNameValue,
        artworkUrl100 = artworkUrl100,
        trackCount = trackCount,
        releaseDate = releaseDate,
        primaryGenreName = primaryGenreName,
        collectionPrice = collectionPrice,
        currency = currency
    )
}

fun List<ITunesResult>.toTracks(): List<Track> {
    return mapNotNull { it.toTrack() }
}
