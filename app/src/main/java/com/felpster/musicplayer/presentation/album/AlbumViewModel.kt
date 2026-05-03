package com.felpster.musicplayer.presentation.album

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
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: SongRepository,
): ViewModel() {
    var albumId: Long =
        savedStateHandle["albumId"] ?: throw IllegalArgumentException("Album ID is required")

    val state: StateFlow<AlbumViewState> by lazy {
        repository
            .getAlbumWithSongs(albumId)
            .asResult()
            .map { result ->
                when (result) {
                    is Result.Error -> {
                        result.exception?.printStackTrace()
                        AlbumViewState.Error(result.exception?.message ?: "An error occurred while retrieving album songs")
                    }
                    is Result.Loading -> AlbumViewState.Loading("Retrieving album songs...")
                    is Result.Success -> {
                        AlbumViewState.Success(
                            albumName = result.data.name,
                            artistName = result.data.artistName,
                            albumArtUrl = result.data.artUrl,
                            songs = result.data.songs
                        )
                    }
                }
            }.stateIn(
                scope = viewModelScope,
                initialValue = AlbumViewState.Loading("Retrieving album songs..."),
                started = SharingStarted.WhileSubscribed(5_000),
            )
    }
}