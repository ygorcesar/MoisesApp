package com.example.moiseschallenge.automotive.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.moiseschallenge.automotive.ui.theme.AutomotiveTheme
import com.example.moiseschallenge.resources.R
import com.example.moiseschallenge.ui.components.SplashBackground
import com.example.moiseschallenge.ui.util.PreviewAuto
import kotlinx.coroutines.delay

@Composable
fun AutoSplashScreen(
    onNavigateToBrowse: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(2000)
        onNavigateToBrowse()
    }

    SplashBackground {
        Image(
            painter = painterResource(id = R.drawable.ic_music_note),
            contentDescription = stringResource(id = R.string.app_name),
            modifier = Modifier.size(200.dp)
        )
    }
}

@PreviewAuto
@Composable
private fun AutoSplashScreenPreview() {
    AutomotiveTheme {
        SplashBackground {
            Image(
                painter = painterResource(id = R.drawable.ic_music_note),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
        }
    }
}
