package com.felpster.musicplayer.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    @field:Json(name = "resultCount")
    val resultCount: Int,

    @field:Json(name = "results")
    val results: List<RemoteSong>
)
