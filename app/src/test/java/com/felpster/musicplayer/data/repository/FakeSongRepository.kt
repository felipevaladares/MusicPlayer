package com.felpster.musicplayer.data.repository

import com.felpster.musicplayer.domain.SongRepository
import com.felpster.musicplayer.domain.model.Album
import com.felpster.musicplayer.domain.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow

class FakeSongRepository : SongRepository {

    private val songsFlow = MutableSharedFlow<List<Song>>()
    private val albumFlow = MutableSharedFlow<Album>()
    private val songFlow = MutableSharedFlow<Song>()

    private var error: Throwable? = null

    fun setError(throwable: Throwable?) {
        error = throwable
    }

    suspend fun emitSongs(songs: List<Song>) {
        songsFlow.emit(songs)
    }

    suspend fun emitAlbum(album: Album) {
        albumFlow.emit(album)
    }

    suspend fun emitSong(song: Song) {
        songFlow.emit(song)
    }

    override fun searchSongs(search: String): Flow<List<Song>> = flow {
        error?.let { throw it }
        songsFlow.collect { emit(it) }
    }

    override fun getAlbumWithSongs(albumId: Long): Flow<Album> = flow {
        error?.let { throw it }
        albumFlow.collect { emit(it) }
    }

    override fun getSong(id: Long): Flow<Song> = flow {
        error?.let { throw it }
        songFlow.collect { emit(it) }
    }

    companion object {
        val songsList = listOf(
            Song(1L, "Song 1", "Artist 1", 10L, "url1"),
            Song(2L, "Song 2", "Artist 2", 20L, "url2")
        )

        val album = Album(
            id = 10L,
            name = "Album 1",
            artUrl = "url1",
            artistId = 1L,
            artistName = "Artist 1",
            songs = songsList
        )
    }
}
