package com.example.moiseschallenge.shared.album

import com.example.moiseschallenge.domain.model.Track

interface AlbumScreenContract {
    interface ActionHandler {
        fun onTrackClick(track: Track)
        fun retry()
    }

    class NoOp : ActionHandler {
        override fun onTrackClick(track: Track) = Unit
        override fun retry() = Unit
    }
}
