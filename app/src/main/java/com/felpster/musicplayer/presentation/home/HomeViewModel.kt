package com.felpster.musicplayer.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felpster.musicplayer.domain.SongRepository
import com.felpster.musicplayer.presentation.home.HomeNavEvent.NavigateToAlbum
import com.felpster.musicplayer.presentation.home.HomeNavEvent.NavigateToPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeNavEvent {
    data class NavigateToPlayer(val songId: Long) : HomeNavEvent
    data class NavigateToAlbum(val albumId: Long) : HomeNavEvent
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: SongRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel() {

    private val navigationEventsChannel = Channel<HomeNavEvent>(Channel.UNLIMITED)
    val navigationEvents = navigationEventsChannel.receiveAsFlow()

    var homeViewState by mutableStateOf<HomeViewState>(HomeViewState.Success(emptyList()))
        private set

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SearchQueryChanged -> {
                val query = event.query.trim().lowercase()

                if (query.isEmpty()) {
                    homeViewState = HomeViewState.Success(emptyList())
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
                    is HomeViewState.Success -> currentState.copy(actionSheetSong = event.song)
                    else -> currentState
                }
            }

            HomeEvent.MenuOptionDismissed -> {
                homeViewState = when (val currentState = homeViewState) {
                    is HomeViewState.Success -> currentState.copy(actionSheetSong = null)
                    else -> currentState
                }
            }
        }
    }

    private fun searchSongs(query: String) {
        viewModelScope.launch(ioDispatcher) {
            repository.searchSongs(query)
                .catch {
                    it.printStackTrace()
                }
                .collect { songs ->
                    homeViewState = HomeViewState.Success(songs)
                }
        }
    }
}