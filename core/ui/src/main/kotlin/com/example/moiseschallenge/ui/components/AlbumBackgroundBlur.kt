package com.example.moiseschallenge.ui.components

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.moiseschallenge.resources.R

@Composable
fun AlbumBackgroundBlur(
    artworkUrl: String?,
    modifier: Modifier = Modifier,
    blurRadius: Dp = 100.dp,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (artworkUrl != null) {
            AsyncImage(
                model = artworkUrl,
                contentDescription = stringResource(id = R.string.cd_album_artwork),
                modifier = Modifier
                    .fillMaxSize()
                    .blur(blurRadius),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Composable
fun MobileAlbumBackgroundBlurCompat(
    artworkUrl: String?,
    modifier: Modifier = Modifier,
    blurRadius: Dp = 100.dp,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && artworkUrl.orEmpty()
            .takeIf { it.isNotBlank() } != null
    ) {
        AlbumBackgroundBlur(
            artworkUrl = artworkUrl,
            blurRadius = blurRadius,
            modifier = modifier.fillMaxWidth(),
        )
    } else {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        )
    }
}

@Composable
fun AutoAlbumBackgroundBlurCompat(
    artworkUrl: String?,
    modifier: Modifier = Modifier,
    blurRadius: Dp = 100.dp,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && artworkUrl.orEmpty()
            .takeIf { it.isNotBlank() } != null
    ) {
        AlbumBackgroundBlur(
            artworkUrl = artworkUrl,
            blurRadius = blurRadius,
            modifier = modifier.fillMaxWidth(),
        )
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background.copy(alpha = 0.2f)))
    } else {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        )
    }
}
