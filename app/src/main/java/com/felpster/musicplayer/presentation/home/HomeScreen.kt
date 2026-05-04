package com.felpster.musicplayer.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.felpster.core_ui.components.AppBar
import com.felpster.core_ui.components.ErrorView
import com.felpster.core_ui.components.LoadingView
import com.felpster.core_ui.theme.MusicPlayerTheme
import com.felpster.musicplayer.commons.mockSongs
import com.felpster.musicplayer.domain.model.Song
import com.felpster.musicplayer.presentation.components.MoreOptionsSheet
import com.felpster.musicplayer.presentation.components.SearchBar
import com.felpster.musicplayer.presentation.components.SongItem

sealed class HomeViewState {
    data class Success(val songs: List<Song>, val actionSheetSong: Song? = null) : HomeViewState()
    data class Error(val message: String) : HomeViewState()
    data class Loading(val message: String) : HomeViewState()
}

sealed interface HomeEvent {
    data class SongSelected(val song: Song) : HomeEvent
    data class SearchQueryChanged(val query: String) : HomeEvent
    data class MenuOptionSelected(val song: Song) : HomeEvent
    data class AlbumOptionSelected(val song: Song) : HomeEvent
    data object MenuOptionDismissed : HomeEvent
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewState: HomeViewState,
    onEvent: (HomeEvent) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AppBar(title = "Songs") },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            val searchQuery = remember { mutableStateOf("") }
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                query = searchQuery.value,
                onQueryChange = {
                    searchQuery.value = it
                    onEvent(HomeEvent.SearchQueryChanged(it))
                },
            )

            when (viewState) {
                is HomeViewState.Success -> {
                    HomeView(
                        songs = viewState.songs,
                        onEvent = onEvent,
                    )

                    viewState.actionSheetSong?.let { song ->
                        MoreOptionsSheet(
                            song = song,
                            onDismissRequest = { onEvent(HomeEvent.MenuOptionDismissed) },
                            onViewAlbum = { onEvent(HomeEvent.AlbumOptionSelected(song)) }
                        )
                    }
                }

                is HomeViewState.Error -> {
                    ErrorView(
                        message = viewState.message,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                is HomeViewState.Loading -> {
                    LoadingView(
                        message = viewState.message,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}

@Composable
fun HomeView(songs: List<Song>, onEvent: (HomeEvent) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Song List or Empty State
        if (songs.isEmpty()) {
            Text(
                text = "No songs found, try searching for a different artist or song",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(songs) { song ->
                    SongItem(
                        song = song,
                        onItemClick = { onEvent(HomeEvent.SongSelected(song)) },
                        onMenuClick = { onEvent(HomeEvent.MenuOptionSelected(song)) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    MusicPlayerTheme {
        HomeScreen(
            viewState = HomeViewState.Success(mockSongs),
            onEvent = {}
        )
    }
}

@Preview
@Composable
fun HomeScreenEmptyPreview() {
    MusicPlayerTheme {
        HomeScreen(
            viewState = HomeViewState.Success(emptyList()),
            onEvent = {}
        )
    }
}

@Preview
@Composable
fun HomeScreenLoadingPreview() {
    MusicPlayerTheme {
        HomeScreen(
            viewState = HomeViewState.Loading("Fetching music..."),
            onEvent = {}
        )
    }
}

@Preview
@Composable
fun HomeScreenErrorPreview() {
    MusicPlayerTheme {
        HomeScreen(
            viewState = HomeViewState.Error("Failed to load music. Please try again."),
            onEvent = {}
        )
    }
}