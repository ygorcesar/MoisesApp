package com.example.moiseschallenge.ui.util

import com.example.moiseschallenge.domain.model.Album
import com.example.moiseschallenge.domain.model.Track
import com.example.moiseschallenge.ui.model.toUiModel

object PreviewDataMocks {

    val previewAlbum = Album(
        id = 1,
        collectionName = "A Night at the Opera",
        artistName = "Queen",
        artworkUrl100 = null,
        primaryGenreName = "Rock",
        trackCount = 12,
        releaseDate = "1975-11-21",
        collectionPrice = null,
        currency = null
    )

    val previewTrack = Track(
        id = 1,
        trackName = "Bohemian Rhapsody",
        artistName = "Queen",
        collectionName = "A Night at the Opera",
        artworkUrl100 = null,
        previewUrl = null,
        trackTimeMillis = 354000,
        collectionId = 1,
        artistId = 1,
        trackNumber = 1,
        discNumber = 1,
        releaseDate = "1975-10-31",
        primaryGenreName = "Rock"
    )

    val previewTrackUiModel = previewTrack.toUiModel()

    val previewTracks = listOf(
        previewTrack,
        Track(
            id = 2,
            trackName = "You're My Best Friend",
            artistName = "Queen",
            collectionName = "A Night at the Opera",
            artworkUrl100 = null,
            previewUrl = null,
            trackTimeMillis = 169000,
            collectionId = 1,
            artistId = 1,
            trackNumber = 2,
            discNumber = 1,
            releaseDate = "1975-10-31",
            primaryGenreName = "Rock"
        ),
        Track(
            id = 3,
            trackName = "Love of My Life",
            artistName = "Queen",
            collectionName = "A Night at the Opera",
            artworkUrl100 = null,
            previewUrl = null,
            trackTimeMillis = 219000,
            collectionId = 1,
            artistId = 1,
            trackNumber = 3,
            discNumber = 1,
            releaseDate = "1975-10-31",
            primaryGenreName = "Rock"
        )
    )

    val previewTracksItems2 = listOf(
        Track(
            id = 1,
            trackName = "Purple Rain",
            artistName = "Prince",
            collectionName = "Purple Rain",
            artworkUrl100 = null,
            previewUrl = null,
            trackTimeMillis = 520000,
            collectionId = 1,
            artistId = 1,
            trackNumber = 1,
            discNumber = 1,
            releaseDate = "1984-06-25",
            primaryGenreName = "Pop"
        ),
        Track(
            id = 2,
            trackName = "Power Of Equality",
            artistName = "Red Hot Chili Peppers",
            collectionName = "Blood Sugar Sex Magik",
            artworkUrl100 = null,
            previewUrl = null,
            trackTimeMillis = 245000,
            collectionId = 2,
            artistId = 2,
            trackNumber = 1,
            discNumber = 1,
            releaseDate = "1991-09-24",
            primaryGenreName = "Rock"
        ),
        Track(
            id = 3,
            trackName = "Something",
            artistName = "The Beatles",
            collectionName = "Abbey Road",
            artworkUrl100 = null,
            previewUrl = null,
            trackTimeMillis = 182000,
            collectionId = 3,
            artistId = 3,
            trackNumber = 2,
            discNumber = 1,
            releaseDate = "1969-09-26",
            primaryGenreName = "Rock"
        ),
        Track(
            id = 4,
            trackName = "Like A Virgin",
            artistName = "Madonna",
            collectionName = "Like a Virgin",
            artworkUrl100 = null,
            previewUrl = null,
            trackTimeMillis = 218000,
            collectionId = 4,
            artistId = 4,
            trackNumber = 1,
            discNumber = 1,
            releaseDate = "1984-11-12",
            primaryGenreName = "Pop"
        ),
        Track(
            id = 5,
            trackName = "Get Lucky",
            artistName = "Daft Punk feat. Pharrell Williams",
            collectionName = "Random Access Memories",
            artworkUrl100 = null,
            previewUrl = null,
            trackTimeMillis = 369000,
            collectionId = 5,
            artistId = 5,
            trackNumber = 8,
            discNumber = 1,
            releaseDate = "2013-05-17",
            primaryGenreName = "Electronic"
        )
    )
}