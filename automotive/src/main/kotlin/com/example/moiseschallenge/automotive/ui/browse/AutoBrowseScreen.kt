package com.example.moiseschallenge.automotive.ui.browse

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moiseschallenge.automotive.ui.theme.AutomotiveTheme
import com.example.moiseschallenge.domain.model.Track
import com.example.moiseschallenge.resources.R
import com.example.moiseschallenge.ui.components.AutoMiniPlayerBar
import com.example.moiseschallenge.ui.components.TrackListItem
import com.example.moiseschallenge.ui.components.TrackListItemStyle
import com.example.moiseschallenge.ui.model.PlayerStateUiModel
import com.example.moiseschallenge.ui.model.toUiModel
import com.example.moiseschallenge.ui.util.PreviewAuto
import com.example.moiseschallenge.ui.util.PreviewDataMocks
import kotlinx.collections.immutable.toImmutableList

@Composable
fun AutoBrowseScreen(
    onTrackClick: () -> Unit,
    onPlayerClick: () -> Unit,
    viewModel: AutoBrowseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val actionHandler: AutoBrowseScreenContract.ActionHandler = remember { viewModel }

    AutoBrowseScreenContent(
        uiState = uiState,
        playerState = playerState,
        actionHandler = actionHandler,
        onTrackClick = onTrackClick,
        onPlayerClick = onPlayerClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AutoBrowseScreenContent(
    uiState: AutoBrowseUiState,
    playerState: PlayerStateUiModel,
    actionHandler: AutoBrowseScreenContract.ActionHandler,
    onTrackClick: () -> Unit,
    onPlayerClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.title_songs),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = {
            playerState.currentTrack?.let { track ->
                AutoMiniPlayerBar(
                    track = track.toDomain(),
                    isPlaying = playerState.isPlaying,
                    progress = playerState.progress,
                    onBarClick = onPlayerClick,
                    onPlayPauseClick = {
                        if (playerState.isPlaying) {
                            actionHandler.onPause()
                        } else {
                            actionHandler.onResume()
                        }
                    },
                    onSkipPrevious = actionHandler::onSkipPrevious,
                    onSkipNext = actionHandler::onSkipNext
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        when {
            uiState.isLoading -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }

            uiState.error != null -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.error,
                        color = MaterialTheme.colorScheme.error
                    )
                }

            else -> TrackList(
                    tracks = uiState.tracks,
                    currentTrackId = playerState.currentTrack?.id,
                    isPlaying = playerState.isPlaying,
                    onTrackClick = { track ->
                        actionHandler.onTrackClick(track)
                        onTrackClick()
                    },
                    modifier = Modifier.padding(paddingValues)
                )
        }
    }
}

@Composable
private fun TrackList(
    tracks: List<Track>,
    currentTrackId: Long?,
    isPlaying: Boolean,
    onTrackClick: (Track) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(tracks, key = { it.id }) { track ->
            TrackListItem(
                track = track,
                onClick = { onTrackClick(track) },
                isCurrentTrack = track.id == currentTrackId,
                isPlaying = isPlaying,
                style = TrackListItemStyle.Auto
            )
        }
    }
}

@PreviewAuto
@Composable
private fun AutoBrowseScreenPreview() {
    AutomotiveTheme {
        AutoBrowseScreenContent(
            uiState = AutoBrowseUiState(
                isLoading = false,
                tracks = PreviewDataMocks.previewTracks.toImmutableList()
            ),
            playerState = PlayerStateUiModel(),
            actionHandler = AutoBrowseScreenContract.NoOp(),
            onTrackClick = {},
            onPlayerClick = {}
        )
    }
}

@PreviewAuto
@Composable
private fun AutoBrowseScreenWithPlayerPreview() {
    AutomotiveTheme {
        AutoBrowseScreenContent(
            uiState = AutoBrowseUiState(
                isLoading = false,
                tracks = PreviewDataMocks.previewTracks.toImmutableList()
            ),
            playerState = PlayerStateUiModel(
                currentTrack = PreviewDataMocks.previewTrack.toUiModel(),
                isPlaying = true,
                currentPosition = 141600,
                duration = 354000
            ),
            actionHandler = AutoBrowseScreenContract.NoOp(),
            onTrackClick = {},
            onPlayerClick = {}
        )
    }
}

@PreviewAuto
@Composable
private fun AutoBrowseScreenLoadingPreview() {
    AutomotiveTheme {
        AutoBrowseScreenContent(
            uiState = AutoBrowseUiState(isLoading = true),
            playerState = PlayerStateUiModel(),
            actionHandler = AutoBrowseScreenContract.NoOp(),
            onTrackClick = {},
            onPlayerClick = {}
        )
    }
}

@PreviewAuto
@Composable
private fun AutoBrowseScreenErrorPreview() {
    AutomotiveTheme {
        AutoBrowseScreenContent(
            uiState = AutoBrowseUiState(
                isLoading = false,
                error = "Failed to load content"
            ),
            playerState = PlayerStateUiModel(),
            actionHandler = AutoBrowseScreenContract.NoOp(),
            onTrackClick = {},
            onPlayerClick = {}
        )
    }
}
