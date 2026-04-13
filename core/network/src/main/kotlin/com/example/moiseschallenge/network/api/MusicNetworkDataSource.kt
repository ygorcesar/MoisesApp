package com.example.moiseschallenge.network.api

import com.example.moiseschallenge.network.model.ITunesSearchResponse

/**
 * Network abstraction layer for music data.
 * This interface allows the API implementation to be replaced
 * without affecting other layers of the application.
 */
interface MusicNetworkDataSource {

    suspend fun searchMusic(
        query: String,
        limit: Int = 50,
        offset: Int = 0
    ): Result<ITunesSearchResponse>

    suspend fun lookupTrack(trackId: Long): Result<ITunesSearchResponse>

    suspend fun lookupAlbum(albumId: Long): Result<ITunesSearchResponse>

    suspend fun getAlbumTracks(albumId: Long): Result<ITunesSearchResponse>
}
