package com.felpster.musicplayer.presentation.player

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.felpster.musicplayer.domain.model.Song
import com.felpster.musicplayer.presentation.home.mockSongs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    var song: Song = mockSongs.first{ it.id == savedStateHandle["songId"] }

    var playerViewState by mutableStateOf<PlayerViewState>(PlayerViewState.Success(song))
        private set
}