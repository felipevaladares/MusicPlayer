package com.felpster.musicplayer.data.remote

data class AlbumResponse(
    val resultCount: Int,
    val results: List<RemoteSong>,
    val album: RemoteAlbum,
)
