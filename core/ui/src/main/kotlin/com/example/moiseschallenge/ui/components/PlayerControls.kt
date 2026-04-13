package com.example.moiseschallenge.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.moiseschallenge.domain.model.RepeatMode
import com.example.moiseschallenge.resources.R
import com.example.moiseschallenge.ui.theme.MoisesTheme

@Composable
fun PlayPauseButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonSize: Dp = 56.dp,
    iconSize: Dp = 28.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    iconTint: Color = MaterialTheme.colorScheme.onSurface
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(buttonSize)
            .background(backgroundColor, CircleShape)
    ) {
        Icon(
            painter = painterResource(
                id = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
            ),
            contentDescription = stringResource(
                id = if (isPlaying) R.string.action_pause else R.string.action_play
            ),
            modifier = Modifier.size(iconSize),
            tint = iconTint
        )
    }
}

@Composable
fun SkipButton(
    isNext: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = 32.dp,
    tint: Color = MaterialTheme.colorScheme.onBackground
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(
                id = if (isNext) R.drawable.ic_skip_next else R.drawable.ic_skip_previous
            ),
            contentDescription = stringResource(
                id = if (isNext) R.string.action_next else R.string.action_previous
            ),
            modifier = Modifier.size(iconSize),
            tint = tint
        )
    }
}

@Composable
fun RepeatButton(
    repeatMode: RepeatMode,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = 24.dp,
    enabledTint: Color = MaterialTheme.colorScheme.primary,
    disabledTint: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(
                id = when (repeatMode) {
                    RepeatMode.ONE -> R.drawable.ic_repeat_one
                    else -> R.drawable.ic_repeat
                }
            ),
            contentDescription = stringResource(id = R.string.action_repeat),
            modifier = Modifier.size(iconSize),
            tint = if (repeatMode != RepeatMode.OFF) enabledTint else disabledTint
        )
    }
}

@Composable
fun SkipControls(
    onSkipPrevious: () -> Unit,
    onSkipNext: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = 32.dp,
    spacing: Dp = 16.dp,
    tint: Color = MaterialTheme.colorScheme.onBackground
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SkipButton(
            isNext = false,
            onClick = onSkipPrevious,
            iconSize = iconSize,
            tint = tint
        )
        Spacer(modifier = Modifier.width(spacing))
        SkipButton(
            isNext = true,
            onClick = onSkipNext,
            iconSize = iconSize,
            tint = tint
        )
    }
}

@Preview
@Composable
private fun PlayPauseButtonPlayingPreview() {
    MoisesTheme {
        PlayPauseButton(
            isPlaying = true,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun PlayPauseButtonPausedPreview() {
    MoisesTheme {
        PlayPauseButton(
            isPlaying = false,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun SkipButtonNextPreview() {
    MoisesTheme {
        SkipButton(
            isNext = true,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun SkipButtonPreviousPreview() {
    MoisesTheme {
        SkipButton(
            isNext = false,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun SkipControlsPreview() {
    MoisesTheme {
        SkipControls(
            onSkipPrevious = {},
            onSkipNext = {}
        )
    }
}

@Preview
@Composable
private fun RepeatButtonOffPreview() {
    MoisesTheme {
        RepeatButton(
            repeatMode = RepeatMode.OFF,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun RepeatButtonAllPreview() {
    MoisesTheme {
        RepeatButton(
            repeatMode = RepeatMode.ALL,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun RepeatButtonOnePreview() {
    MoisesTheme {
        RepeatButton(
            repeatMode = RepeatMode.ONE,
            onClick = {}
        )
    }
}
