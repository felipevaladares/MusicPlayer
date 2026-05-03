package com.felpster.musicplayer.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.felpster.musicplayer.domain.model.Song
import com.felpster.musicplayer.presentation.home.HomeNavEvent.NavigateToAlbum
import com.felpster.musicplayer.presentation.home.HomeNavEvent.NavigateToPlayer
import com.felpster.musicplayer.presentation.home.HomeViewState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

val mockSongs = listOf(
    Song(
        id = "1",
        title = "Purple Rain",
        artist = "Prince",
        albumId = "1",
        albumArtUrl = "https://via.placeholder.com/56",
        duration = 240,
    ),
    Song(
        id = "2",
        title = "Power Of Equality",
        artist = "Red Hot Chili Peppers",
        albumId = "2",
        albumArtUrl = "https://via.placeholder.com/56",
        duration = 240,
    ),
    Song(
        id = "3",
        title = "Something",
        artist = "The Beatles",
        albumId = "3",
        albumArtUrl = "https://via.placeholder.com/56",
        duration = 240,
    ),
    Song(
        id = "4",
        title = "Like A Virgin",
        artist = "Madonna",
        albumId = "4",
        albumArtUrl = "https://via.placeholder.com/56",
        duration = 240,
    ),
    Song(
        id = "5",
        title = "Get Lucky",
        artist = "Daft Punk feat. Pharrell Williams",
        albumId = "5",
        albumArtUrl = "https://via.placeholder.com/56",
        duration = 240,
    )
)

sealed interface HomeNavEvent {
    data class NavigateToPlayer(val songId: String) : HomeNavEvent
    data class NavigateToAlbum(val albumId: String) : HomeNavEvent
}

@HiltViewModel
class HomeViewModel @Inject constructor(

): ViewModel() {

    private val navigationEventsChannel = Channel<HomeNavEvent>(Channel.UNLIMITED)
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    var homeViewState by mutableStateOf<HomeViewState>(HomeViewState.Success(mockSongs))
        private set

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SearchQueryChanged -> {
                val query = event.query.trim().lowercase()
                homeViewState = if (query.isEmpty()) {
                    Success(mockSongs)
                } else {
                    val filteredSongs = mockSongs.filter { song ->
                        song.title.lowercase().contains(query) ||
                        song.artist.lowercase().contains(query)
                    }

                    Success(filteredSongs)
                }
            }

            is HomeEvent.SongSelected -> {
                navigationEventsChannel.trySend(NavigateToPlayer(event.song.id))
            }

            is HomeEvent.AlbumOptionSelected -> {
                navigationEventsChannel.trySend(NavigateToAlbum(event.song.albumId))
            }

            is HomeEvent.MenuOptionSelected -> {
                homeViewState = when (val currentState = homeViewState) {
                    is Success -> currentState.copy(actionSheetSong = event.song)
                    else -> currentState
                }
            }

            HomeEvent.MenuOptionDismissed -> {
                homeViewState = when (val currentState = homeViewState) {
                    is Success -> currentState.copy(actionSheetSong = null)
                    else -> currentState
                }
            }
        }
    }
}