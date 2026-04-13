package com.example.moiseschallenge.shared.player

interface PlayerScreenContract {
    interface ActionHandler {
        fun onPlayPauseClick()
        fun onSkipNext()
        fun onSkipPrevious()
        fun onSeekForward()
        fun onSeekBackward()
        fun onSeekTo(progress: Float)
        fun onToggleRepeat()
    }

    class NoOp : ActionHandler {
        override fun onPlayPauseClick() = Unit
        override fun onSkipNext() = Unit
        override fun onSkipPrevious() = Unit
        override fun onSeekForward() = Unit
        override fun onSeekBackward() = Unit
        override fun onSeekTo(progress: Float) = Unit
        override fun onToggleRepeat() = Unit
    }
}
