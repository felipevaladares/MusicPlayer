package com.felpster.musicplayer.domain

import com.felpster.musicplayer.domain.model.Song

interface SongRepository {
    suspend fun searchSongs(search: String): List<Song>

    suspend fun getAlbumSongs(albumId: Long): List<Song>
}