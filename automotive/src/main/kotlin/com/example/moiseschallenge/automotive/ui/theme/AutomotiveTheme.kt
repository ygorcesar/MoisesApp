package com.example.moiseschallenge.automotive.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import com.example.moiseschallenge.ui.theme.MoisesAccent
import com.example.moiseschallenge.ui.theme.MoisesBackground
import com.example.moiseschallenge.ui.theme.MoisesError
import com.example.moiseschallenge.ui.theme.MoisesOnBackground
import com.example.moiseschallenge.ui.theme.MoisesOnSurface
import com.example.moiseschallenge.ui.theme.MoisesOnSurfaceVariant
import com.example.moiseschallenge.ui.theme.MoisesPrimary
import com.example.moiseschallenge.ui.theme.MoisesSecondary
import com.example.moiseschallenge.ui.theme.MoisesSurface
import com.example.moiseschallenge.ui.theme.MoisesSurfaceVariant

private val AutomotiveDarkColorScheme = darkColorScheme(
    primary = MoisesPrimary,
    secondary = MoisesSecondary,
    background = MoisesBackground,
    surface = MoisesSurface,
    surfaceVariant = MoisesSurfaceVariant,
    onPrimary = MoisesBackground,
    onSecondary = MoisesOnBackground,
    onBackground = MoisesOnBackground,
    onSurface = MoisesOnSurface,
    onSurfaceVariant = MoisesOnSurfaceVariant,
    error = MoisesError
)

@Composable
fun AutomotiveTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AutomotiveDarkColorScheme,
        content = content
    )
}
