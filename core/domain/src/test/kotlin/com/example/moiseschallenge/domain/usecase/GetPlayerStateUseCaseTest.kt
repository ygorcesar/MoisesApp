package com.example.moiseschallenge.domain.usecase

import app.cash.turbine.test
import com.example.moiseschallenge.domain.model.PlayerState
import com.example.moiseschallenge.domain.model.RepeatMode
import com.example.moiseschallenge.domain.model.Track
import com.example.moiseschallenge.domain.repository.PlayerRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetPlayerStateUseCaseTest {

    private lateinit var playerRepository: PlayerRepository
    private lateinit var useCase: GetPlayerStateUseCase
    private val playerStateFlow = MutableStateFlow(PlayerState())

    @Before
    fun setup() {
        playerRepository = mockk()
        every { playerRepository.playerState } returns playerStateFlow
        useCase = GetPlayerStateUseCase(playerRepository)
    }

    @Test
    fun `invoke returns playerState flow from repository`() = runTest {
        useCase().test {
            val initialState = awaitItem()
            assertThat(initialState.isPlaying).isFalse()
            assertThat(initialState.currentTrack).isNull()
        }
    }

    @Test
    fun `emits updated state when repository state changes`() = runTest {
        val track = createTestTrack(1)

        useCase().test {
            awaitItem() // initial state

            playerStateFlow.value = PlayerState(
                currentTrack = track,
                isPlaying = true,
                currentPosition = 5000,
                duration = 180000
            )

            val updatedState = awaitItem()
            assertThat(updatedState.currentTrack).isEqualTo(track)
            assertThat(updatedState.isPlaying).isTrue()
            assertThat(updatedState.currentPosition).isEqualTo(5000)
            assertThat(updatedState.duration).isEqualTo(180000)
        }
    }

    @Test
    fun `emits state with repeat mode`() = runTest {
        useCase().test {
            awaitItem() // initial state

            playerStateFlow.value = PlayerState(
                repeatMode = RepeatMode.ALL
            )

            val updatedState = awaitItem()
            assertThat(updatedState.repeatMode).isEqualTo(RepeatMode.ALL)
        }
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
