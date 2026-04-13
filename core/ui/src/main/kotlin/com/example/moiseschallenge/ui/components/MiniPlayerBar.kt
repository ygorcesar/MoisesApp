package com.example.moiseschallenge.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.moiseschallenge.domain.model.Track
import com.example.moiseschallenge.ui.theme.MoisesTheme

enum class MiniPlayerBarStyle(
    val artworkSize: Dp,
    val artworkCornerRadius: Dp,
    val artworkPlaceholderSize: Dp,
    val progressHeight: Dp,
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val contentSpacing: Dp,
    val skipButtonSize: Dp,
    val skipIconSize: Dp,
    val playPauseButtonSize: Dp,
    val playPauseIconSize: Dp,
    val controlSpacing: Dp
) {
    Mobile(
        artworkSize = 48.dp,
        artworkCornerRadius = 4.dp,
        artworkPlaceholderSize = 24.dp,
        progressHeight = 2.dp,
        horizontalPadding = 16.dp,
        verticalPadding = 8.dp,
        contentSpacing = 12.dp,
        skipButtonSize = 40.dp,
        skipIconSize = 24.dp,
        playPauseButtonSize = 40.dp,
        playPauseIconSize = 20.dp,
        controlSpacing = 0.dp
    ),
    Auto(
        artworkSize = 56.dp,
        artworkCornerRadius = 6.dp,
        artworkPlaceholderSize = 28.dp,
        progressHeight = 3.dp,
        horizontalPadding = 24.dp,
        verticalPadding = 12.dp,
        contentSpacing = 16.dp,
        skipButtonSize = 48.dp,
        skipIconSize = 28.dp,
        playPauseButtonSize = 52.dp,
        playPauseIconSize = 28.dp,
        controlSpacing = 8.dp
    )
}

@Composable
fun MiniPlayerBar(
    track: Track,
    isPlaying: Boolean,
    progress: Float,
    onBarClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onSkipPrevious: () -> Unit,
    onSkipNext: () -> Unit,
    modifier: Modifier = Modifier,
    style: MiniPlayerBarStyle = MiniPlayerBarStyle.Mobile,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    progressColor: Color = MaterialTheme.colorScheme.primary
) {
    val titleStyle: TextStyle = when (style) {
        MiniPlayerBarStyle.Mobile -> MaterialTheme.typography.bodyMedium
        MiniPlayerBarStyle.Auto -> MaterialTheme.typography.titleMedium
    }
    val subtitleStyle: TextStyle = when (style) {
        MiniPlayerBarStyle.Mobile -> MaterialTheme.typography.bodySmall
        MiniPlayerBarStyle.Auto -> MaterialTheme.typography.bodyMedium
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
    ) {
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(style.progressHeight),
            color = progressColor,
            trackColor = Color.Transparent
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onBarClick)
                .padding(horizontal = style.horizontalPadding, vertical = style.verticalPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AlbumArtwork(
                artworkUrl = track.artworkUrl100,
                modifier = Modifier.size(style.artworkSize),
                cornerRadius = style.artworkCornerRadius,
                placeholderSize = style.artworkPlaceholderSize
            )

            Spacer(modifier = Modifier.width(style.contentSpacing))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = track.trackName,
                    style = titleStyle.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = track.artistName,
                    style = subtitleStyle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            SkipButton(
                isNext = false,
                onClick = onSkipPrevious,
                iconSize = style.skipIconSize,
                modifier = Modifier.size(style.skipButtonSize)
            )

            if (style.controlSpacing > 0.dp) {
                Spacer(modifier = Modifier.width(style.controlSpacing))
            }

            PlayPauseButton(
                isPlaying = isPlaying,
                onClick = onPlayPauseClick,
                buttonSize = style.playPauseButtonSize,
                iconSize = style.playPauseIconSize,
                backgroundColor = MaterialTheme.colorScheme.surface
            )

            if (style.controlSpacing > 0.dp) {
                Spacer(modifier = Modifier.width(style.controlSpacing))
            }

            SkipButton(
                isNext = true,
                onClick = onSkipNext,
                iconSize = style.skipIconSize,
                modifier = Modifier.size(style.skipButtonSize)
            )
        }
    }
}

@Composable
fun AutoMiniPlayerBar(
    track: Track,
    isPlaying: Boolean,
    progress: Float,
    onBarClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onSkipPrevious: () -> Unit,
    onSkipNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    MiniPlayerBar(
        track = track,
        isPlaying = isPlaying,
        progress = progress,
        onBarClick = onBarClick,
        onPlayPauseClick = onPlayPauseClick,
        onSkipPrevious = onSkipPrevious,
        onSkipNext = onSkipNext,
        modifier = modifier,
        style = MiniPlayerBarStyle.Auto
    )
}

private val previewTrack = Track(
    id = 1,
    trackName = "Bohemian Rhapsody",
    artistName = "Queen",
    collectionName = "A Night at the Opera",
    artworkUrl100 = null,
    previewUrl = null,
    trackTimeMillis = 354000,
    collectionId = 1,
    artistId = 1,
    trackNumber = 1,
    discNumber = 1,
    releaseDate = "1975-10-31",
    primaryGenreName = "Rock"
)

@Preview(showBackground = true)
@Composable
private fun MiniPlayerBarMobilePreview() {
    MoisesTheme {
        MiniPlayerBar(
            track = previewTrack,
            isPlaying = false,
            progress = 0.4f,
            onBarClick = {},
            onPlayPauseClick = {},
            onSkipPrevious = {},
            onSkipNext = {},
            style = MiniPlayerBarStyle.Mobile
        )
    }
}

@Preview(showBackground = true, widthDp = 800)
@Composable
private fun MiniPlayerBarAutoPreview() {
    MoisesTheme {
        MiniPlayerBar(
            track = previewTrack,
            isPlaying = false,
            progress = 0.4f,
            onBarClick = {},
            onPlayPauseClick = {},
            onSkipPrevious = {},
            onSkipNext = {},
            style = MiniPlayerBarStyle.Auto
        )
    }
}
