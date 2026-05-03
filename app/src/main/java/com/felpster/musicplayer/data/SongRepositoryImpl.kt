package com.felpster.musicplayer.data

import com.felpster.musicplayer.data.remote.ItunesApi
import com.felpster.musicplayer.domain.SongRepository
import com.felpster.musicplayer.domain.model.Album
import com.felpster.musicplayer.domain.model.Song
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(
    private val api: ItunesApi
): SongRepository {

    override fun searchSongs(search: String) =
        flow {
            emit(
                api.getSongs(search).results.map { result ->
                    Song(
                        id = result.trackId,
                        title = result.trackName,
                        artist = result.artistName,
                        albumId = result.collectionId,
                        albumArtUrl = result.artworkUrl100,
                        durationMillis = result.trackTimeMillis
                    )
                }
            )
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
                songs = response.results.map { result ->
                    Song(
                        id = result.trackId,
                        title = result.trackName,
                        artist = result.artistName,
                        albumId = result.collectionId,
                        albumArtUrl = result.artworkUrl100,
                        durationMillis = result.trackTimeMillis
                    )
                }
            )
            emit(album)
        }

    override fun getSong(id: Long) =
        flow {
            val result = api.getSong(id).results.firstOrNull()
                ?: throw IllegalArgumentException("Song with ID $id not found")
            emit(
                Song(
                    id = result.trackId,
                    title = result.trackName,
                    artist = result.artistName,
                    albumId = result.collectionId,
                    albumArtUrl = result.artworkUrl100,
                    durationMillis = result.trackTimeMillis
                )
            )
        }
}