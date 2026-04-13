package com.example.moiseschallenge.shared.album

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moiseschallenge.domain.model.Track
import com.example.moiseschallenge.domain.repository.MusicRepository
import com.example.moiseschallenge.domain.usecase.GetPlayerStateUseCase
import com.example.moiseschallenge.domain.usecase.PlayTrackUseCase
import com.example.moiseschallenge.ui.model.AlbumUiState
import com.example.moiseschallenge.ui.model.PlayerStateUiModel
import com.example.moiseschallenge.ui.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val musicRepository: MusicRepository,
    private val playTrackUseCase: PlayTrackUseCase,
    getPlayerStateUseCase: GetPlayerStateUseCase
) : ViewModel(), AlbumScreenContract.ActionHandler {

    private val albumId: Long = savedStateHandle.get<Long>("albumId") ?: -1L

    private val _uiState = MutableStateFlow(AlbumUiState())
    val uiState: StateFlow<AlbumUiState> = _uiState.asStateFlow()

    val playerState: StateFlow<PlayerStateUiModel> = getPlayerStateUseCase()
        .map { it.toUiModel() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlayerStateUiModel()
        )

    init {
        loadAlbum()
    }

    private fun loadAlbum() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val albumResult = musicRepository.getAlbumDetails(albumId)
            val tracksResult = musicRepository.getAlbumTracks(albumId)

            if (albumResult.isSuccess && tracksResult.isSuccess) {
                _uiState.value = AlbumUiState(
                    album = albumResult.getOrNull(),
                    tracks = tracksResult.getOrDefault(emptyList()).toImmutableList(),
                    isLoading = false
                )
            } else {
                _uiState.value = AlbumUiState(
                    isLoading = false,
                    error = albumResult.exceptionOrNull()?.message
                        ?: tracksResult.exceptionOrNull()?.message
                        ?: "Failed to load album"
                )
            }
        }
    }

    override fun onTrackClick(track: Track) {
        val tracks = _uiState.value.tracks
        val index = tracks.indexOf(track)
        if (index >= 0) {
            playTrackUseCase.playQueue(tracks, index)
        } else {
            playTrackUseCase(track)
        }
    }

    override fun retry() {
        loadAlbum()
    }
}
