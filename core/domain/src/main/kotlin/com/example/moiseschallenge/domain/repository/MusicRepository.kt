package com.example.moiseschallenge.domain.repository

import androidx.paging.PagingData
import com.example.moiseschallenge.domain.model.Album
import com.example.moiseschallenge.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    fun searchTracks(query: String): Flow<PagingData<Track>>

    suspend fun searchTracksOneShot(query: String, limit: Int = 50): Result<List<Track>>

    suspend fun getAlbumTracks(albumId: Long): Result<List<Track>>

    suspend fun getAlbumDetails(albumId: Long): Result<Album>

    suspend fun lookupTrack(trackId: Long): Result<Track>
}
