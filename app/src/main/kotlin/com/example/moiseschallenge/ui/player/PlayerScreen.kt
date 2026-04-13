package com.example.moiseschallenge.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moiseschallenge.resources.R
import com.example.moiseschallenge.shared.player.PlayerScreenContract
import com.example.moiseschallenge.shared.player.PlayerViewModel
import com.example.moiseschallenge.ui.components.AlbumArtwork
import com.example.moiseschallenge.ui.components.MobileAlbumBackgroundBlurCompat
import com.example.moiseschallenge.ui.components.PlayPauseButton
import com.example.moiseschallenge.ui.components.ProgressSlider
import com.example.moiseschallenge.ui.components.RepeatButton
import com.example.moiseschallenge.ui.components.SkipButton
import com.example.moiseschallenge.ui.components.TrackActionSheet
import com.example.moiseschallenge.ui.model.PlayerStateUiModel
import com.example.moiseschallenge.ui.model.TrackUiModel
import com.example.moiseschallenge.ui.theme.MoisesTheme
import com.example.moiseschallenge.ui.util.PreviewDataMocks

@Composable
fun PlayerScreen(
    onNavigateBack: () -> Unit,
    onAlbumClick: (Long) -> Unit,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val actionHandler: PlayerScreenContract.ActionHandler = remember { viewModel }

    PlayerScreenContent(
        playerState = playerState,
        actionHandler = actionHandler,
        onNavigateBack = onNavigateBack,
        onAlbumClick = onAlbumClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PlayerScreenContent(
    playerState: PlayerStateUiModel,
    actionHandler: PlayerScreenContract.ActionHandler,
    onNavigateBack: () -> Unit,
    onAlbumClick: (Long) -> Unit
) {
    val currentTrack: TrackUiModel? = playerState.currentTrack
    var showActionSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.title_now_playing),
                        style = MaterialTheme.typography.titleMedium
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
                actions = {
                    IconButton(onClick = { showActionSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(id = R.string.nav_more_options)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.background(
                    brush = Brush.verticalGradient(
                        0.0f to MaterialTheme.colorScheme.background,
                        0.95f to Color.Transparent,
                    )
                )
            )
        },
        containerColor = Color.Transparent,
    ) { paddingValues ->
        MobileAlbumBackgroundBlurCompat(currentTrack?.artworkUrlHighRes)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.weight(0.4f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 72.dp),
                contentAlignment = Alignment.Center
            ) {
                AlbumArtwork(
                    artworkUrl = currentTrack?.artworkUrlHighRes,
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 24.dp,
                    placeholderSize = 64.dp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            BottomContent(
                playerState = playerState,
                actionHandler = actionHandler,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.75f))
                    .padding(top = 24.dp)
                    .padding(horizontal = 24.dp)
            )
        }
    }

    if (showActionSheet && currentTrack != null) {
        TrackActionSheet(
            track = currentTrack.toDomain(),
            onDismiss = { showActionSheet = false },
            onViewAlbum = {
                currentTrack.collectionId?.let { onAlbumClick(it) }
            }
        )
    }
}

@Composable
private fun BottomContent(
    playerState: PlayerStateUiModel,
    actionHandler: PlayerScreenContract.ActionHandler,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        TrackInformation(playerState, actionHandler)

        Spacer(modifier = Modifier.height(24.dp))

        ControlsRow(playerState, actionHandler)
    }
}

@Composable
private fun TrackInformation(
    playerState: PlayerStateUiModel,
    actionHandler: PlayerScreenContract.ActionHandler,
) {
    val currentTrack: TrackUiModel? = playerState.currentTrack
    Text(
        text = currentTrack?.trackName ?: stringResource(id = R.string.player_no_track),
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.onBackground,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.fillMaxWidth(),
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = currentTrack?.artistName
            ?: stringResource(id = R.string.placeholder_unknown_artist),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.fillMaxWidth(),
    )

    Spacer(modifier = Modifier.height(12.dp))

    ProgressSlider(
        progress = playerState.progress,
        currentPosition = playerState.currentPosition,
        duration = playerState.duration,
        onSeek = actionHandler::onSeekTo,
        showRemainingTime = true
    )
}

@Composable
private fun ControlsRow(
    playerState: PlayerStateUiModel,
    actionHandler: PlayerScreenContract.ActionHandler,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PlayPauseButton(
                isPlaying = playerState.isPlaying,
                onClick = actionHandler::onPlayPauseClick
            )

            SkipButton(
                isNext = false,
                onClick = actionHandler::onSkipPrevious
            )

            SkipButton(
                isNext = true,
                onClick = actionHandler::onSkipNext
            )
        }

        RepeatButton(
            repeatMode = playerState.repeatMode,
            onClick = actionHandler::onToggleRepeat
        )
    }
}

@Preview
@Composable
private fun PlayerScreenPausedPreview() {
    MoisesTheme {
        PlayerScreenContent(
            playerState = PlayerStateUiModel(
                currentTrack = PreviewDataMocks.previewTrackUiModel,
                isPlaying = false,
                currentPosition = 1500,
                duration = 3000,
            ),
            actionHandler = PlayerScreenContract.NoOp(),
            onNavigateBack = {},
            onAlbumClick = {}
        )
    }
}
