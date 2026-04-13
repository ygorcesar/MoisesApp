package com.example.moiseschallenge.domain.usecase

import com.example.moiseschallenge.domain.model.RepeatMode
import com.example.moiseschallenge.domain.repository.PlayerRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class PlayerControlsUseCaseTest {

    private lateinit var playerRepository: PlayerRepository
    private lateinit var playerControlsUseCase: PlayerControlsUseCase

    @Before
    fun setup() {
        playerRepository = mockk(relaxed = true)
        playerControlsUseCase = PlayerControlsUseCase(playerRepository)
    }

    @Test
    fun `pause calls repository pause`() {
        playerControlsUseCase.pause()
        verify(exactly = 1) { playerRepository.pause() }
    }

    @Test
    fun `resume calls repository resume`() {
        playerControlsUseCase.resume()
        verify(exactly = 1) { playerRepository.resume() }
    }

    @Test
    fun `stop calls repository stop`() {
        playerControlsUseCase.stop()
        verify(exactly = 1) { playerRepository.stop() }
    }

    @Test
    fun `seekTo calls repository seekTo with correct position`() {
        val position = 5000L
        playerControlsUseCase.seekTo(position)
        verify(exactly = 1) { playerRepository.seekTo(position) }
    }

    @Test
    fun `skipToNext calls repository skipToNext`() {
        playerControlsUseCase.skipToNext()
        verify(exactly = 1) { playerRepository.skipToNext() }
    }

    @Test
    fun `skipToPrevious calls repository skipToPrevious`() {
        playerControlsUseCase.skipToPrevious()
        verify(exactly = 1) { playerRepository.skipToPrevious() }
    }

    @Test
    fun `seekForward calls repository seekForward with correct seconds`() {
        val seconds = 15
        playerControlsUseCase.seekForward(seconds)
        verify(exactly = 1) { playerRepository.seekForward(seconds) }
    }

    @Test
    fun `seekBackward calls repository seekBackward with correct seconds`() {
        val seconds = 15
        playerControlsUseCase.seekBackward(seconds)
        verify(exactly = 1) { playerRepository.seekBackward(seconds) }
    }

    @Test
    fun `setRepeatMode calls repository setRepeatMode`() {
        playerControlsUseCase.setRepeatMode(RepeatMode.ALL)
        verify(exactly = 1) { playerRepository.setRepeatMode(RepeatMode.ALL) }
    }
}
