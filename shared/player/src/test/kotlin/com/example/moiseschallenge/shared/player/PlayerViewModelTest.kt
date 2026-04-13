package com.example.moiseschallenge.shared.player

import app.cash.turbine.test
import com.example.moiseschallenge.domain.model.PlayerState
import com.example.moiseschallenge.domain.model.RepeatMode
import com.example.moiseschallenge.domain.model.Track
import com.example.moiseschallenge.domain.usecase.GetPlayerStateUseCase
import com.example.moiseschallenge.domain.usecase.PlayerControlsUseCase
import com.google.common.truth.Truth.assertThat
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
class PlayerViewModelTest {

    private lateinit var getPlayerStateUseCase: GetPlayerStateUseCase
    private lateinit var playerControlsUseCase: PlayerControlsUseCase
    private lateinit var viewModel: PlayerViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val playerStateFlow = MutableStateFlow(PlayerState())

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getPlayerStateUseCase = mockk()
        playerControlsUseCase = mockk(relaxed = true)

        every { getPlayerStateUseCase() } returns playerStateFlow

        viewModel = PlayerViewModel(
            getPlayerStateUseCase,
            playerControlsUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `playerState emits initial state`() = runTest {
        viewModel.playerState.test {
            val state = awaitItem()
            assertThat(state.currentTrack).isNull()
            assertThat(state.isPlaying).isFalse()
        }
    }

    @Test
    fun `playerState maps domain state to UI model`() = runTest {
        val track = createTestTrack(1)

        viewModel.playerState.test {
            awaitItem() // initial

            playerStateFlow.value = PlayerState(
                currentTrack = track,
                isPlaying = true,
                currentPosition = 5000,
                duration = 180000,
                repeatMode = RepeatMode.ALL
            )

            val state = awaitItem()
            assertThat(state.currentTrack?.id).isEqualTo(1)
            assertThat(state.isPlaying).isTrue()
            assertThat(state.currentPosition).isEqualTo(5000)
            assertThat(state.duration).isEqualTo(180000)
            assertThat(state.repeatMode).isEqualTo(RepeatMode.ALL)
        }
    }

    @Test
    fun `onPlayPauseClick pauses when playing`() = runTest {
        viewModel.playerState.test {
            awaitItem() // initial
            playerStateFlow.value = PlayerState(isPlaying = true)
            awaitItem() // updated state

            viewModel.onPlayPauseClick()
            verify { playerControlsUseCase.pause() }
        }
    }

    @Test
    fun `onPlayPauseClick resumes when paused`() = runTest {
        viewModel.playerState.test {
            awaitItem() // initial state (isPlaying = false)

            viewModel.onPlayPauseClick()
            verify { playerControlsUseCase.resume() }
        }
    }

    @Test
    fun `onSkipNext calls playerControls skipToNext`() {
        viewModel.onSkipNext()
        verify { playerControlsUseCase.skipToNext() }
    }

    @Test
    fun `onSkipPrevious calls playerControls skipToPrevious`() {
        viewModel.onSkipPrevious()
        verify { playerControlsUseCase.skipToPrevious() }
    }

    @Test
    fun `onSeekForward calls playerControls seekForward with 15 seconds`() {
        viewModel.onSeekForward()
        verify { playerControlsUseCase.seekForward(15) }
    }

    @Test
    fun `onSeekBackward calls playerControls seekBackward with 15 seconds`() {
        viewModel.onSeekBackward()
        verify { playerControlsUseCase.seekBackward(15) }
    }

    @Test
    fun `onSeekTo calculates position from progress and seeks`() = runTest {
        viewModel.playerState.test {
            awaitItem() // initial
            playerStateFlow.value = PlayerState(duration = 100000)
            awaitItem() // updated state

            viewModel.onSeekTo(0.5f)
            verify { playerControlsUseCase.seekTo(50000) }
        }
    }

    @Test
    fun `onToggleRepeat cycles OFF to ALL`() = runTest {
        viewModel.playerState.test {
            awaitItem() // initial (repeatMode = OFF by default)

            viewModel.onToggleRepeat()
            verify { playerControlsUseCase.setRepeatMode(RepeatMode.ALL) }
        }
    }

    @Test
    fun `onToggleRepeat cycles ALL to ONE`() = runTest {
        viewModel.playerState.test {
            awaitItem() // initial
            playerStateFlow.value = PlayerState(repeatMode = RepeatMode.ALL)
            awaitItem() // updated state

            viewModel.onToggleRepeat()
            verify { playerControlsUseCase.setRepeatMode(RepeatMode.ONE) }
        }
    }

    @Test
    fun `onToggleRepeat cycles ONE to OFF`() = runTest {
        viewModel.playerState.test {
            awaitItem() // initial
            playerStateFlow.value = PlayerState(repeatMode = RepeatMode.ONE)
            awaitItem() // updated state

            viewModel.onToggleRepeat()
            verify { playerControlsUseCase.setRepeatMode(RepeatMode.OFF) }
        }
    }

    private fun createTestTrack(id: Long) = Track(
        id = id,
        trackName = "Track $id",
        artistName = "Artist",
        collectionName = "Album",
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
