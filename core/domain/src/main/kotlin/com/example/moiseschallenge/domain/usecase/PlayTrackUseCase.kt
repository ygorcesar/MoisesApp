package com.example.moiseschallenge.domain.usecase

import com.example.moiseschallenge.domain.model.Track
import com.example.moiseschallenge.domain.repository.PlayerRepository
import javax.inject.Inject

class PlayTrackUseCase @Inject constructor(
    private val playerRepository: PlayerRepository
) {
    operator fun invoke(track: Track) {
        playerRepository.play(track)
    }

    fun playQueue(tracks: List<Track>, startIndex: Int = 0) {
        playerRepository.playQueue(tracks, startIndex)
    }
}
