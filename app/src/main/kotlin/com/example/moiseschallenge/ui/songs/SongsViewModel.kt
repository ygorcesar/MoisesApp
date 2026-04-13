package com.example.moiseschallenge.ui.songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moiseschallenge.domain.model.Track
import com.example.moiseschallenge.domain.usecase.GetPlayerStateUseCase
import com.example.moiseschallenge.domain.usecase.PlayerControlsUseCase
import com.example.moiseschallenge.domain.usecase.PlayTrackUseCase
import com.example.moiseschallenge.domain.usecase.SearchTracksUseCase
import com.example.moiseschallenge.ui.model.PlayerStateUiModel
import com.example.moiseschallenge.ui.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val SEARCH_DEBOUNCE_TIMEOUT = 1200L

@HiltViewModel
class SongsViewModel @Inject constructor(
    private val searchTracksUseCase: SearchTracksUseCase,
    private val playTrackUseCase: PlayTrackUseCase,
    private val playerControlsUseCase: PlayerControlsUseCase,
    getPlayerStateUseCase: GetPlayerStateUseCase
) : ViewModel(), SongsScreenContract.ActionHandler {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val tracks: Flow<PagingData<Track>> = _searchQuery
        .debounce(SEARCH_DEBOUNCE_TIMEOUT)
        .flatMapLatest { query ->
            val searchTerm = query.ifBlank { DEFAULT_SEARCH_TERM }
            searchTracksUseCase(searchTerm)
        }
        .cachedIn(viewModelScope)

    val playerState: StateFlow<PlayerStateUiModel> = getPlayerStateUseCase()
        .map { it.toUiModel() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlayerStateUiModel()
        )

    override fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    override fun onTrackClick(track: Track, allTracks: List<Track>) {
        val index = allTracks.indexOf(track)
        if (index >= 0) {
            playTrackUseCase.playQueue(allTracks, index)
        } else {
            playTrackUseCase(track)
        }
    }

    override fun onPause() {
        playerControlsUseCase.pause()
    }

    override fun onResume() {
        playerControlsUseCase.resume()
    }

    override fun onSkipPrevious() {
        playerControlsUseCase.skipToPrevious()
    }

    override fun onSkipNext() {
        playerControlsUseCase.skipToNext()
    }

    companion object {
        private const val DEFAULT_SEARCH_TERM = "pop"
    }
}
