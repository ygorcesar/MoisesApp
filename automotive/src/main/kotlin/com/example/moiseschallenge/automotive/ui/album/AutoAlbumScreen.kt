package com.example.moiseschallenge.automotive.ui.album

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moiseschallenge.automotive.ui.theme.AutomotiveTheme
import com.example.moiseschallenge.resources.R
import com.example.moiseschallenge.shared.album.AlbumScreenContract
import com.example.moiseschallenge.shared.album.AlbumViewModel
import com.example.moiseschallenge.ui.components.AutoAlbumBackgroundBlurCompat
import com.example.moiseschallenge.ui.components.TrackListItem
import com.example.moiseschallenge.ui.components.TrackListItemStyle
import com.example.moiseschallenge.ui.model.AlbumUiState
import com.example.moiseschallenge.ui.model.PlayerStateUiModel
import com.example.moiseschallenge.ui.model.toUiModel
import com.example.moiseschallenge.ui.util.PreviewAuto
import com.example.moiseschallenge.ui.util.PreviewDataMocks
import kotlinx.collections.immutable.toImmutableList

@Composable
fun AutoAlbumScreen(
    onNavigateBack: () -> Unit,
    onTrackClick: () -> Unit,
    viewModel: AlbumViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val actionHandler: AlbumScreenContract.ActionHandler = remember { viewModel }

    AutoAlbumScreenContent(
        uiState = uiState,
        playerState = playerState,
        actionHandler = actionHandler,
        onNavigateBack = onNavigateBack,
        onTrackClick = onTrackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AutoAlbumScreenContent(
    uiState: AlbumUiState,
    playerState: PlayerStateUiModel,
    actionHandler: AlbumScreenContract.ActionHandler,
    onNavigateBack: () -> Unit,
    onTrackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.album?.collectionName
                            ?: stringResource(id = R.string.title_album),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.nav_back),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        AutoAlbumBackgroundBlurCompat(uiState.album?.artworkUrlHighRes)
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
                    text = uiState.error.orEmpty(),
                    color = MaterialTheme.colorScheme.error
                )
            }

            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(uiState.tracks, key = { it.id }) { track ->
                    TrackListItem(
                        track = track,
                        onClick = {
                            actionHandler.onTrackClick(track)
                            onTrackClick()
                        },
                        isCurrentTrack = track.id == playerState.currentTrack?.id,
                        isPlaying = playerState.isPlaying,
                        style = TrackListItemStyle.Auto
                    )
                }
            }
        }
    }
}

@PreviewAuto
@Composable
private fun AutoAlbumScreenPreview() {
    AutomotiveTheme {
        AutoAlbumScreenContent(
            uiState = AlbumUiState(
                album = PreviewDataMocks.previewAlbum,
                tracks = PreviewDataMocks.previewTracks.toImmutableList(),
                isLoading = false
            ),
            playerState = PlayerStateUiModel(),
            actionHandler = AlbumScreenContract.NoOp(),
            onNavigateBack = {},
            onTrackClick = {}
        )
    }
}

@PreviewAuto
@Composable
private fun AutoAlbumScreenPlayingPreview() {
    AutomotiveTheme {
        AutoAlbumScreenContent(
            uiState = AlbumUiState(
                album = PreviewDataMocks.previewAlbum,
                tracks = PreviewDataMocks.previewTracks.toImmutableList(),
                isLoading = false
            ),
            playerState = PlayerStateUiModel(
                currentTrack = PreviewDataMocks.previewTracks[1].toUiModel(),
                isPlaying = true
            ),
            actionHandler = AlbumScreenContract.NoOp(),
            onNavigateBack = {},
            onTrackClick = {}
        )
    }
}

@PreviewAuto
@Composable
private fun AutoAlbumScreenLoadingPreview() {
    AutomotiveTheme {
        AutoAlbumScreenContent(
            uiState = AlbumUiState(isLoading = true),
            playerState = PlayerStateUiModel(),
            actionHandler = AlbumScreenContract.NoOp(),
            onNavigateBack = {},
            onTrackClick = {}
        )
    }
}
