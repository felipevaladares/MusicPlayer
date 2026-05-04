package com.felpster.musicplayer.data

import app.cash.turbine.test
import com.felpster.musicplayer.data.local.SongDao
import com.felpster.musicplayer.data.local.SongEntity
import com.felpster.musicplayer.data.remote.AlbumResponse
import com.felpster.musicplayer.data.remote.ItunesApi
import com.felpster.musicplayer.data.remote.RemoteAlbum
import com.felpster.musicplayer.data.remote.RemoteSong
import com.felpster.musicplayer.data.remote.SearchResponse
import com.felpster.musicplayer.data.remote.SongResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SongRepositoryTest {

    private lateinit var repository: SongRepositoryImpl
    private val api: ItunesApi = mockk()
    private val songDao: SongDao = mockk()
    private var cachedSongs = listOf<SongEntity>()

    @Before
    fun setUp() {
        repository = SongRepositoryImpl(api, songDao)
        cachedSongs = emptyList()
        every { songDao.getSong(any()) } returns flow { emit(null) }
        every { songDao.searchSongs(any()) } returns flow {
            emit(cachedSongs)
        }
        coEvery { songDao.insertSongs(any()) } answers {
            // Simulate database storage by updating cachedSongs
            @Suppress("UNCHECKED_CAST")
            cachedSongs = firstArg() as List<SongEntity>
        }
    }

    @Test
    fun `searchSongs should map remote songs to domain songs`() = runTest {
        val remoteSong = createRemoteSong(trackId = 1L, trackName = "Song 1")
        coEvery { api.getSongs(any()) } returns SearchResponse(
            resultCount = 1,
            results = listOf(remoteSong)
        )

        repository.searchSongs("query").test {
            val result = awaitItem()
            assertThat(result).hasSize(1)
            assertThat(result[0].id).isEqualTo(1L)
            assertThat(result[0].title).isEqualTo("Song 1")
            awaitComplete()
        }
    }

    @Test
    fun `getAlbumWithSongs should map remote album and songs to domain album`() = runTest {
        val remoteAlbum = createRemoteAlbum(collectionId = 10L, collectionName = "Album 1")
        val remoteSong = createRemoteSong(trackId = 1L, trackName = "Song 1", collectionId = 10L)
        
        coEvery { api.getAlbumSongs(any()) } returns AlbumResponse(
            resultCount = 1,
            results = listOf(remoteSong),
            album = remoteAlbum
        )

        repository.getAlbumWithSongs(10L).test {
            val result = awaitItem()
            assertThat(result.id).isEqualTo(10L)
            assertThat(result.name).isEqualTo("Album 1")
            assertThat(result.songs).hasSize(1)
            assertThat(result.songs[0].title).isEqualTo("Song 1")
            awaitComplete()
        }
    }

    @Test
    fun `getSong should map remote song to domain song`() = runTest {
        val remoteSong = createRemoteSong(trackId = 1L, trackName = "Song 1")
        coEvery { api.getSong(any()) } returns SongResponse(
            resultCount = 1,
            results = listOf(remoteSong)
        )

        repository.getSong(1L).test {
            val result = awaitItem()
            assertThat(result.id).isEqualTo(1L)
            assertThat(result.title).isEqualTo("Song 1")
            awaitComplete()
        }
    }

    @Test
    fun `getSong should throw exception when song not found`() = runTest {
        coEvery { api.getSong(any()) } returns SongResponse(
            resultCount = 0,
            results = emptyList()
        )

        repository.getSong(1L).test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IllegalArgumentException::class.java)
            assertThat(error).hasMessageThat().contains("Song with ID 1 not found")
        }
    }

    private fun createRemoteSong(
        trackId: Long = 1L,
        trackName: String = "Track Name",
        artistName: String = "Artist Name",
        collectionId: Long = 10L,
        artworkUrl100: String = "url",
        trackTimeMillis: Long = 1000L
    ) = RemoteSong(
        wrapperType = "track",
        kind = "song",
        artistId = 1L,
        collectionId = collectionId,
        trackId = trackId,
        artistName = artistName,
        collectionName = "Collection Name",
        trackName = trackName,
        collectionCensoredName = "Censored",
        trackCensoredName = "Censored",
        artistViewUrl = "",
        collectionViewUrl = "",
        trackViewUrl = "",
        previewUrl = "",
        artworkUrl30 = "",
        artworkUrl60 = "",
        artworkUrl100 = artworkUrl100,
        releaseDate = "",
        collectionExplicitness = "",
        trackExplicitness = "",
        discCount = 1,
        discNumber = 1,
        trackCount = 1,
        trackNumber = 1,
        trackTimeMillis = trackTimeMillis,
        country = "",
        currency = "",
        primaryGenreName = ""
    )

    private fun createRemoteAlbum(
        collectionId: Long = 10L,
        collectionName: String = "Collection Name",
        artistName: String = "Artist Name",
        artworkUrl100: String = "url"
    ) = RemoteAlbum(
        wrapperType = "collection",
        collectionType = "Album",
        artistId = 1L,
        collectionId = collectionId,
        artistName = artistName,
        collectionName = collectionName,
        collectionCensoredName = "Censored",
        artistViewUrl = "",
        collectionViewUrl = "",
        artworkUrl60 = "",
        artworkUrl100 = artworkUrl100,
        collectionExplicitness = "",
        trackCount = 1,
        copyright = "",
        country = "",
        currency = "",
        releaseDate = "",
        primaryGenreName = ""
    )
}
