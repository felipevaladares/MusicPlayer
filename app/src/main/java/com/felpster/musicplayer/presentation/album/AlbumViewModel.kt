package com.felpster.musicplayer.presentation.album

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.felpster.musicplayer.presentation.home.mockSongs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    var albumId: String =
        savedStateHandle["albumId"] ?: throw IllegalArgumentException("Album ID is required")

    var albumViewState by mutableStateOf<AlbumViewState>(
        AlbumViewState.Success(
            albumName = "Album name",
            artistName = "Artist name",
            albumArtUrl = "https://via.placeholder.com/150",
            songs = mockSongs,
        )
    )
        private set
}