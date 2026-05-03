package com.felpster.musicplayer.presentation.album

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.felpster.core_ui.R
import com.felpster.core_ui.components.AppBar
import com.felpster.core_ui.components.ErrorView
import com.felpster.core_ui.components.LoadingView
import com.felpster.core_ui.theme.MusicPlayerTheme
import com.felpster.musicplayer.commons.mockSongs
import com.felpster.musicplayer.domain.model.Song
import com.felpster.musicplayer.presentation.components.SongItem

sealed class AlbumViewState {
    data class Success(
        val albumName: String,
        val artistName: String,
        val albumArtUrl: String,
        val songs: List<Song>
    ) : AlbumViewState()
    data class Error(val message: String) : AlbumViewState()
    data class Loading(val message: String) : AlbumViewState()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreen(
    viewState: AlbumViewState,
    onBack: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
                title = "Album title",
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = Color.White,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        when (viewState) {
            is AlbumViewState.Success -> {
                AlbumView(
                    viewState = viewState,
                    modifier = Modifier.padding(innerPadding),
                )
            }

            is AlbumViewState.Error -> {
                ErrorView(
                    message = viewState.message,
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                )
            }

            is AlbumViewState.Loading -> {
                LoadingView(
                    message = viewState.message,
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                )
            }
        }
    }
}

@Composable
fun AlbumView(
    viewState: AlbumViewState.Success,
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
                .size(120.dp)
                .clip(RoundedCornerShape(24.dp)),

            model = viewState.albumArtUrl,
            placeholder = painterResource(id = R.drawable.ic_musical_note),
            error = painterResource(id = R.drawable.ic_musical_note),
            contentDescription = viewState.albumName,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Album Name
        Text(
            text = viewState.albumName,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        )

        // Artist
        Text(
            text = viewState.artistName,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Song List
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(viewState.songs) { song ->
                SongItem(song = song)
            }
        }
    }
}

@Preview
@Composable
fun AlbumViewPreview() {
    MusicPlayerTheme {
        AlbumScreen(
            viewState = AlbumViewState.Success(
                albumName = "Mock Album",
                artistName = "Mock Artist",
                albumArtUrl = "https://via.placeholder.com/300",
                songs = mockSongs
            ),
            onBack = {}
        )
    }
}
