package com.felpster.musicplayer.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteSong(
    @field:Json(name = "wrapperType")
    val wrapperType: String,

    @field:Json(name = "kind")
    val kind: String,

    @field:Json(name = "artistId")
    val artistId: Long,

    @field:Json(name = "collectionId")
    val collectionId: Long,

    @field:Json(name = "trackId")
    val trackId: Long,

    @field:Json(name = "artistName")
    val artistName: String,

    @field:Json(name = "collectionName")
    val collectionName: String,

    @field:Json(name = "trackName")
    val trackName: String,

    @field:Json(name = "collectionCensoredName")
    val collectionCensoredName: String,

    @field:Json(name = "trackCensoredName")
    val trackCensoredName: String,

    @field:Json(name = "artistViewUrl")
    val artistViewUrl: String,

    @field:Json(name = "collectionViewUrl")
    val collectionViewUrl: String,

    @field:Json(name = "trackViewUrl")
    val trackViewUrl: String,

    @field:Json(name = "previewUrl")
    val previewUrl: String? = null,

    @field:Json(name = "artworkUrl30")
    val artworkUrl30: String,

    @field:Json(name = "artworkUrl60")
    val artworkUrl60: String,

    @field:Json(name = "artworkUrl100")
    val artworkUrl100: String,

    @field:Json(name = "collectionPrice")
    val collectionPrice: Double? = null,

    @field:Json(name = "trackPrice")
    val trackPrice: Double? = null,

    @field:Json(name = "releaseDate")
    val releaseDate: String,

    @field:Json(name = "collectionExplicitness")
    val collectionExplicitness: String,

    @field:Json(name = "trackExplicitness")
    val trackExplicitness: String,

    @field:Json(name = "discCount")
    val discCount: Int,

    @field:Json(name = "discNumber")
    val discNumber: Int,

    @field:Json(name = "trackCount")
    val trackCount: Int,

    @field:Json(name = "trackNumber")
    val trackNumber: Int,

    @field:Json(name = "trackTimeMillis")
    val trackTimeMillis: Int,

    @field:Json(name = "country")
    val country: String,

    @field:Json(name = "currency")
    val currency: String,

    @field:Json(name = "primaryGenreName")
    val primaryGenreName: String,

    @field:Json(name = "isStreamable")
    val isStreamable: Boolean? = null
)
