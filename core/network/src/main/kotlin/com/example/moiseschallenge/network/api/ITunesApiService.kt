package com.example.moiseschallenge.network.api

import com.example.moiseschallenge.network.model.ITunesSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {

    @GET("search")
    suspend fun search(
        @Query("term") term: String,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "song",
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0,
        @Query("country") country: String = "US"
    ): ITunesSearchResponse

    @GET("lookup")
    suspend fun lookup(
        @Query("id") id: Long,
        @Query("entity") entity: String? = null
    ): ITunesSearchResponse

    @GET("lookup")
    suspend fun lookupAlbumTracks(
        @Query("id") albumId: Long,
        @Query("entity") entity: String = "song"
    ): ITunesSearchResponse

    companion object {
        const val BASE_URL = "https://itunes.apple.com/"
    }
}
