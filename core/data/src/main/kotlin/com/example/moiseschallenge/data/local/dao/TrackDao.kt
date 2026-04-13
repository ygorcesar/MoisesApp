package com.example.moiseschallenge.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moiseschallenge.data.local.entity.CachedTrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Query("SELECT * FROM cached_tracks WHERE searchQuery = :query ORDER BY trackName ASC")
    fun getTracksByQuery(query: String): Flow<List<CachedTrackEntity>>

    @Query("SELECT * FROM cached_tracks WHERE searchQuery = :query ORDER BY trackName ASC")
    fun getTracksByQueryPaging(query: String): PagingSource<Int, CachedTrackEntity>

    @Query("SELECT * FROM cached_tracks WHERE id = :trackId")
    suspend fun getTrackById(trackId: Long): CachedTrackEntity?

    @Query("SELECT * FROM cached_tracks WHERE collectionId = :albumId ORDER BY discNumber, trackNumber")
    fun getTracksByAlbumId(albumId: Long): Flow<List<CachedTrackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(tracks: List<CachedTrackEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: CachedTrackEntity)

    @Query("DELETE FROM cached_tracks WHERE searchQuery = :query")
    suspend fun deleteTracksByQuery(query: String)

    @Query("DELETE FROM cached_tracks WHERE cachedAt < :timestamp")
    suspend fun deleteOldCache(timestamp: Long)

    @Query("DELETE FROM cached_tracks")
    suspend fun clearAll()
}
