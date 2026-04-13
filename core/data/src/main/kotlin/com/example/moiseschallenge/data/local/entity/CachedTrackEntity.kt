package com.example.moiseschallenge.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moiseschallenge.domain.model.Track

@Entity(tableName = "cached_tracks")
data class CachedTrackEntity(
    @PrimaryKey
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
    val isExplicit: Boolean,
    val searchQuery: String,
    val cachedAt: Long = System.currentTimeMillis()
)

fun Track.toCachedEntity(searchQuery: String): CachedTrackEntity {
    return CachedTrackEntity(
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
        isExplicit = isExplicit,
        searchQuery = searchQuery.lowercase()
    )
}
