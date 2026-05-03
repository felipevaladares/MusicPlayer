package com.felpster.musicplayer.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import com.felpster.musicplayer.domain.model.Song
import com.felpster.musicplayer.presentation.components.SearchBar
import com.felpster.musicplayer.presentation.components.SongItem

sealed class HomeViewState {
    data class Success(val songs: List<Song>) : HomeViewState()
    data class Error(val message: String) : HomeViewState()
    data class Loading(val message: String) : HomeViewState()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewState: HomeViewState,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AppBar(title = "Songs") },
    ) { innerPadding ->
        when (viewState) {
            is HomeViewState.Success -> {
                HomeView(
                    songs = viewState.songs,
                    modifier = Modifier.padding(innerPadding),
                )
            }

            is HomeViewState.Error -> {
                ErrorView(
                    message = viewState.message,
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                )
            }

            is HomeViewState.Loading -> {
                LoadingView(
                    message = viewState.message,
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                )
            }
        }
    }
}

@Composable
fun HomeView(songs: List<Song>, modifier: Modifier = Modifier) {
    val searchQuery = remember { mutableStateOf("") }

    val filteredSongs = songs.filter { song ->
        song.title.contains(searchQuery.value, ignoreCase = true) ||
        song.artist.contains(searchQuery.value, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Search Bar
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            query = searchQuery.value,
            onQueryChange = { searchQuery.value = it },
        )

        // Song List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(filteredSongs) { song ->
                SongItem(
                    song = song,
                    onMenuClick = { /* TODO: Handle menu click */ }
                )
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
        )
    }
}

@Preview
@Composable
fun HomeScreenLoadingPreview() {
    MusicPlayerTheme {
        HomeScreen(
            viewState = HomeViewState.Loading("Fetching music..."),
        )
    }
}

@Preview
@Composable
fun HomeScreenErrorPreview() {
    MusicPlayerTheme {
        HomeScreen(
            viewState = HomeViewState.Error("Failed to load music. Please try again."),
        )
    }
}