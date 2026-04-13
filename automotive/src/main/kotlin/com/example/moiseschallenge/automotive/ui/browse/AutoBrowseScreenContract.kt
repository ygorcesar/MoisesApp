package com.example.moiseschallenge.automotive.ui.browse

import com.example.moiseschallenge.domain.model.Track

internal interface AutoBrowseScreenContract {
    interface ActionHandler {
        fun onTrackClick(track: Track)
        fun onSkipNext()
        fun onSkipPrevious()
        fun onPause()
        fun onResume()
    }

    // Should be used only in Composable Previews
    class NoOp : ActionHandler {
        override fun onTrackClick(track: Track) = Unit
        override fun onSkipNext() = Unit
        override fun onSkipPrevious() = Unit
        override fun onPause() = Unit
        override fun onResume() = Unit
    }
}
