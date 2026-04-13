package com.example.moiseschallenge.domain.model

data class PlayerState(
    val currentTrack: Track? = null,
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val playbackSpeed: Float = 1f,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val queue: List<Track> = emptyList(),
    val currentQueueIndex: Int = -1
) {
    val progress: Float
        get() = if (duration > 0) currentPosition.toFloat() / duration else 0f

    val hasNext: Boolean
        get() = currentQueueIndex < queue.lastIndex

    val hasPrevious: Boolean
        get() = currentQueueIndex > 0
}

enum class RepeatMode {
    OFF,
    ONE,
    ALL
}
