package com.example.moiseschallenge.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moiseschallenge.data.mapper.toTracks
import com.example.moiseschallenge.domain.model.Track
import com.example.moiseschallenge.network.api.MusicNetworkDataSource

class TracksPagingSource(
    private val networkDataSource: MusicNetworkDataSource,
    private val query: String
) : PagingSource<Int, Track>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Track> {
        val offset = params.key ?: 0
        val limit = params.loadSize

        return try {
            val response = networkDataSource.searchMusic(
                query = query,
                limit = limit,
                offset = offset
            )

            response.fold(
                onSuccess = { searchResponse ->
                    val tracks = searchResponse.results.toTracks()

                    LoadResult.Page(
                        data = tracks,
                        prevKey = if (offset == 0) null else offset - limit,
                        nextKey = if (tracks.isEmpty() || tracks.size < limit) null else offset + limit
                    )
                },
                onFailure = { throwable ->
                    LoadResult.Error(throwable)
                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Track>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(state.config.pageSize)
                ?: anchorPage?.nextKey?.minus(state.config.pageSize)
        }
    }
}
