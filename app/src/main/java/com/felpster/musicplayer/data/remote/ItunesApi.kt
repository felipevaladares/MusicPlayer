package com.felpster.musicplayer.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {

    @GET("search")
    suspend fun getSongs(
        @Query(value="term", encoded=true) search: String,
        @Query(value = "entity") entity: String = "song",
    ): SearchResponse

    @GET("lookup")
    suspend fun getAlbumSongs(
        @Query(value="upc", encoded=true) albumId: Long,
        @Query(value = "entity") entity: String = "song",
    ): SearchResponse
}