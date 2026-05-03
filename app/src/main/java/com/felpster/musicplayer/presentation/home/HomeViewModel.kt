package com.felpster.musicplayer.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.felpster.musicplayer.domain.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

val mockSongs = listOf(
    Song(
        id = "1",
        title = "Purple Rain",
        artist = "Prince",
        albumArtUrl = "https://via.placeholder.com/56",
        duration = 240,
    ),
    Song(
        id = "2",
        title = "Power Of Equality",
        artist = "Red Hot Chili Peppers",
        albumArtUrl = "https://via.placeholder.com/56",
        duration = 240,
    ),
    Song(
        id = "3",
        title = "Something",
        artist = "The Beatles",
        albumArtUrl = "https://via.placeholder.com/56",
        duration = 240,
    ),
    Song(
        id = "4",
        title = "Like A Virgin",
        artist = "Madonna",
        albumArtUrl = "https://via.placeholder.com/56",
        duration = 240,
    ),
    Song(
        id = "5",
        title = "Get Lucky",
        artist = "Daft Punk feat. Pharrell Williams",
        albumArtUrl = "https://via.placeholder.com/56",
        duration = 240,
    )
)

sealed interface HomeNavEvent {
     data class NavigateToPlayer(val song: Song) : HomeNavEvent
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
                    HomeViewState.Success(mockSongs)
                } else {
                    val filteredSongs = mockSongs.filter { song ->
                        song.title.lowercase().contains(query) ||
                        song.artist.lowercase().contains(query)
                    }

                    HomeViewState.Success(filteredSongs)
                }
            }

            is HomeEvent.SongSelected -> {
                // Handle song selection, e.g. navigate to player screen
                navigationEventsChannel.trySend(HomeNavEvent.NavigateToPlayer(event.song))
            }

            is HomeEvent.MenuOptionSelected -> {
                // Handle menu option selection, e.g. show context menu
            }
        }
    }
}