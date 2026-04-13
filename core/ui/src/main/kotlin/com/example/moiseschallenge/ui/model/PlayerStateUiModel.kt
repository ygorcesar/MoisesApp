package com.example.moiseschallenge.ui.model

import androidx.compose.runtime.Immutable
import com.example.moiseschallenge.domain.model.PlayerState
import com.example.moiseschallenge.domain.model.RepeatMode
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class PlayerStateUiModel(
    val currentTrack: TrackUiModel? = null,
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val playbackSpeed: Float = 1f,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val queue: ImmutableList<TrackUiModel> = persistentListOf(),
    val currentQueueIndex: Int = -1
) {
    val progress: Float
        get() = if (duration > 0) currentPosition.toFloat() / duration else 0f

    val hasNext: Boolean
        get() = currentQueueIndex < queue.lastIndex

    val hasPrevious: Boolean
        get() = currentQueueIndex > 0

    companion object {
        fun from(state: PlayerState): PlayerStateUiModel = PlayerStateUiModel(
            currentTrack = state.currentTrack?.toUiModel(),
            isPlaying = state.isPlaying,
            currentPosition = state.currentPosition,
            duration = state.duration,
            playbackSpeed = state.playbackSpeed,
            repeatMode = state.repeatMode,
            queue = state.queue.map { it.toUiModel() }.toImmutableList(),
            currentQueueIndex = state.currentQueueIndex
        )
    }
}

fun PlayerState.toUiModel(): PlayerStateUiModel = PlayerStateUiModel.from(this)
