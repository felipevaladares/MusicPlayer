package com.felpster.musicplayer.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.felpster.core_ui.components.ErrorView
import com.felpster.core_ui.components.LoadingView
import com.felpster.core_ui.theme.MusicPlayerTheme

sealed class HomeViewState {
    data class Success(val data: String) : HomeViewState()
    data class Error(val message: String) : HomeViewState()
    data class Loading(val message: String) : HomeViewState()
}

@Composable
fun HomeScreen(
    viewState: HomeViewState,
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when(viewState) {
            is HomeViewState.Success -> {
                HomeView(
                    data = viewState.data,
                    modifier = Modifier.padding(innerPadding))
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
fun HomeView(data: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = data)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MusicPlayerTheme {
        HomeScreen(
            HomeViewState.Success("Music loaded successfully!"),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UsersScreenLoadingPreview() {
    MusicPlayerTheme {
        HomeScreen(
            HomeViewState.Loading("Fetching music..."),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UsersScreenErrorPreview() {
    MusicPlayerTheme {
        HomeScreen(
            HomeViewState.Error("Failed to load music. Please try again."),
        )
    }
}