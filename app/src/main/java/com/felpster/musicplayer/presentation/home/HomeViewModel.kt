package com.felpster.musicplayer.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felpster.musicplayer.domain.SongRepository
import com.felpster.musicplayer.domain.model.Song
import com.felpster.musicplayer.presentation.home.HomeNavEvent.NavigateToAlbum
import com.felpster.musicplayer.presentation.home.HomeNavEvent.NavigateToPlayer
import com.felpster.musicplayer.presentation.home.HomeViewState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

val mockSongs = listOf(
    Song(
        id = 1,
        title = "Purple Rain",
        artist = "Prince",
        albumId = 1,
        albumArtUrl = "https://via.placeholder.com/56",
        durationMillis = 240,
    ),
    Song(
        id = 2,
        title = "Power Of Equality",
        artist = "Red Hot Chili Peppers",
        albumId = 2,
        albumArtUrl = "https://via.placeholder.com/56",
        durationMillis = 240,
    ),
    Song(
        id = 3,
        title = "Something",
        artist = "The Beatles",
        albumId = 3,
        albumArtUrl = "https://via.placeholder.com/56",
        durationMillis = 240,
    ),
    Song(
        id = 4,
        title = "Like A Virgin",
        artist = "Madonna",
        albumId = 4,
        albumArtUrl = "https://via.placeholder.com/56",
        durationMillis = 240,
    ),
    Song(
        id = 5,
        title = "Get Lucky",
        artist = "Daft Punk feat. Pharrell Williams",
        albumId = 5,
        albumArtUrl = "https://via.placeholder.com/56",
        durationMillis = 240,
    )
)

sealed interface HomeNavEvent {
    data class NavigateToPlayer(val songId: Long) : HomeNavEvent
    data class NavigateToAlbum(val albumId: Long) : HomeNavEvent
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: SongRepository,
): ViewModel() {

    private val navigationEventsChannel = Channel<HomeNavEvent>(Channel.UNLIMITED)
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    var homeViewState by mutableStateOf<HomeViewState>(HomeViewState.Success(mockSongs))
        private set

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SearchQueryChanged -> {
                val query = event.query.trim().lowercase()

                if (query.isEmpty()) {
                    homeViewState = Success(mockSongs)
                } else {
                    searchSongs(query)
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

    private fun searchSongs(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.searchSongs(query)
                .catch {
                    it.printStackTrace()
                }
                .collect { songs ->
                    homeViewState = Success(songs)
                }
        }
    }
}