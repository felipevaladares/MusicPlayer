package com.felpster.musicplayer.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {

    @GET("search")
    suspend fun getSongs(
        @Query(value="term") search: String,
        @Query(value = "entity") entity: String = "song",
    ): SearchResponse

    @GET("lookup")
    suspend fun getAlbumSongs(
        @Query(value="id") albumId: Long,
        @Query(value = "entity") entity: String = "song",
    ): AlbumResponse

    @GET("lookup")
    suspend fun getSong(
        @Query(value="id") songId: Long,
        @Query(value = "entity") entity: String = "song",
    ): SongResponse
}