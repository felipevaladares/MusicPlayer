package com.felpster.musicplayer.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteAlbum(
    @field:Json(name = "wrapperType")
    val wrapperType: String,

    @field:Json(name = "collectionType")
    val collectionType: String,

    @field:Json(name = "artistId")
    val artistId: Long,

    @field:Json(name = "collectionId")
    val collectionId: Long,

    @field:Json(name = "amgArtistId")
    val amgArtistId: Long? = null,

    @field:Json(name = "artistName")
    val artistName: String,

    @field:Json(name = "collectionName")
    val collectionName: String,

    @field:Json(name = "collectionCensoredName")
    val collectionCensoredName: String,

    @field:Json(name = "artistViewUrl")
    val artistViewUrl: String,

    @field:Json(name = "collectionViewUrl")
    val collectionViewUrl: String,

    @field:Json(name = "artworkUrl60")
    val artworkUrl60: String,

    @field:Json(name = "artworkUrl100")
    val artworkUrl100: String,

    @field:Json(name = "collectionPrice")
    val collectionPrice: Double? = null,

    @field:Json(name = "collectionExplicitness")
    val collectionExplicitness: String,

    @field:Json(name = "trackCount")
    val trackCount: Int,

    @field:Json(name = "copyright")
    val copyright: String,

    @field:Json(name = "country")
    val country: String,

    @field:Json(name = "currency")
    val currency: String,

    @field:Json(name = "releaseDate")
    val releaseDate: String,

    @field:Json(name = "primaryGenreName")
    val primaryGenreName: String
)
