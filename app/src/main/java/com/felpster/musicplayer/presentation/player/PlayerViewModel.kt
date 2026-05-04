package com.felpster.musicplayer.presentation.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felpster.musicplayer.commons.Result
import com.felpster.musicplayer.commons.asResult
import com.felpster.musicplayer.domain.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: SongRepository,
): ViewModel() {
    var songId: Long = savedStateHandle["songId"]
        ?: throw IllegalArgumentException("Song ID is required")

    val state: StateFlow<PlayerViewState> by lazy {
        repository
            .getSong(songId)
            .asResult()
            .map { result ->
                when (result) {
                    is Result.Error -> {
                        PlayerViewState.Error(result.exception?.message ?: "An error occurred while retrieving album songs")
                    }
                    is Result.Loading -> PlayerViewState.Loading("Loading song...")
                    is Result.Success -> PlayerViewState.Success(result.data)

                }
            }.stateIn(
                scope = viewModelScope,
                initialValue = PlayerViewState.Loading("Loading song..."),
                started = SharingStarted.WhileSubscribed(5_000),
            )
    }

    fun onEvent(event: PlayerEvent) {
        viewModelScope.launch {
            when (event) {
                is PlayerEvent.PlayPauseToggled -> TODO()
                is PlayerEvent.NextSongRequested -> TODO()
                is PlayerEvent.PreviousSongRequested -> TODO()
                is PlayerEvent.RepeatToggled -> TODO()
            }
        }
    }
}