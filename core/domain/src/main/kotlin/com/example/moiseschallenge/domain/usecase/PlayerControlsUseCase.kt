package com.example.moiseschallenge.domain.usecase

import com.example.moiseschallenge.domain.model.RepeatMode
import com.example.moiseschallenge.domain.repository.PlayerRepository
import javax.inject.Inject

class PlayerControlsUseCase @Inject constructor(
    private val playerRepository: PlayerRepository
) {
    fun pause() = playerRepository.pause()

    fun resume() = playerRepository.resume()

    fun stop() = playerRepository.stop()

    fun seekTo(position: Long) = playerRepository.seekTo(position)

    fun skipToNext() = playerRepository.skipToNext()

    fun skipToPrevious() = playerRepository.skipToPrevious()

    fun seekForward(seconds: Int = 15) = playerRepository.seekForward(seconds)

    fun seekBackward(seconds: Int = 15) = playerRepository.seekBackward(seconds)

    fun setRepeatMode(mode: RepeatMode) = playerRepository.setRepeatMode(mode)
}
