package com.felpster.musicplayer.data

import com.felpster.musicplayer.data.remote.ItunesApi
import com.felpster.musicplayer.domain.SongRepository
import com.felpster.musicplayer.domain.model.Song
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(
    private val api: ItunesApi
): SongRepository {
    override suspend fun searchSongs(search: String): List<Song> {
        TODO("Not yet implemented")
    }

    override suspend fun getAlbumSongs(albumId: Long): List<Song> {
        TODO("Not yet implemented")
    }
}