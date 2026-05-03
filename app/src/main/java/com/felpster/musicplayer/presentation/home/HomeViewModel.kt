package com.felpster.musicplayer.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.felpster.musicplayer.domain.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

val mockSongs = listOf(
    Song(
        id = "1",
        title = "Purple Rain",
        artist = "Prince",
        albumArtUrl = "https://via.placeholder.com/56"
    ),
    Song(
        id = "2",
        title = "Power Of Equality",
        artist = "Red Hot Chili Peppers",
        albumArtUrl = "https://via.placeholder.com/56"
    ),
    Song(
        id = "3",
        title = "Something",
        artist = "The Beatles",
        albumArtUrl = "https://via.placeholder.com/56"
    ),
    Song(
        id = "4",
        title = "Like A Virgin",
        artist = "Madonna",
        albumArtUrl = "https://via.placeholder.com/56"
    ),
    Song(
        id = "5",
        title = "Get Lucky",
        artist = "Daft Punk feat. Pharrell Williams",
        albumArtUrl = "https://via.placeholder.com/56"
    )
)

@HiltViewModel
class HomeViewModel @Inject constructor(

): ViewModel() {
    var homeViewState by mutableStateOf(HomeViewState.Success(mockSongs))
        private set
}