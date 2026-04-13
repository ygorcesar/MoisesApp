package com.example.moiseschallenge.automotive.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moiseschallenge.automotive.ui.theme.AutomotiveTheme
import com.example.moiseschallenge.resources.R
import com.example.moiseschallenge.shared.player.PlayerScreenContract
import com.example.moiseschallenge.shared.player.PlayerViewModel
import com.example.moiseschallenge.ui.components.AlbumArtwork
import com.example.moiseschallenge.ui.components.AutoAlbumBackgroundBlurCompat
import com.example.moiseschallenge.ui.components.AutoProgressBar
import com.example.moiseschallenge.ui.components.RepeatButton
import com.example.moiseschallenge.ui.components.SkipButton
import com.example.moiseschallenge.ui.model.PlayerStateUiModel
import com.example.moiseschallenge.ui.model.TrackUiModel
import com.example.moiseschallenge.ui.model.toUiModel
import com.example.moiseschallenge.ui.util.PreviewAuto
import com.example.moiseschallenge.ui.util.PreviewDataMocks
import com.example.moiseschallenge.ui.util.formatTime

@Composable
fun AutoPlayerScreen(
    onNavigateBack: () -> Unit,
    onAlbumClick: (Long) -> Unit,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val actionHandler: PlayerScreenContract.ActionHandler = remember { viewModel }

    AutoPlayerScreenContent(
        playerState = playerState,
        actionHandler = actionHandler,
        onNavigateBack = onNavigateBack,
        onAlbumClick = onAlbumClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AutoPlayerScreenContent(
    playerState: PlayerStateUiModel,
    actionHandler: PlayerScreenContract.ActionHandler,
    onNavigateBack: () -> Unit,
    onAlbumClick: (Long) -> Unit = {}
) {
    val currentTrack = playerState.currentTrack

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.title_music),
                        style = MaterialTheme.typography.titleLarge
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
                actions = {
                    currentTrack?.collectionId?.let { albumId ->
                        Row(
                            modifier = Modifier
                                .background(
                                    color = Color.White.copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .clickable { onAlbumClick(albumId) }
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_album),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = stringResource(id = R.string.title_album),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        AutoAlbumBackgroundBlurCompat(currentTrack?.artworkUrlHighRes)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 124.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.Top,
            ) {
                AlbumArtwork(
                    artworkUrl = currentTrack?.artworkUrlHighRes,
                    modifier = Modifier.size(200.dp),
                    cornerRadius = 8.dp,
                    placeholderSize = 124.dp
                )

                Spacer(modifier = Modifier.width(24.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 36.dp)
                ) {
                    Text(
                        text = currentTrack?.trackName
                            ?: stringResource(id = R.string.player_no_track),
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = currentTrack?.artistName ?: "",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${formatTime(playerState.currentPosition)}/${formatTime(playerState.duration)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AutoProgressBar(
                progress = playerState.progress,
                currentPosition = playerState.currentPosition,
                duration = playerState.duration,
                onSeek = actionHandler::onSeekTo,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.White.copy(alpha = 0.4f),
            )

            Spacer(modifier = Modifier.height(16.dp))

            PlayerControls(
                playerState = playerState,
                currentTrack = currentTrack,
                actionHandler = actionHandler,
                onAlbumClick = onAlbumClick,
            )
        }
    }
}

@Composable
private fun PlayerControls(
    playerState: PlayerStateUiModel,
    currentTrack: TrackUiModel?,
    actionHandler: PlayerScreenContract.ActionHandler,
    onAlbumClick: (Long) -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RepeatButton(
            repeatMode = playerState.repeatMode,
            onClick = actionHandler::onToggleRepeat,
            modifier = Modifier.size(64.dp),
            iconSize = 42.dp
        )

        SkipButton(
            isNext = false,
            onClick = actionHandler::onSkipPrevious,
            modifier = Modifier.size(64.dp),
            iconSize = 54.dp
        )

        IconButton(
            onClick = actionHandler::onPlayPauseClick,
            modifier = Modifier
                .size(68.dp)
                .background(
                    Color.White.copy(alpha = 0.4f),
                    RoundedCornerShape(12.dp)
                )
        ) {
            Icon(
                painter = painterResource(
                    id = if (playerState.isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                ),
                contentDescription = stringResource(
                    id = if (playerState.isPlaying) R.string.action_pause else R.string.action_play
                ),
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        SkipButton(
            isNext = true,
            onClick = actionHandler::onSkipNext,
            modifier = Modifier.size(64.dp),
            iconSize = 54.dp
        )

        Box {
            IconButton(
                onClick = { showMenu = true },
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(id = R.string.nav_more_options),
                    modifier = Modifier.size(42.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                currentTrack?.collectionId?.let { albumId ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_album),
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(text = stringResource(id = R.string.action_view_album))
                            }
                        },
                        onClick = {
                            showMenu = false
                            onAlbumClick(albumId)
                        }
                    )
                }
            }
        }
    }
}

@PreviewAuto
@Composable
private fun AutoPlayerScreenPreview() {
    AutomotiveTheme {
        AutoPlayerScreenContent(
            playerState = PlayerStateUiModel(
                currentTrack = PreviewDataMocks.previewTrack.toUiModel(),
                isPlaying = true,
                currentPosition = 1500,
                duration = 3000
            ),
            actionHandler = PlayerScreenContract.NoOp(),
            onNavigateBack = {}
        )
    }
}
