package com.felpster.musicplayer.data.remote

data class SongResponse(
    val resultCount: Int,
    val results: List<RemoteSong>,
)
