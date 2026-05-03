package com.felpster.musicplayer.domain

import com.felpster.musicplayer.domain.model.Album
import com.felpster.musicplayer.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    fun searchSongs(search: String): Flow<List<Song>>

    fun getAlbumWithSongs(albumId: Long): Flow<Album>

    fun getSong(id: Long): Flow<Song>
}