package com.felpster.musicplayer.domain.model

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val albumId: Long,
    val albumArtUrl: String,
    val durationMillis: Long = 0,
)
