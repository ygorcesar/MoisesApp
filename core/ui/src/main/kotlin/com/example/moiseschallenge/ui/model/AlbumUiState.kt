package com.example.moiseschallenge.ui.model

import androidx.compose.runtime.Immutable
import com.example.moiseschallenge.domain.model.Album
import com.example.moiseschallenge.domain.model.Track
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class AlbumUiState(
    val album: Album? = null,
    val tracks: ImmutableList<Track> = persistentListOf(),
    val isLoading: Boolean = true,
    val error: String? = null
)
