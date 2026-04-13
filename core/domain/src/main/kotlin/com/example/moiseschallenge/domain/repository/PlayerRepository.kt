package com.example.moiseschallenge.domain.repository

import com.example.moiseschallenge.domain.model.PlayerState
import com.example.moiseschallenge.domain.model.RepeatMode
import com.example.moiseschallenge.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    val playerState: Flow<PlayerState>

    fun play(track: Track)

    fun playQueue(tracks: List<Track>, startIndex: Int = 0)

    fun pause()

    fun resume()

    fun stop()

    fun seekTo(position: Long)

    fun skipToNext()

    fun skipToPrevious()

    fun seekForward(seconds: Int = 15)

    fun seekBackward(seconds: Int = 15)

    fun setRepeatMode(mode: RepeatMode)

    fun addToQueue(track: Track)

    fun removeFromQueue(index: Int)

    fun clearQueue()

    fun release()
}
