package com.example.moiseschallenge.ui.songs

import androidx.paging.PagingData
import app.cash.turbine.test
import com.example.moiseschallenge.domain.model.PlayerState
import com.example.moiseschallenge.domain.model.Track
import com.example.moiseschallenge.domain.usecase.GetPlayerStateUseCase
import com.example.moiseschallenge.domain.usecase.PlayerControlsUseCase
import com.example.moiseschallenge.domain.usecase.PlayTrackUseCase
import com.example.moiseschallenge.domain.usecase.SearchTracksUseCase
import com.google.common.truth.Truth.assertThat
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
class SongsViewModelTest {

    private lateinit var searchTracksUseCase: SearchTracksUseCase
    private lateinit var playTrackUseCase: PlayTrackUseCase
    private lateinit var playerControlsUseCase: PlayerControlsUseCase
    private lateinit var getPlayerStateUseCase: GetPlayerStateUseCase
    private lateinit var viewModel: SongsViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        searchTracksUseCase = mockk()
        playTrackUseCase = mockk(relaxed = true)
        playerControlsUseCase = mockk(relaxed = true)
        getPlayerStateUseCase = mockk()

        every { searchTracksUseCase(any()) } returns flowOf(PagingData.empty())
        every { getPlayerStateUseCase() } returns flowOf(PlayerState())

        viewModel = SongsViewModel(
            searchTracksUseCase,
            playTrackUseCase,
            playerControlsUseCase,
            getPlayerStateUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial search query is empty`() {
        assertThat(viewModel.searchQuery.value).isEmpty()
    }

    @Test
    fun `onSearchQueryChange updates search query`() = runTest {
        viewModel.searchQuery.test {
            assertThat(awaitItem()).isEmpty()

            viewModel.onSearchQueryChange("rock")

            assertThat(awaitItem()).isEqualTo("rock")
        }
    }

    @Test
    fun `onTrackClick plays track from queue`() {
        val track1 = createTestTrack(1)
        val track2 = createTestTrack(2)
        val tracks = listOf(track1, track2)

        viewModel.onTrackClick(track2, tracks)

        verify { playTrackUseCase.playQueue(tracks, 1) }
    }

    @Test
    fun `onTrackClick plays single track when not in list`() {
        val track = createTestTrack(1)
        val emptyTracks = emptyList<Track>()

        viewModel.onTrackClick(track, emptyTracks)

        verify { playTrackUseCase(track) }
    }

    @Test
    fun `playerState emits initial state`() = runTest {
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
        previewUrl = null,
        trackTimeMillis = 180000,
        collectionId = 100,
        artistId = 200,
        trackNumber = id.toInt(),
        discNumber = 1,
        releaseDate = null,
        primaryGenreName = "Pop",
        isExplicit = false
    )
}
