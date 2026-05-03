package com.felpster.musicplayer.presentation.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.felpster.core_ui.R
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
    Column(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // Album Art
        AsyncImage(
            modifier = Modifier
                .size(260.dp)
                .clip(RoundedCornerShape(24.dp)),

            model = song.albumArtUrl,
            placeholder = painterResource(id = R.drawable.ic_musical_note),
            error = painterResource(id = R.drawable.ic_musical_note),
            contentDescription = song.title,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Song Title
        Text(
            text = song.title,
            fontSize = 32.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        )

        // Artist
        Text(
            text = song.artist,
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview
@Composable
fun PlayerViewPreview() {
    MusicPlayerTheme {
        PlayerScreen(
            viewState = PlayerViewState.Success(song = mockSongs.first())
        )
    }
}
