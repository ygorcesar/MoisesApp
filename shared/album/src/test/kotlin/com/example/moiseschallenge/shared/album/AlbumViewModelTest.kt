package com.example.moiseschallenge.shared.album

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.moiseschallenge.domain.model.Album
import com.example.moiseschallenge.domain.model.PlayerState
import com.example.moiseschallenge.domain.model.Track
import com.example.moiseschallenge.domain.repository.MusicRepository
import com.example.moiseschallenge.domain.usecase.GetPlayerStateUseCase
import com.example.moiseschallenge.domain.usecase.PlayTrackUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumViewModelTest {

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var musicRepository: MusicRepository
    private lateinit var playTrackUseCase: PlayTrackUseCase
    private lateinit var getPlayerStateUseCase: GetPlayerStateUseCase
    private lateinit var viewModel: AlbumViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val testAlbum = Album(
        id = 100,
        collectionName = "Test Album",
        artistName = "Test Artist",
        artworkUrl100 = null,
        primaryGenreName = "Pop",
        trackCount = 3,
        releaseDate = "2024-01-01",
        collectionPrice = null,
        currency = null
    )

    private val testTracks = listOf(
        createTestTrack(1),
        createTestTrack(2),
        createTestTrack(3)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        savedStateHandle = SavedStateHandle(mapOf("albumId" to 100L))
        musicRepository = mockk()
        playTrackUseCase = mockk(relaxed = true)
        getPlayerStateUseCase = mockk()

        every { getPlayerStateUseCase() } returns flowOf(PlayerState())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): AlbumViewModel {
        return AlbumViewModel(
            savedStateHandle,
            musicRepository,
            playTrackUseCase,
            getPlayerStateUseCase
        )
    }

    @Test
    fun `initial state is loading`() = runTest {
        coEvery { musicRepository.getAlbumDetails(100) } returns Result.success(testAlbum)
        coEvery { musicRepository.getAlbumTracks(100) } returns Result.success(testTracks)

        viewModel = createViewModel()

        viewModel.uiState.test {
            val initialState = awaitItem()
            assertThat(initialState.isLoading).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loads album and tracks successfully`() = runTest {
        coEvery { musicRepository.getAlbumDetails(100) } returns Result.success(testAlbum)
        coEvery { musicRepository.getAlbumTracks(100) } returns Result.success(testTracks)

        viewModel = createViewModel()

        viewModel.uiState.test {
            skipItems(1) // Skip initial loading state
            testDispatcher.scheduler.advanceUntilIdle()

            val loadedState = awaitItem()
            assertThat(loadedState.isLoading).isFalse()
            assertThat(loadedState.album).isEqualTo(testAlbum)
            assertThat(loadedState.tracks).hasSize(3)
            assertThat(loadedState.error).isNull()
        }
    }

    @Test
    fun `handles album load error`() = runTest {
        coEvery { musicRepository.getAlbumDetails(100) } returns Result.failure(Exception("Network error"))
        coEvery { musicRepository.getAlbumTracks(100) } returns Result.success(testTracks)

        viewModel = createViewModel()

        viewModel.uiState.test {
            skipItems(1) // Skip initial loading state
            testDispatcher.scheduler.advanceUntilIdle()

            val errorState = awaitItem()
            assertThat(errorState.isLoading).isFalse()
            assertThat(errorState.error).isEqualTo("Network error")
        }
    }

    @Test
    fun `handles tracks load error`() = runTest {
        coEvery { musicRepository.getAlbumDetails(100) } returns Result.success(testAlbum)
        coEvery { musicRepository.getAlbumTracks(100) } returns Result.failure(Exception("Failed to load tracks"))

        viewModel = createViewModel()

        viewModel.uiState.test {
            skipItems(1) // Skip initial loading state
            testDispatcher.scheduler.advanceUntilIdle()

            val errorState = awaitItem()
            assertThat(errorState.isLoading).isFalse()
            assertThat(errorState.error).isEqualTo("Failed to load tracks")
        }
    }

    @Test
    fun `onTrackClick plays track from queue`() = runTest {
        coEvery { musicRepository.getAlbumDetails(100) } returns Result.success(testAlbum)
        coEvery { musicRepository.getAlbumTracks(100) } returns Result.success(testTracks)

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val track = testTracks[1]
        viewModel.onTrackClick(track)

        verify { playTrackUseCase.playQueue(any(), 1) }
    }

    @Test
    fun `onTrackClick plays single track when track not in list`() = runTest {
        coEvery { musicRepository.getAlbumDetails(100) } returns Result.success(testAlbum)
        coEvery { musicRepository.getAlbumTracks(100) } returns Result.success(testTracks)

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val unknownTrack = createTestTrack(999)
        viewModel.onTrackClick(unknownTrack)

        verify { playTrackUseCase(unknownTrack) }
    }

    @Test
    fun `retry reloads album data`() = runTest {
        coEvery { musicRepository.getAlbumDetails(100) } returns Result.failure(Exception("Error"))
        coEvery { musicRepository.getAlbumTracks(100) } returns Result.success(testTracks)

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        // Setup success response for retry
        coEvery { musicRepository.getAlbumDetails(100) } returns Result.success(testAlbum)

        viewModel.retry()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.album).isEqualTo(testAlbum)
            assertThat(state.error).isNull()
        }
    }

    @Test
    fun `playerState emits initial state`() = runTest {
        coEvery { musicRepository.getAlbumDetails(100) } returns Result.success(testAlbum)
        coEvery { musicRepository.getAlbumTracks(100) } returns Result.success(testTracks)

        viewModel = createViewModel()

        viewModel.playerState.test {
            val state = awaitItem()
            assertThat(state.currentTrack).isNull()
            assertThat(state.isPlaying).isFalse()
        }
    }

    private fun createTestTrack(id: Long) = Track(
        id = id,
        trackName = "Track $id",
        artistName = "Test Artist",
        collectionName = "Test Album",
        artworkUrl100 = null,
        previewUrl = "https://example.com/preview.mp3",
        trackTimeMillis = 180000,
        collectionId = 100,
        artistId = 200,
        trackNumber = id.toInt(),
        discNumber = 1,
        releaseDate = null,
        primaryGenreName = "Pop"
    )
}
