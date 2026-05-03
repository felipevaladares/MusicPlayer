package com.felpster.musicplayer.presentation.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

        // Playback Slider
        PlayerSlider(song)
        Spacer(modifier = Modifier.height(32.dp))

        // Playback Controls
        PlayerControls()
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun PlayerSlider(song: Song) {
    var sliderPosition by remember { mutableFloatStateOf(0.33f) }
    val currentPosition = (song.duration * sliderPosition).toInt()
    Column(modifier = Modifier.fillMaxWidth()) {
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.Gray
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = formatTime(currentPosition), color = Color.Gray, fontSize = 12.sp)
            Text(
                text = "-" + formatTime(song.duration),
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun PlayerControls() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFF222222)),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = { /* Play/Pause */ }) {
                Icon(
                    imageVector = Icons.Default.PlayArrow, // Swap with Pause if playing
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        IconButton(onClick = { /* Previous */ }) {
            Icon(
                imageVector = Icons.Default.FastRewind,
                contentDescription = "Previous",
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }

        IconButton(onClick = { /* Next */ }) {
            Icon(
                imageVector = Icons.Default.FastForward,
                contentDescription = "Next",
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = { /* Repeat */ }) {
            Icon(
                imageVector = Icons.Default.Repeat,
                contentDescription = "Repeat",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}


private fun formatTime(seconds: Int): String {
    val min = seconds / 60
    val sec = seconds % 60
    return "%d:%02d".format(min, sec)
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
