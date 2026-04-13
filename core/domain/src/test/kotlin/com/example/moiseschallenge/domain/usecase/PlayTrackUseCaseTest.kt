package com.example.moiseschallenge.domain.usecase

import com.example.moiseschallenge.domain.model.Track
import com.example.moiseschallenge.domain.repository.PlayerRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class PlayTrackUseCaseTest {

    private lateinit var playerRepository: PlayerRepository
    private lateinit var useCase: PlayTrackUseCase

    @Before
    fun setup() {
        playerRepository = mockk(relaxed = true)
        useCase = PlayTrackUseCase(playerRepository)
    }

    @Test
    fun `invoke calls playerRepository play with track`() {
        val track = createTestTrack(1)

        useCase(track)

        verify { playerRepository.play(track) }
    }

    @Test
    fun `playQueue calls playerRepository playQueue with tracks and index`() {
        val tracks = listOf(
            createTestTrack(1),
            createTestTrack(2),
            createTestTrack(3)
        )

        useCase.playQueue(tracks, 1)

        verify { playerRepository.playQueue(tracks, 1) }
    }

    @Test
    fun `playQueue uses default startIndex of 0`() {
        val tracks = listOf(
            createTestTrack(1),
            createTestTrack(2)
        )

        useCase.playQueue(tracks)

        verify { playerRepository.playQueue(tracks, 0) }
    }

    private fun createTestTrack(id: Long) = Track(
        id = id,
        trackName = "Track $id",
        artistName = "Artist",
        collectionName = "Album",
        artworkUrl100 = null,
        previewUrl = "https://example.com/preview.mp3",
        trackTimeMillis = 180000,
        collectionId = 100,
        artistId = 200,
        trackNumber = id.toInt(),
        discNumber = 1,
        releaseDate = null,
        primaryGenreName = "Pop"
    )
}
