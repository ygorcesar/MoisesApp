package com.example.moiseschallenge.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.moiseschallenge.ui.theme.MoisesTheme

@Composable
fun PlayingIndicator(
    modifier: Modifier = Modifier,
    isPlaying: Boolean = true,
    barColor: Color = MaterialTheme.colorScheme.onSurface,
    barCount: Int = 4,
    barWidth: Dp = 3.dp,
    barSpacing: Dp = 2.dp,
    barCornerRadius: Dp = 1.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "playing_indicator")

    val bar1Height by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar1"
    )

    val bar2Height by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 300, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar2"
    )

    val bar3Height by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar3"
    )

    val bar4Height by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 350, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar4"
    )

    val barHeights = if (isPlaying) {
        listOf(bar1Height, bar2Height, bar3Height, bar4Height).take(barCount)
    } else {
        listOf(0.5f, 0.3f, 0.6f, 0.4f).take(barCount)
    }

    Canvas(modifier = modifier) {
        val totalWidth = (barWidth.toPx() * barCount) + (barSpacing.toPx() * (barCount - 1))
        val startX = (size.width - totalWidth) / 2
        val maxBarHeight = size.height * 0.8f
        val cornerRadiusPx = barCornerRadius.toPx()

        barHeights.forEachIndexed { index, heightFraction ->
            val barHeight = maxBarHeight * heightFraction
            val x = startX + (index * (barWidth.toPx() + barSpacing.toPx()))
            val y = (size.height - barHeight) / 2

            drawRoundRect(
                color = barColor,
                topLeft = Offset(x, y),
                size = Size(barWidth.toPx(), barHeight),
                cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayingIndicatorPreview() {
    MoisesTheme {
        PlayingIndicator(
            modifier = Modifier.size(24.dp),
            isPlaying = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayingIndicatorPausedPreview() {
    MoisesTheme {
        PlayingIndicator(
            modifier = Modifier.size(24.dp),
            isPlaying = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayingIndicatorLargePreview() {
    MoisesTheme {
        PlayingIndicator(
            modifier = Modifier.size(48.dp),
            isPlaying = true,
            barWidth = 6.dp,
            barSpacing = 4.dp
        )
    }
}
