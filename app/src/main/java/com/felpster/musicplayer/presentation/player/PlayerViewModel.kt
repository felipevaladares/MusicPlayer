package com.felpster.musicplayer.presentation.player

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.felpster.musicplayer.presentation.home.mockSongs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(

): ViewModel() {
    var playerViewState by mutableStateOf(PlayerViewState.Success(mockSongs.first()))
        private set
}