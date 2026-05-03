package com.felpster.musicplayer.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.felpster.core_ui.R
import com.felpster.core_ui.theme.MusicPlayerTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onComplete: () -> Unit
) {
    LaunchedEffect(true) {
        delay(2000) // Simulate loading time (2 seconds)
        onComplete() // Notify that the splash screen is complete
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_default_background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize() // Matches the parent Box size
        )

        Image(
            painter = painterResource(id = R.drawable.ic_musical_note),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(100.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    MusicPlayerTheme {
        SplashScreen(){}
    }
}