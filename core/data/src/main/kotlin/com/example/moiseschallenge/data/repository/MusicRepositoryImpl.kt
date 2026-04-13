package com.example.moiseschallenge.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moiseschallenge.data.local.dao.TrackDao
import com.example.moiseschallenge.data.local.entity.toCachedEntity
import com.example.moiseschallenge.data.mapper.toAlbum
import com.example.moiseschallenge.data.mapper.toTrack
import com.example.moiseschallenge.data.mapper.toTracks
import com.example.moiseschallenge.data.paging.TracksPagingSource
import com.example.moiseschallenge.domain.model.Album
import com.example.moiseschallenge.domain.model.Track
import com.example.moiseschallenge.domain.repository.MusicRepository
import com.example.moiseschallenge.network.api.MusicNetworkDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val networkDataSource: MusicNetworkDataSource,
    private val trackDao: TrackDao
) : MusicRepository {

    override fun searchTracks(query: String): Flow<PagingData<Track>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = {
                TracksPagingSource(networkDataSource, query)
            }
        ).flow
    }

    override suspend fun searchTracksOneShot(query: String, limit: Int): Result<List<Track>> {
        return networkDataSource.searchMusic(query, limit).map { response ->
            val tracks = response.results.toTracks()

            val cachedEntities = tracks.map { it.toCachedEntity(query.lowercase()) }
            trackDao.deleteTracksByQuery(query.lowercase())
            trackDao.insertTracks(cachedEntities)

            tracks
        }
    }

    override suspend fun getAlbumTracks(albumId: Long): Result<List<Track>> {
        return networkDataSource.getAlbumTracks(albumId).map { response ->
            response.results
                .filter { it.isTrack }
                .mapNotNull { it.toTrack() }
                .sortedWith(compareBy({ it.discNumber }, { it.trackNumber }))
        }
    }

    override suspend fun getAlbumDetails(albumId: Long): Result<Album> {
        return networkDataSource.lookupAlbum(albumId).mapCatching { response ->
            response.results
                .firstOrNull { it.isAlbum }
                ?.toAlbum()
                ?: throw NoSuchElementException("Album not found")
        }
    }

    override suspend fun lookupTrack(trackId: Long): Result<Track> {
        return networkDataSource.lookupTrack(trackId).mapCatching { response ->
            response.results
                .firstOrNull { it.isTrack }
                ?.toTrack()
                ?: throw NoSuchElementException("Track not found")
        }
    }

    companion object {
        private const val PAGE_SIZE = 25
    }
}
