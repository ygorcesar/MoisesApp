package com.example.moiseschallenge.automotive.ui.browse

import app.cash.turbine.test
import com.example.moiseschallenge.domain.model.PlayerState
import com.example.moiseschallenge.domain.model.Track
import com.example.moiseschallenge.domain.repository.MusicRepository
import com.example.moiseschallenge.player.MoisesPlayer
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AutoBrowseViewModelTest {

    private lateinit var musicRepository: MusicRepository
    private lateinit var player: MoisesPlayer
    private lateinit var viewModel: AutoBrowseViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val playerStateFlow = MutableStateFlow(PlayerState())

    private val testTracks = listOf(
        createTestTrack(1),
        createTestTrack(2),
        createTestTrack(3)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        musicRepository = mockk()
        player = mockk(relaxed = true)

        every { player.playerState } returns playerStateFlow
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): AutoBrowseViewModel {
        return AutoBrowseViewModel(musicRepository, player)
    }

    @Test
    fun `loads tracks successfully`() = runTest {
        coEvery { musicRepository.searchTracksOneShot("pop", limit = 50) } returns Result.success(testTracks)

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val loadedState = awaitItem()
            assertThat(loadedState.isLoading).isFalse()
            assertThat(loadedState.tracks).hasSize(3)
            assertThat(loadedState.error).isNull()
        }
    }

    @Test
    fun `handles load error`() = runTest {
        coEvery { musicRepository.searchTracksOneShot("pop", limit = 50) } returns Result.failure(Exception("Network error"))

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val errorState = awaitItem()
            assertThat(errorState.isLoading).isFalse()
            assertThat(errorState.error).isEqualTo("Network error")
        }
    }

    @Test
    fun `onTrackClick plays track from queue`() = runTest {
        coEvery { musicRepository.searchTracksOneShot("pop", limit = 50) } returns Result.success(testTracks)

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val track = testTracks[1]
        viewModel.onTrackClick(track)

        verify { player.playQueue(testTracks, 1) }
    }

    @Test
    fun `onSkipNext calls player skipToNext`() = runTest {
        coEvery { musicRepository.searchTracksOneShot("pop", limit = 50) } returns Result.success(testTracks)

        viewModel = createViewModel()

        viewModel.onSkipNext()

        verify { player.skipToNext() }
    }

    @Test
    fun `onSkipPrevious calls player skipToPrevious`() = runTest {
        coEvery { musicRepository.searchTracksOneShot("pop", limit = 50) } returns Result.success(testTracks)

        viewModel = createViewModel()

        viewModel.onSkipPrevious()

        verify { player.skipToPrevious() }
    }

    @Test
    fun `onPause calls player pause`() = runTest {
        coEvery { musicRepository.searchTracksOneShot("pop", limit = 50) } returns Result.success(testTracks)

        viewModel = createViewModel()

        viewModel.onPause()

        verify { player.pause() }
    }

    @Test
    fun `onResume calls player resume`() = runTest {
        coEvery { musicRepository.searchTracksOneShot("pop", limit = 50) } returns Result.success(testTracks)

        viewModel = createViewModel()

        viewModel.onResume()

        verify { player.resume() }
    }

    @Test
    fun `playerState emits initial state`() = runTest {
        coEvery { musicRepository.searchTracksOneShot("pop", limit = 50) } returns Result.success(testTracks)

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
        artistName = "Artist",
        collectionName = "Album",
        artworkUrl100 = null,
        previewUrl = "https://somepreview.com/file.mp3",
        trackTimeMillis = 180000,
        collectionId = 100,
        artistId = 200,
        trackNumber = id.toInt(),
        discNumber = 1,
        releaseDate = null,
        primaryGenreName = "Pop"
    )
}
