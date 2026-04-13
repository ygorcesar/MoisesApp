package com.example.moiseschallenge.shared.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moiseschallenge.domain.model.RepeatMode
import com.example.moiseschallenge.domain.usecase.GetPlayerStateUseCase
import com.example.moiseschallenge.domain.usecase.PlayerControlsUseCase
import com.example.moiseschallenge.ui.model.PlayerStateUiModel
import com.example.moiseschallenge.ui.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    getPlayerStateUseCase: GetPlayerStateUseCase,
    private val playerControlsUseCase: PlayerControlsUseCase
) : ViewModel(), PlayerScreenContract.ActionHandler {

    val playerState: StateFlow<PlayerStateUiModel> = getPlayerStateUseCase()
        .map { it.toUiModel() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlayerStateUiModel()
        )

    override fun onPlayPauseClick() {
        if (playerState.value.isPlaying) {
            playerControlsUseCase.pause()
        } else {
            playerControlsUseCase.resume()
        }
    }

    override fun onSkipNext() {
        playerControlsUseCase.skipToNext()
    }

    override fun onSkipPrevious() {
        playerControlsUseCase.skipToPrevious()
    }

    override fun onSeekForward() {
        playerControlsUseCase.seekForward(15)
    }

    override fun onSeekBackward() {
        playerControlsUseCase.seekBackward(15)
    }

    override fun onSeekTo(progress: Float) {
        val duration = playerState.value.duration
        val newPosition = (progress * duration).toLong()
        playerControlsUseCase.seekTo(newPosition)
    }

    override fun onToggleRepeat() {
        val nextMode = when (playerState.value.repeatMode) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
        playerControlsUseCase.setRepeatMode(nextMode)
    }
}
