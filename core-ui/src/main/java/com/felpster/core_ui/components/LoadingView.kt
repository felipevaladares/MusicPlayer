package com.felpster.core_ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.felpster.core_ui.components.LoadingLayoutTags.LOADING_LAYOUT
import com.felpster.core_ui.theme.MusicPlayerTheme

@Composable
fun LoadingView(
    message: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.testTag(LOADING_LAYOUT),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )

        message?.let {
            Text(
                modifier = Modifier.padding(top = 32.dp),
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
                text = it,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingViewPreview() {
    MusicPlayerTheme {
        LoadingView(message = "Loading...")
    }
}

object LoadingLayoutTags {
    const val LOADING_LAYOUT = "LoadingLayoutTags_content"
}

