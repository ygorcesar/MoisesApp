package com.example.moiseschallenge.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.moiseschallenge.ui.theme.MoisesTheme
import com.example.moiseschallenge.ui.theme.PlayerProgressIndicator
import com.example.moiseschallenge.ui.theme.PlayerProgressTrack
import com.example.moiseschallenge.ui.util.formatTime

enum class ProgressSliderStyle(
    val thumbSize: Dp,
    val trackHeight: Dp,
    val showThumb: Boolean,
    val showTimeLabels: Boolean
) {
    Mobile(
        thumbSize = 12.dp,
        trackHeight = 4.dp,
        showThumb = true,
        showTimeLabels = true
    ),
    Auto(
        thumbSize = 24.dp,
        trackHeight = 10.dp,
        showThumb = true,
        showTimeLabels = false
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressSlider(
    progress: Float,
    currentPosition: Long,
    duration: Long,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
    style: ProgressSliderStyle = ProgressSliderStyle.Mobile,
    activeTrackColor: Color = PlayerProgressIndicator,
    inactiveTrackColor: Color = PlayerProgressTrack,
    showRemainingTime: Boolean = true
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Slider(
            value = progress,
            onValueChange = onSeek,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = if (style.showThumb) activeTrackColor else Color.Transparent,
                activeTrackColor = activeTrackColor,
                inactiveTrackColor = inactiveTrackColor
            ),
            thumb = {
                if (style.showThumb) {
                    Box(
                        modifier = Modifier
                            .size(style.thumbSize)
                            .clip(CircleShape)
                            .background(activeTrackColor)
                    )
                }
            },
            track = { _ ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(style.trackHeight)
                        .clip(CircleShape)
                        .background(inactiveTrackColor)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress.coerceIn(0f, 1f))
                            .height(style.trackHeight)
                            .clip(CircleShape)
                            .background(activeTrackColor)
                    )
                }
            }
        )

        if (style.showTimeLabels) {
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatTime(currentPosition),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (showRemainingTime) {
                        "-${formatTime(duration - currentPosition)}"
                    } else {
                        formatTime(duration)
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun AutoProgressBar(
    progress: Float,
    currentPosition: Long,
    duration: Long,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
    activeTrackColor: Color = PlayerProgressIndicator,
    inactiveTrackColor: Color = PlayerProgressTrack,
) {
    ProgressSlider(
        progress = progress,
        currentPosition = currentPosition,
        duration = duration,
        onSeek = onSeek,
        modifier = modifier,
        style = ProgressSliderStyle.Auto,
        activeTrackColor = activeTrackColor,
        inactiveTrackColor = inactiveTrackColor,
    )
}

@Preview
@Composable
private fun ProgressSliderMobilePreview() {
    MoisesTheme {
        ProgressSlider(
            progress = 0.4f,
            currentPosition = 1500,
            duration = 3000,
            onSeek = {},
            style = ProgressSliderStyle.Mobile
        )
    }
}

@Preview(widthDp = 800)
@Composable
private fun ProgressSliderAutoPreview() {
    MoisesTheme {
        ProgressSlider(
            progress = 0.4f,
            currentPosition = 1500,
            duration = 3000,
            onSeek = {},
            style = ProgressSliderStyle.Auto
        )
    }
}
