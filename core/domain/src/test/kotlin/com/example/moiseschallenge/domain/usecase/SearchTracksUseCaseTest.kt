package com.example.moiseschallenge.domain.usecase

import androidx.paging.PagingData
import app.cash.turbine.test
import com.example.moiseschallenge.domain.model.Track
import com.example.moiseschallenge.domain.repository.MusicRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SearchTracksUseCaseTest {

    private lateinit var musicRepository: MusicRepository
    private lateinit var searchTracksUseCase: SearchTracksUseCase

    @Before
    fun setup() {
        musicRepository = mockk()
        searchTracksUseCase = SearchTracksUseCase(musicRepository)
    }

    @Test
    fun `invoke calls repository searchTracks with correct query`() = runTest {
        val query = "test query"
        val pagingData = PagingData.from(listOf(createTestTrack()))

        every { musicRepository.searchTracks(query) } returns flowOf(pagingData)

        searchTracksUseCase(query).test {
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 1) { musicRepository.searchTracks(query) }
    }

    @Test
    fun `invoke returns flow from repository`() = runTest {
        val query = "pop"
        val tracks = listOf(
            createTestTrack(id = 1, name = "Track 1"),
            createTestTrack(id = 2, name = "Track 2")
        )
        val pagingData = PagingData.from(tracks)

        every { musicRepository.searchTracks(query) } returns flowOf(pagingData)

        searchTracksUseCase(query).test {
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createTestTrack(
        id: Long = 1L,
        name: String = "Test Track"
    ) = Track(
        id = id,
        trackName = name,
        artistName = "Test Artist",
        collectionName = "Test Album",
        artworkUrl100 = "https://example.com/artwork.jpg",
        previewUrl = "https://example.com/preview.mp3",
        trackTimeMillis = 180000,
        collectionId = 100L,
        artistId = 200L,
        trackNumber = 1,
        discNumber = 1,
        releaseDate = "2024-01-01",
        primaryGenreName = "Pop",
        isExplicit = false
    )
}
