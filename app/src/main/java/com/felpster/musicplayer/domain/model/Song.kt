package com.felpster.musicplayer.domain.model

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val albumId: String,
    val albumArtUrl: String,
    val duration: Int = 0,
)
