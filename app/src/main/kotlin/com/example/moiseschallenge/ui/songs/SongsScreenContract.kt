package com.example.moiseschallenge.ui.songs

import com.example.moiseschallenge.domain.model.Track

internal interface SongsScreenContract {
    interface ActionHandler {
        fun onSearchQueryChange(query: String)
        fun onTrackClick(track: Track, allTracks: List<Track>)
        fun onPause()
        fun onResume()
        fun onSkipPrevious()
        fun onSkipNext()
    }

    // Should be used only in Composable Previews
    class NoOp : ActionHandler {
        override fun onSearchQueryChange(query: String) = Unit
        override fun onTrackClick(track: Track, allTracks: List<Track>) = Unit
        override fun onPause() = Unit
        override fun onResume() = Unit
        override fun onSkipPrevious() = Unit
        override fun onSkipNext() = Unit
    }
}
