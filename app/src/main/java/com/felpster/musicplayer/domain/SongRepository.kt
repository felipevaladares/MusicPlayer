package com.felpster.musicplayer.domain

import com.felpster.musicplayer.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    fun searchSongs(search: String): Flow<List<Song>>

    fun getAlbumSongs(albumId: Long): Flow<List<Song>>
}