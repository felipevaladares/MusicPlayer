package com.felpster.musicplayer.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.felpster.core_ui.R
import com.felpster.core_ui.components.ListItem
import com.felpster.musicplayer.domain.model.Song

@Composable
fun SongItem(
    song: Song,
    modifier: Modifier = Modifier,
    onItemClick: (() -> Unit)? = null,
    onMenuClick: (() -> Unit)? = null,
) {
    ListItem(modifier = modifier
        .clickable(
            enabled = onItemClick != null,
            role = Role.Button,
            onClickLabel = "Play ${song.title}",
            onClick = { onItemClick?.invoke() }
        ),
    ) {
        // Album Art
        AsyncImage(
            model = song.albumArtUrl,
            contentDescription = song.title,
            placeholder = painterResource(id = R.drawable.ic_musical_note),
            error = painterResource(id = R.drawable.ic_musical_note),
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        // Song Info
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = song.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Text(
                text = song.artist,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        // Menu Button
        onMenuClick?.let {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}