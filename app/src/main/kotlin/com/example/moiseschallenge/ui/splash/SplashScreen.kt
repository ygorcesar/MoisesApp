package com.example.moiseschallenge.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moiseschallenge.resources.R
import com.example.moiseschallenge.ui.components.SplashBackground
import com.example.moiseschallenge.ui.theme.MoisesTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToSongs: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(1500)
        onNavigateToSongs()
    }

    SplashBackground {
        Image(
            painter = painterResource(id = R.drawable.ic_music_note),
            contentDescription = stringResource(id = R.string.app_name),
            modifier = Modifier.size(160.dp)
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    MoisesTheme {
        SplashBackground {
            Image(
                painter = painterResource(id = R.drawable.ic_music_note),
                contentDescription = null,
                modifier = Modifier.size(160.dp)
            )
        }
    }
}
