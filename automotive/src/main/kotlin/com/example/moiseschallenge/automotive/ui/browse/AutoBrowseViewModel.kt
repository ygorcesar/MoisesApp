package com.example.moiseschallenge.automotive.ui.browse

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moiseschallenge.domain.model.Track
import com.example.moiseschallenge.ui.model.PlayerStateUiModel
import com.example.moiseschallenge.ui.model.toUiModel
import com.example.moiseschallenge.domain.repository.MusicRepository
import com.example.moiseschallenge.player.MoisesPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AutoBrowseViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    private val player: MoisesPlayer
) : ViewModel(), AutoBrowseScreenContract.ActionHandler {

    private val _uiState = MutableStateFlow(AutoBrowseUiState())
    val uiState: StateFlow<AutoBrowseUiState> = _uiState.asStateFlow()

    val playerState: StateFlow<PlayerStateUiModel> = player.playerState
        .map { it.toUiModel() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlayerStateUiModel()
        )

    init {
        loadContent()
    }

    private fun loadContent() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            musicRepository.searchTracksOneShot("pop", limit = 50)
                .onSuccess { tracks ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            tracks = tracks.toImmutableList()
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load content"
                        )
                    }
                }
        }
    }

    override fun onTrackClick(track: Track) {
        val allTracks = _uiState.value.tracks
        player.playQueue(allTracks, allTracks.indexOf(track))
    }

    override fun onSkipNext() {
        player.skipToNext()
    }

    override fun onSkipPrevious() {
        player.skipToPrevious()
    }

    override fun onPause() {
        player.pause()
    }

    override fun onResume() {
        player.resume()
    }
}

@Immutable
data class AutoBrowseUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val tracks: ImmutableList<Track> = persistentListOf()
)
