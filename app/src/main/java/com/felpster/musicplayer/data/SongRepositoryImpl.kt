package com.felpster.musicplayer.data

import com.felpster.musicplayer.data.local.SongDao
import com.felpster.musicplayer.data.local.toDomain
import com.felpster.musicplayer.data.local.toEntity
import com.felpster.musicplayer.data.remote.ItunesApi
import com.felpster.musicplayer.data.remote.toDomain
import com.felpster.musicplayer.domain.SongRepository
import com.felpster.musicplayer.domain.model.Album
import com.felpster.musicplayer.domain.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(
    private val api: ItunesApi,
    private val songDao: SongDao
): SongRepository {

    override fun searchSongs(search: String): Flow<List<Song>> = flow {
        // 1. Emit existing cached songs matching the query
        val localSongs = songDao.searchSongs(search).first().map { it.toDomain() }
        emit(localSongs)

        // 2. Fetch fresh songs from network
        val response = api.getSongs(search)
        val networkSongs = response.results.map { it.toDomain() }

        // 3. Emit combined list of songs, ensuring no duplicates
        val combined = (localSongs + networkSongs).distinctBy { it.id }
        emit(combined)

        // 4. Save to database
        songDao.insertSongs(networkSongs.map { it.toEntity() })
    }

    override fun getAlbumWithSongs(albumId: Long) =
        flow {
            val response = api.getAlbumSongs(albumId)
            val album = Album(
                id = response.album.collectionId,
                name = response.album.collectionName,
                artUrl = response.album.artworkUrl100,
                artistId = response.album.artistId,
                artistName = response.album.artistName,
                songs = response.results.map { it.toDomain() }
            )
            emit(album)
        }

    override fun getSong(id: Long): Flow<Song> =
        songDao.getSong(id).map { entity ->
            if (entity != null) {
                entity.toDomain()
            } else {
                // Fetch from network if not in DB
                val result = api.getSong(id).results.firstOrNull() ?: throw IllegalArgumentException("Song with ID $id not found")
                val song = result.toDomain()
                songDao.insertSongs(listOf(song.toEntity()))
                song
            }
        }

    override fun getRecentlyPlayed(): Flow<List<Song>> {
        return songDao.getRecentlyPlayed().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun markSongAsPlayed(id: Long) {
        songDao.updateLastPlayed(id, System.currentTimeMillis())
    }
}
