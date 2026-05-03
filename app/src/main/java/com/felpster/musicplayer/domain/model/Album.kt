package com.felpster.musicplayer.domain.model

data class Album(
    val id: Long,
    val name: String,
    val artUrl: String,
    val artistId: Long,
    val artistName: String,
    val songs: List<Song>,
)
