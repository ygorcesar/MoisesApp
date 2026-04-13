package com.example.moiseschallenge.ui.album

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.moiseschallenge.domain.model.Album
import com.example.moiseschallenge.domain.model.Track
import com.example.moiseschallenge.resources.R
import com.example.moiseschallenge.shared.album.AlbumScreenContract
import com.example.moiseschallenge.shared.album.AlbumViewModel
import com.example.moiseschallenge.ui.components.ErrorContent
import com.example.moiseschallenge.ui.components.LoadingContent
import com.example.moiseschallenge.ui.components.PlayingIndicator
import com.example.moiseschallenge.ui.model.AlbumUiState
import com.example.moiseschallenge.ui.model.PlayerStateUiModel
import com.example.moiseschallenge.ui.model.toUiModel
import com.example.moiseschallenge.ui.theme.MoisesTheme
import com.example.moiseschallenge.ui.util.PreviewDataMocks
import kotlinx.collections.immutable.toImmutableList

@Composable
fun AlbumScreen(
    onNavigateBack: () -> Unit,
    onTrackClick: (Long) -> Unit,
    viewModel: AlbumViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val actionHandler: AlbumScreenContract.ActionHandler = remember { viewModel }

    AlbumScreenContent(
        uiState = uiState,
        playerState = playerState,
        actionHandler = actionHandler,
        onNavigateBack = onNavigateBack,
        onTrackClick = onTrackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AlbumScreenContent(
    uiState: AlbumUiState,
    playerState: PlayerStateUiModel,
    actionHandler: AlbumScreenContract.ActionHandler,
    onNavigateBack: () -> Unit,
    onTrackClick: (Long) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.album?.collectionName ?: "Album",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.nav_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        when {
            uiState.isLoading -> LoadingContent(
                modifier = Modifier.padding(paddingValues),
                message = stringResource(id = R.string.loading_album)
            )

            uiState.error != null -> ErrorContent(
                message = uiState.error.orEmpty(),
                onRetry = actionHandler::retry,
                modifier = Modifier.padding(paddingValues)
            )

            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    AlbumHeader(album = uiState.album)
                }

                items(
                    items = uiState.tracks,
                    key = { it.id }
                ) { track ->
                    AlbumTrackItem(
                        track = track,
                        onClick = {
                            actionHandler.onTrackClick(track)
                            onTrackClick(track.id)
                        },
                        isCurrentTrack = track.id == playerState.currentTrack?.id,
                        isPlaying = playerState.isPlaying
                    )
                }
            }
        }
    }
}

@Composable
private fun AlbumHeader(
    album: Album?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (album?.artworkUrlHighRes != null) {
                AsyncImage(
                    model = album.artworkUrlHighRes,
                    contentDescription = stringResource(id = R.string.cd_album_artwork),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_playing_animation),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = album?.collectionName ?: stringResource(id = R.string.placeholder_unknown_album),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = album?.artistName ?: stringResource(id = R.string.placeholder_unknown_artist),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        album?.primaryGenreName?.let { genre ->
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = genre,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun AlbumTrackItem(
    track: Track,
    onClick: () -> Unit,
    isCurrentTrack: Boolean,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.trackName,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = if (isCurrentTrack) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = track.artistName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (isCurrentTrack && isPlaying) {
            PlayingIndicator(
                modifier = Modifier.size(24.dp),
                isPlaying = true,
                barColor = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview
@Composable
private fun AlbumScreenPreview() {
    MoisesTheme {
        AlbumScreenContent(
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

@Preview
@Composable
private fun AlbumScreenPlayingPreview() {
    MoisesTheme {
        AlbumScreenContent(
            uiState = AlbumUiState(
                album = PreviewDataMocks.previewAlbum,
                tracks = PreviewDataMocks.previewTracks.toImmutableList(),
                isLoading = false
            ),
            playerState = PlayerStateUiModel(
                currentTrack = PreviewDataMocks.previewTrack.toUiModel(),
                isPlaying = true
            ),
            actionHandler = AlbumScreenContract.NoOp(),
            onNavigateBack = {},
            onTrackClick = {}
        )
    }
}

@Preview
@Composable
private fun AlbumScreenLoadingPreview() {
    MoisesTheme {
        AlbumScreenContent(
            uiState = AlbumUiState(isLoading = true),
            playerState = PlayerStateUiModel(),
            actionHandler = AlbumScreenContract.NoOp(),
            onNavigateBack = {},
            onTrackClick = {}
        )
    }
}

@Preview
@Composable
private fun AlbumScreenErrorPreview() {
    MoisesTheme {
        AlbumScreenContent(
            uiState = AlbumUiState(
                isLoading = false,
                error = "Failed to load album. Please try again."
            ),
            playerState = PlayerStateUiModel(),
            actionHandler = AlbumScreenContract.NoOp(),
            onNavigateBack = {},
            onTrackClick = {}
        )
    }
}

@Preview
@Composable
private fun AlbumHeaderPreview() {
    MoisesTheme {
        AlbumHeader(album = PreviewDataMocks.previewAlbum)
    }
}

@Preview
@Composable
private fun AlbumTrackItemPreview() {
    MoisesTheme {
        AlbumTrackItem(
            track = PreviewDataMocks.previewTrack,
            onClick = {},
            isCurrentTrack = false,
            isPlaying = false
        )
    }
}

@Preview
@Composable
private fun AlbumTrackItemPlayingPreview() {
    MoisesTheme {
        AlbumTrackItem(
            track = PreviewDataMocks.previewTrack,
            onClick = {},
            isCurrentTrack = true,
            isPlaying = true
        )
    }
}
