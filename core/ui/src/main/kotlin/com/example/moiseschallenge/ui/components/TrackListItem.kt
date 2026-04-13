package com.example.moiseschallenge.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.moiseschallenge.domain.model.Track
import com.example.moiseschallenge.resources.R
import com.example.moiseschallenge.ui.theme.MoisesTheme
import com.example.moiseschallenge.ui.util.PreviewDataMocks

enum class TrackListItemStyle(
    val artworkSize: Dp,
    val artworkCornerRadius: Dp,
    val artworkPlaceholderSize: Dp,
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val contentSpacing: Dp,
    val playingIndicatorSize: Dp,
    val playingIndicatorBarWidth: Dp,
    val playingIndicatorBarSpacing: Dp,
    val showMoreButton: Boolean
) {
    Mobile(
        artworkSize = 56.dp,
        artworkCornerRadius = 8.dp,
        artworkPlaceholderSize = 24.dp,
        horizontalPadding = 16.dp,
        verticalPadding = 12.dp,
        contentSpacing = 12.dp,
        playingIndicatorSize = 24.dp,
        playingIndicatorBarWidth = 3.dp,
        playingIndicatorBarSpacing = 2.dp,
        showMoreButton = true
    ),
    Auto(
        artworkSize = 72.dp,
        artworkCornerRadius = 8.dp,
        artworkPlaceholderSize = 32.dp,
        horizontalPadding = 24.dp,
        verticalPadding = 16.dp,
        contentSpacing = 20.dp,
        playingIndicatorSize = 32.dp,
        playingIndicatorBarWidth = 4.dp,
        playingIndicatorBarSpacing = 3.dp,
        showMoreButton = false
    )
}

@Composable
fun TrackListItem(
    track: Track,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onMoreClick: (() -> Unit)? = null,
    isCurrentTrack: Boolean = false,
    isPlaying: Boolean = false,
    style: TrackListItemStyle = TrackListItemStyle.Mobile
) {
    val titleStyle: TextStyle = when (style) {
        TrackListItemStyle.Mobile -> MaterialTheme.typography.bodyLarge
        TrackListItemStyle.Auto -> MaterialTheme.typography.titleMedium
    }
    val subtitleStyle: TextStyle = when (style) {
        TrackListItemStyle.Mobile -> MaterialTheme.typography.bodyMedium
        TrackListItemStyle.Auto -> MaterialTheme.typography.bodyMedium
    }

    // Background for Auto style items
    val itemBackground = when (style) {
        TrackListItemStyle.Auto -> {
            if (isCurrentTrack) Color.White.copy(alpha = 0.6f)
            else Color.White.copy(alpha = 0.1f)
        }

        TrackListItemStyle.Mobile -> Color.Transparent
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (style == TrackListItemStyle.Auto) {
                    Modifier
                        .padding(horizontal = style.horizontalPadding, vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(itemBackground)
                        .clickable(onClick = onClick)
                        .padding(horizontal = 16.dp, vertical = style.verticalPadding)
                } else {
                    Modifier
                        .clickable(onClick = onClick)
                        .padding(
                            horizontal = style.horizontalPadding,
                            vertical = style.verticalPadding
                        )
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(style.artworkSize)
                .clip(RoundedCornerShape(style.artworkCornerRadius))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (track.artworkUrl100 != null) {
                AsyncImage(
                    model = track.artworkUrl100,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_playing_animation),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(style.artworkPlaceholderSize)
                )
            }
        }

        Spacer(modifier = Modifier.width(style.contentSpacing))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.trackName,
                style = titleStyle.copy(fontWeight = FontWeight.Medium),
                color = when {
                    style == TrackListItemStyle.Auto && isCurrentTrack -> Color.White
                    isCurrentTrack -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.onSurface
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = track.artistName,
                style = subtitleStyle,
                color = if (style == TrackListItemStyle.Auto && isCurrentTrack) {
                    Color.White
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (isCurrentTrack && isPlaying) {
            PlayingIndicator(
                modifier = Modifier.size(style.playingIndicatorSize),
                isPlaying = true,
                barColor = if (style == TrackListItemStyle.Auto) Color.White else MaterialTheme.colorScheme.onSurface,
                barWidth = style.playingIndicatorBarWidth,
                barSpacing = style.playingIndicatorBarSpacing
            )
        }

        if (style.showMoreButton && onMoreClick != null) {
            IconButton(onClick = onMoreClick) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(id = R.string.nav_more_options),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview
@Composable
private fun TrackListItemMobilePreview() {
    MoisesTheme {
        TrackListItem(
            track = PreviewDataMocks.previewTrack,
            onClick = {},
            onMoreClick = {},
            style = TrackListItemStyle.Mobile
        )
    }
}

@Preview
@Composable
private fun TrackListItemMobilePlayingPreview() {
    MoisesTheme {
        TrackListItem(
            track = PreviewDataMocks.previewTrack,
            onClick = {},
            onMoreClick = {},
            isCurrentTrack = true,
            isPlaying = true,
            style = TrackListItemStyle.Mobile
        )
    }
}

@Preview(widthDp = 800)
@Composable
private fun TrackListItemAutoPreview() {
    MoisesTheme {
        TrackListItem(
            track = PreviewDataMocks.previewTrack,
            onClick = {},
            style = TrackListItemStyle.Auto
        )
    }
}

@Preview(widthDp = 800)
@Composable
private fun TrackListItemAutoPlayingPreview() {
    MoisesTheme {
        TrackListItem(
            track = PreviewDataMocks.previewTrack,
            onClick = {},
            isCurrentTrack = true,
            isPlaying = true,
            style = TrackListItemStyle.Auto
        )
    }
}
