package com.example.moiseschallenge.data.repository

import com.example.moiseschallenge.data.local.dao.TrackDao
import com.example.moiseschallenge.network.api.MusicNetworkDataSource
import com.example.moiseschallenge.network.model.ITunesResult
import com.example.moiseschallenge.network.model.ITunesSearchResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MusicRepositoryImplTest {

    private lateinit var networkDataSource: MusicNetworkDataSource
    private lateinit var trackDao: TrackDao
    private lateinit var repository: MusicRepositoryImpl

    @Before
    fun setup() {
        networkDataSource = mockk()
        trackDao = mockk(relaxed = true)
        repository = MusicRepositoryImpl(networkDataSource, trackDao)
    }

    @Test
    fun `searchTracksOneShot returns tracks from network`() = runTest {
        val query = "pop"
        val response = ITunesSearchResponse(
            resultCount = 2,
            results = listOf(
                createTestResult(1, "Track 1"),
                createTestResult(2, "Track 2")
            )
        )

        coEvery { networkDataSource.searchMusic(query, any()) } returns Result.success(response)

        val result = repository.searchTracksOneShot(query)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.size).isEqualTo(2)
    }

    @Test
    fun `searchTracksOneShot caches results`() = runTest {
        val query = "jazz"
        val response = ITunesSearchResponse(
            resultCount = 1,
            results = listOf(createTestResult(1, "Jazz Track"))
        )

        coEvery { networkDataSource.searchMusic(query, any()) } returns Result.success(response)

        repository.searchTracksOneShot(query)

        coVerify { trackDao.deleteTracksByQuery(query.lowercase()) }
        coVerify { trackDao.insertTracks(any()) }
    }

    @Test
    fun `searchTracksOneShot returns failure on network error`() = runTest {
        val query = "error"
        val exception = RuntimeException("Network error")

        coEvery { networkDataSource.searchMusic(query, any()) } returns Result.failure(exception)

        val result = repository.searchTracksOneShot(query)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Network error")
    }

    @Test
    fun `getAlbumTracks returns sorted tracks`() = runTest {
        val albumId = 100L
        val response = ITunesSearchResponse(
            resultCount = 3,
            results = listOf(
                createTestResult(3, "Track 3", trackNumber = 3, discNumber = 1),
                createTestResult(1, "Track 1", trackNumber = 1, discNumber = 1),
                createTestResult(2, "Track 2", trackNumber = 2, discNumber = 1)
            )
        )

        coEvery { networkDataSource.getAlbumTracks(albumId) } returns Result.success(response)

        val result = repository.getAlbumTracks(albumId)

        assertThat(result.isSuccess).isTrue()
        val tracks = result.getOrNull()!!
        assertThat(tracks.map { it.trackName }).isEqualTo(listOf("Track 1", "Track 2", "Track 3"))
    }

    private fun createTestResult(
        id: Long,
        name: String,
        trackNumber: Int = 1,
        discNumber: Int = 1
    ) = ITunesResult(
        wrapperType = "track",
        kind = "song",
        trackId = id,
        trackName = name,
        artistName = "Iron Maiden",
        collectionName = "Powerslave",
        artworkUrl100 = "https://example.com/artwork.jpg",
        previewUrl = "https://example.com/rime_ancient_mariner.mp3",
        trackTimeMillis = 180000,
        collectionId = 100L,
        artistId = 200L,
        trackNumber = trackNumber,
        discNumber = discNumber
    )
}
