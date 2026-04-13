package com.example.moiseschallenge.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.example.moiseschallenge.domain.model.PlayerState
import com.example.moiseschallenge.domain.model.RepeatMode
import com.example.moiseschallenge.domain.model.Track
import com.example.moiseschallenge.domain.repository.PlayerRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoisesPlayer @Inject constructor(
    @ApplicationContext private val context: Context
) : PlayerRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val exoPlayer: ExoPlayer by lazy {
        ExoPlayer.Builder(context).build().apply {
            addListener(playerListener)
        }
    }

    private val _playerState = MutableStateFlow(PlayerState())
    override val playerState: Flow<PlayerState> = _playerState.asStateFlow()

    private val currentQueue = mutableListOf<Track>()
    private var currentIndex = -1

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            updatePlayingState()
            if (playbackState == Player.STATE_ENDED) {
                handleTrackEnded()
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            updatePlayingState()
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            updateCurrentTrack()
        }
    }

    init {
        startPositionUpdater()
    }

    private fun startPositionUpdater() {
        scope.launch {
            while (isActive) {
                if (exoPlayer.isPlaying) {
                    _playerState.update {
                        it.copy(
                            currentPosition = exoPlayer.currentPosition,
                            duration = exoPlayer.duration.coerceAtLeast(0)
                        )
                    }
                }
                delay(200)
            }
        }
    }

    private fun updatePlayingState() {
        _playerState.update {
            it.copy(
                isPlaying = exoPlayer.isPlaying,
                currentPosition = exoPlayer.currentPosition,
                duration = exoPlayer.duration.coerceAtLeast(0)
            )
        }
    }

    private fun updateCurrentTrack() {
        val track = if (currentIndex in currentQueue.indices) {
            currentQueue[currentIndex]
        } else null

        _playerState.update {
            it.copy(
                currentTrack = track,
                currentQueueIndex = currentIndex,
                queue = currentQueue.toList()
            )
        }
    }

    private fun handleTrackEnded() {
        when (_playerState.value.repeatMode) {
            RepeatMode.ONE -> {
                // Play once more, then disable repeat
                _playerState.update { it.copy(repeatMode = RepeatMode.OFF) }
                exoPlayer.seekTo(0)
                exoPlayer.play()
            }
            RepeatMode.ALL -> {
                if (currentIndex >= currentQueue.lastIndex) {
                    playTrackAtIndex(0)
                } else {
                    skipToNext()
                }
            }
            RepeatMode.OFF -> {
                if (currentIndex < currentQueue.lastIndex) {
                    skipToNext()
                }
            }
        }
    }

    @OptIn(UnstableApi::class)
    override fun play(track: Track) {
        currentQueue.clear()
        currentQueue.add(track)
        currentIndex = 0
        playTrackAtIndex(0)
    }

    @OptIn(UnstableApi::class)
    override fun playQueue(tracks: List<Track>, startIndex: Int) {
        currentQueue.clear()
        currentQueue.addAll(tracks)
        currentIndex = startIndex.coerceIn(0, tracks.lastIndex)
        playTrackAtIndex(currentIndex)
    }

    @OptIn(UnstableApi::class)
    private fun playTrackAtIndex(index: Int) {
        if (index !in currentQueue.indices) return

        currentIndex = index
        val track = currentQueue[index]
        val previewUrl = track.previewUrl ?: return

        val mediaItem = MediaItem.Builder()
            .setUri(previewUrl.toUri())
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(track.trackName)
                    .setArtist(track.artistName)
                    .setAlbumTitle(track.collectionName)
                    .setArtworkUri(track.artworkUrlHighRes?.toUri())
                    .build()
            )
            .build()

        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()

        updateCurrentTrack()
    }

    override fun pause() {
        exoPlayer.pause()
    }

    override fun resume() {
        exoPlayer.play()
    }

    override fun stop() {
        exoPlayer.stop()
        _playerState.update {
            it.copy(isPlaying = false, currentPosition = 0)
        }
    }

    override fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
        _playerState.update {
            it.copy(currentPosition = position)
        }
    }

    override fun skipToNext() {
        if (currentIndex < currentQueue.lastIndex) {
            playTrackAtIndex(currentIndex + 1)
        } else if (_playerState.value.repeatMode == RepeatMode.ALL) {
            playTrackAtIndex(0)
        }
    }

    override fun skipToPrevious() {
        if (exoPlayer.currentPosition > 3000) {
            seekTo(0)
        } else if (currentIndex > 0) {
            playTrackAtIndex(currentIndex - 1)
        } else if (_playerState.value.repeatMode == RepeatMode.ALL) {
            playTrackAtIndex(currentQueue.lastIndex)
        }
    }

    override fun seekForward(seconds: Int) {
        val newPosition = (exoPlayer.currentPosition + seconds * 1000).coerceAtMost(exoPlayer.duration)
        seekTo(newPosition)
    }

    override fun seekBackward(seconds: Int) {
        val newPosition = (exoPlayer.currentPosition - seconds * 1000).coerceAtLeast(0)
        seekTo(newPosition)
    }

    override fun setRepeatMode(mode: RepeatMode) {
        _playerState.update { it.copy(repeatMode = mode) }
        // Note: RepeatMode.ONE is handled manually in handleTrackEnded()
        exoPlayer.repeatMode = when (mode) {
            RepeatMode.OFF, RepeatMode.ONE -> Player.REPEAT_MODE_OFF
            RepeatMode.ALL -> Player.REPEAT_MODE_ALL
        }
    }

    override fun addToQueue(track: Track) {
        currentQueue.add(track)
        _playerState.update { it.copy(queue = currentQueue.toList()) }
    }

    override fun removeFromQueue(index: Int) {
        if (index in currentQueue.indices) {
            currentQueue.removeAt(index)
            if (index < currentIndex) {
                currentIndex--
            }
            _playerState.update {
                it.copy(queue = currentQueue.toList(), currentQueueIndex = currentIndex)
            }
        }
    }

    override fun clearQueue() {
        currentQueue.clear()
        currentIndex = -1
        exoPlayer.stop()
        _playerState.update {
            PlayerState()
        }
    }

    override fun release() {
        exoPlayer.release()
    }
}
