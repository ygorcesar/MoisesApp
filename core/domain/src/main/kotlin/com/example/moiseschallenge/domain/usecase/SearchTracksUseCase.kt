package com.example.moiseschallenge.domain.usecase

import androidx.paging.PagingData
import com.example.moiseschallenge.domain.model.Track
import com.example.moiseschallenge.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchTracksUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {
    operator fun invoke(query: String): Flow<PagingData<Track>> {
        return musicRepository.searchTracks(query)
    }
}
