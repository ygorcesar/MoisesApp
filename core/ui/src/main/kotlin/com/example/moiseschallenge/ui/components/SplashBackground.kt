package com.example.moiseschallenge.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import com.example.moiseschallenge.ui.theme.MoisesTheme
import com.example.moiseschallenge.ui.theme.SplashGradientColorStops

@Composable
fun SplashBackground(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colorStops = SplashGradientColorStops,
                    start = Offset(0f, Float.POSITIVE_INFINITY),
                    end = Offset(Float.POSITIVE_INFINITY, 0f)
                )
            ),
        contentAlignment = contentAlignment,
        content = content
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun SplashBackgroundPreview() {
    MoisesTheme {
        SplashBackground {}
    }
}
