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

    override fun getRecentlyPlayed(): Flow<List<Song>> {
        TODO("Not yet implemented")
    }

    override suspend fun markSongAsPlayed(id: Long) {
        TODO("Not yet implemented")
    }

    companion object {
        val songsList = listOf(
            Song(1L, "Song 1", "Artist 1", 10L, "url1"),
            Song(2L, "Song 2", "Artist 2", 20L, "url2"),
            Song(3L, "Song 3", "Artist 3", 10L, "url3"),
            Song(4L, "Song 4", "Artist 4", 10L, "url4"),
            Song(5L, "Song 5", "Artist 5", 10L, "url5"),
            Song(6L, "Song 6", "Artist 6", 10L, "url6"),
            Song(7L, "Song 7", "Artist 7", 10L, "url7"),
            Song(8L, "Song 8", "Artist 8", 10L, "url8"),
            Song(9L, "Song 9", "Artist 9", 10L, "url9"),
            Song(10L, "Song 10", "Artist 10", 10L, "url10"),
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
