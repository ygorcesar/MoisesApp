package com.example.moiseschallenge.network.api

import com.example.moiseschallenge.network.model.ITunesSearchResponse
import javax.inject.Inject

/**
 * iTunes implementation of the MusicNetworkDataSource.
 * This can be swapped with another implementation (e.g., Spotify, Deezer)
 * without affecting the rest of the application.
 */
class ITunesNetworkDataSource @Inject constructor(
    private val apiService: ITunesApiService
) : MusicNetworkDataSource {

    override suspend fun searchMusic(
        query: String,
        limit: Int,
        offset: Int
    ): Result<ITunesSearchResponse> {
        return runCatching {
            apiService.search(
                term = query,
                limit = limit,
                offset = offset
            )
        }
    }

    override suspend fun lookupTrack(trackId: Long): Result<ITunesSearchResponse> {
        return runCatching {
            apiService.lookup(id = trackId)
        }
    }

    override suspend fun lookupAlbum(albumId: Long): Result<ITunesSearchResponse> {
        return runCatching {
            apiService.lookup(id = albumId)
        }
    }

    override suspend fun getAlbumTracks(albumId: Long): Result<ITunesSearchResponse> {
        return runCatching {
            apiService.lookupAlbumTracks(albumId = albumId)
        }
    }
}
