package com.example.moiseschallenge.ui.songs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moiseschallenge.domain.model.Track
import com.example.moiseschallenge.resources.R
import com.example.moiseschallenge.ui.components.EmptyContent
import com.example.moiseschallenge.ui.components.ErrorContent
import com.example.moiseschallenge.ui.components.LoadingContent
import com.example.moiseschallenge.ui.components.MiniPlayerBar
import com.example.moiseschallenge.ui.components.TrackActionSheet
import com.example.moiseschallenge.ui.components.TrackListItem
import com.example.moiseschallenge.ui.model.PlayerStateUiModel
import com.example.moiseschallenge.ui.model.toUiModel
import com.example.moiseschallenge.ui.theme.MoisesTheme
import com.example.moiseschallenge.ui.util.PreviewDataMocks

@Composable
fun SongsScreen(
    onTrackClick: (Long) -> Unit,
    onAlbumClick: (Long) -> Unit,
    onPlayerClick: () -> Unit,
    viewModel: SongsViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val tracks = viewModel.tracks.collectAsLazyPagingItems()
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val actionHandler: SongsScreenContract.ActionHandler = remember { viewModel }

    SongsScreenContent(
        searchQuery = searchQuery,
        tracks = tracks,
        playerState = playerState,
        actionHandler = actionHandler,
        onTrackClick = onTrackClick,
        onAlbumClick = onAlbumClick,
        onPlayerClick = onPlayerClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SongsScreenContent(
    searchQuery: String,
    tracks: LazyPagingItems<Track>,
    playerState: PlayerStateUiModel,
    actionHandler: SongsScreenContract.ActionHandler,
    onTrackClick: (Long) -> Unit,
    onAlbumClick: (Long) -> Unit,
    onPlayerClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    var selectedTrackForSheet by remember { mutableStateOf<Track?>(null) }
    var isSearchActive by remember { mutableStateOf(false) }
    val searchFocusRequester = remember { FocusRequester() }

    LaunchedEffect(isSearchActive) {
        if (isSearchActive) {
            searchFocusRequester.requestFocus()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.title_songs),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                actions = {
                    if (!isSearchActive) {
                        IconButton(onClick = { isSearchActive = true }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = stringResource(id = R.string.cd_search),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = {
            playerState.currentTrack?.let { track ->
                MiniPlayerBar(
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
                    onSkipNext = actionHandler::onSkipNext,
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AnimatedVisibility(
                visible = isSearchActive,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = actionHandler::onSearchQueryChange,
                    onSearch = { focusManager.clearFocus() },
                    onClose = {
                        isSearchActive = false
                        actionHandler.onSearchQueryChange("")
                        focusManager.clearFocus()
                    },
                    focusRequester = searchFocusRequester,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            TracksContent(
                tracks = tracks,
                currentTrackId = playerState.currentTrack?.id,
                isPlaying = playerState.isPlaying,
                onTrackClick = { track ->
                    val loadedTracks = (0 until tracks.itemCount)
                        .mapNotNull { tracks[it] }
                    actionHandler.onTrackClick(track, loadedTracks)
                    onTrackClick(track.id)
                },
                onMoreClick = { track ->
                    selectedTrackForSheet = track
                }
            )
        }
    }

    selectedTrackForSheet?.let { track ->
        TrackActionSheet(
            track = track,
            onDismiss = { selectedTrackForSheet = null },
            onViewAlbum = {
                track.collectionId?.let { onAlbumClick(it) }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SongsScreenContent(
    searchQuery: String,
    tracks: List<Track>,
    playerState: PlayerStateUiModel,
    actionHandler: SongsScreenContract.ActionHandler,
    onTrackClick: (Long) -> Unit,
    onAlbumClick: (Long) -> Unit,
    onPlayerClick: () -> Unit,
    isLoading: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    val focusManager = LocalFocusManager.current

    var selectedTrackForSheet by remember { mutableStateOf<Track?>(null) }
    var isSearchActive by remember { mutableStateOf(false) }
    val searchFocusRequester = remember { FocusRequester() }

    LaunchedEffect(isSearchActive) {
        if (isSearchActive) {
            searchFocusRequester.requestFocus()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.title_songs),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                actions = {
                    if (!isSearchActive) {
                        IconButton(onClick = { isSearchActive = true }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = stringResource(id = R.string.cd_search),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = {
            playerState.currentTrack?.let { track ->
                MiniPlayerBar(
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
                    onSkipNext = actionHandler::onSkipNext,
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AnimatedVisibility(
                visible = isSearchActive,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = actionHandler::onSearchQueryChange,
                    onSearch = { focusManager.clearFocus() },
                    onClose = {
                        isSearchActive = false
                        actionHandler.onSearchQueryChange("")
                        focusManager.clearFocus()
                    },
                    focusRequester = searchFocusRequester,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            TracksListContent(
                tracks = tracks,
                currentTrackId = playerState.currentTrack?.id,
                isPlaying = playerState.isPlaying,
                isLoading = isLoading,
                isError = isError,
                errorMessage = errorMessage,
                onTrackClick = { track ->
                    actionHandler.onTrackClick(track, tracks)
                    onTrackClick(track.id)
                },
                onMoreClick = { track ->
                    selectedTrackForSheet = track
                },
                onRetry = {}
            )
        }
    }

    selectedTrackForSheet?.let { track ->
        TrackActionSheet(
            track = track,
            onDismiss = { selectedTrackForSheet = null },
            onViewAlbum = {
                track.collectionId?.let { onAlbumClick(it) }
            }
        )
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClose: () -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.focusRequester(focusRequester),
        placeholder = {
            Text(
                text = stringResource(id = R.string.action_search),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = stringResource(id = R.string.cd_search),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingIcon = {
            IconButton(onClick = onClose) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = stringResource(id = R.string.cd_close_search),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
private fun TracksContent(
    tracks: LazyPagingItems<Track>,
    currentTrackId: Long?,
    isPlaying: Boolean,
    onTrackClick: (Track) -> Unit,
    onMoreClick: (Track) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            tracks.loadState.refresh is LoadState.Loading -> {
                LoadingContent(message = stringResource(id = R.string.loading_songs))
            }

            tracks.loadState.refresh is LoadState.Error -> {
                val error = (tracks.loadState.refresh as LoadState.Error).error
                ErrorContent(
                    message = error.message ?: stringResource(id = R.string.error_generic),
                    onRetry = { tracks.retry() }
                )
            }

            tracks.itemCount == 0 && tracks.loadState.refresh is LoadState.NotLoading -> EmptyContent(
                title = stringResource(id = R.string.empty_no_songs_found),
                message = stringResource(id = R.string.empty_try_different_search)
            )

            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(
                    count = tracks.itemCount,
                    key = { index -> tracks[index]?.id ?: index }
                ) { index ->
                    tracks[index]?.let { track ->
                        TrackListItem(
                            track = track,
                            onClick = { onTrackClick(track) },
                            onMoreClick = { onMoreClick(track) },
                            isCurrentTrack = track.id == currentTrackId,
                            isPlaying = isPlaying
                        )
                    }
                }

                if (tracks.loadState.append is LoadState.Loading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            LoadingContent()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TracksListContent(
    tracks: List<Track>,
    currentTrackId: Long?,
    isPlaying: Boolean,
    isLoading: Boolean,
    isError: Boolean,
    errorMessage: String?,
    onTrackClick: (Track) -> Unit,
    onMoreClick: (Track) -> Unit,
    onRetry: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> LoadingContent(message = stringResource(id = R.string.loading_songs))

            isError -> ErrorContent(
                message = errorMessage ?: stringResource(id = R.string.error_generic),
                onRetry = onRetry
            )

            tracks.isEmpty() -> EmptyContent(
                title = stringResource(id = R.string.empty_no_songs_found),
                message = stringResource(id = R.string.empty_try_different_search)
            )

            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(
                    items = tracks,
                    key = { it.id }
                ) { track ->
                    TrackListItem(
                        track = track,
                        onClick = { onTrackClick(track) },
                        onMoreClick = { onMoreClick(track) },
                        isCurrentTrack = track.id == currentTrackId,
                        isPlaying = isPlaying
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SongsScreenPreview() {
    MoisesTheme {
        SongsScreenContent(
            searchQuery = "",
            tracks = PreviewDataMocks.previewTracksItems2,
            playerState = PlayerStateUiModel(),
            actionHandler = SongsScreenContract.NoOp(),
            onTrackClick = {},
            onAlbumClick = {},
            onPlayerClick = {}
        )
    }
}

@Preview
@Composable
private fun SongsScreenWithPlayerPreview() {
    MoisesTheme {
        SongsScreenContent(
            searchQuery = "",
            tracks = PreviewDataMocks.previewTracksItems2,
            playerState = PlayerStateUiModel(
                currentTrack = PreviewDataMocks.previewTracksItems2[2].toUiModel(),
                isPlaying = true,
                currentPosition = 72800,
                duration = 182000
            ),
            actionHandler = SongsScreenContract.NoOp(),
            onTrackClick = {},
            onAlbumClick = {},
            onPlayerClick = {}
        )
    }
}

@Preview
@Composable
private fun SongsScreenLoadingPreview() {
    MoisesTheme {
        SongsScreenContent(
            searchQuery = "",
            tracks = emptyList(),
            playerState = PlayerStateUiModel(),
            actionHandler = SongsScreenContract.NoOp(),
            onTrackClick = {},
            onAlbumClick = {},
            onPlayerClick = {},
            isLoading = true
        )
    }
}

@Preview
@Composable
private fun SongsScreenEmptyPreview() {
    MoisesTheme {
        SongsScreenContent(
            searchQuery = "xyz123",
            tracks = emptyList(),
            playerState = PlayerStateUiModel(),
            actionHandler = SongsScreenContract.NoOp(),
            onTrackClick = {},
            onAlbumClick = {},
            onPlayerClick = {}
        )
    }
}

@Preview
@Composable
private fun SongsScreenErrorPreview() {
    MoisesTheme {
        SongsScreenContent(
            searchQuery = "",
            tracks = emptyList(),
            playerState = PlayerStateUiModel(),
            actionHandler = SongsScreenContract.NoOp(),
            onTrackClick = {},
            onAlbumClick = {},
            onPlayerClick = {},
            isError = true,
            errorMessage = "Network error. Please try again."
        )
    }
}

@Preview
@Composable
private fun SearchBarPreview() {
    MoisesTheme {
        SearchBar(
            query = "",
            onQueryChange = {},
            onSearch = {},
            onClose = {},
            focusRequester = remember { FocusRequester() }
        )
    }
}

@Preview
@Composable
private fun SearchBarWithQueryPreview() {
    MoisesTheme {
        SearchBar(
            query = "Queen",
            onQueryChange = {},
            onSearch = {},
            onClose = {},
            focusRequester = remember { FocusRequester() }
        )
    }
}
