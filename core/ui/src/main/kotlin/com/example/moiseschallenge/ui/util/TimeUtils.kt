package com.example.moiseschallenge.ui.util

/**
 * Formats milliseconds to a time string in the format "m:ss"
 */
fun formatTime(millis: Long): String {
    if (millis < 0) return "0:00"
    val minutes = millis / 1000 / 60
    val seconds = (millis / 1000) % 60
    return String.format("%d:%02d", minutes, seconds)
}
