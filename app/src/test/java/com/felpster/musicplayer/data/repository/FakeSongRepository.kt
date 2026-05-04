package com.felpster.musicplayer.data.repository

import com.felpster.musicplayer.domain.SongRepository
import com.felpster.musicplayer.domain.model.Album
import com.felpster.musicplayer.domain.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf

class FakeSongRepository : SongRepository {

    private val songsFlow = MutableSharedFlow<List<Song>>()

    private var error: Throwable? = null

    fun setError(throwable: Throwable?) {
        error = throwable
    }

    suspend fun emitSongs(songs: List<Song>) {
        songsFlow.emit(songs)
    }

    override fun searchSongs(search: String): Flow<List<Song>> = songsFlow

    override fun getAlbumWithSongs(albumId: Long): Flow<Album> {
        return flowOf(
            Album(
                id = albumId,
                name = "Fake Album",
                artUrl = "",
                artistId = 1L,
                artistName = "Fake Artist",
                songs = emptyList()
            )
        )
    }

    override fun getSong(id: Long): Flow<Song> {
        return flowOf(
            Song(
                id = id,
                title = "Fake Song",
                artist = "Fake Artist",
                albumId = 1L,
                albumArtUrl = ""
            )
        )
    }

    override fun searchSongs(search: String): Flow<List<Song>> = flow {
        error?.let { throw it }
        songsFlow.collect { emit(it) }
    }

    override fun getAlbumWithSongs(albumId: Long): Flow<Album> = flow {
        error?.let { throw it }
    }

    override fun getSong(id: Long): Flow<Song> = flow {
        error?.let { throw it }
    }

    companion object {
        val songsList = listOf(
            Song(1L, "Song 1", "Artist 1", 10L, "url1"),
            Song(2L, "Song 2", "Artist 2", 20L, "url2")
        )
    }
}
