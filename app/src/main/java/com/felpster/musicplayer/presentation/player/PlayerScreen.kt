package com.felpster.musicplayer.presentation.player

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.felpster.core_ui.components.AppBar
import com.felpster.core_ui.components.ErrorView
import com.felpster.core_ui.components.LoadingView
import com.felpster.core_ui.theme.MusicPlayerTheme
import com.felpster.musicplayer.domain.model.Song
import com.felpster.musicplayer.presentation.home.mockSongs

sealed class PlayerViewState {
    data class Success(val song: Song) : PlayerViewState()
    data class Error(val message: String) : PlayerViewState()
    data class Loading(val message: String) : PlayerViewState()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    viewState: PlayerViewState,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AppBar(title = "Now Playing") },
    ) { innerPadding ->
        when (viewState) {
            is PlayerViewState.Success -> {
                PlayerView(
                    song = viewState.song,
                    modifier = Modifier.padding(innerPadding),
                )
            }

            is PlayerViewState.Error -> {
                ErrorView(
                    message = viewState.message,
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                )
            }

            is PlayerViewState.Loading -> {
                LoadingView(
                    message = viewState.message,
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                )
            }
        }
    }
}

@Composable
fun PlayerView(
    song: Song,
    modifier: Modifier = Modifier
) {

}

@Preview
@Composable
fun PlayerViewPreview() {
    MusicPlayerTheme {
        PlayerView(
            song = mockSongs.first()
        )
    }
}
