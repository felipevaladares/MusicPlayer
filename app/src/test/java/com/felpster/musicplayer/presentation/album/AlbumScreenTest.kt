package com.felpster.musicplayer.presentation.album

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.felpster.core_ui.components.ErrorLayoutTags
import com.felpster.core_ui.components.LoadingLayoutTags
import com.felpster.core_ui.theme.MusicPlayerTheme
import com.felpster.musicplayer.ComposeTest
import com.felpster.musicplayer.data.repository.FakeSongRepository
import com.felpster.musicplayer.presentation.album.AlbumLayoutTags.ALBUM_LAYOUT
import com.felpster.musicplayer.presentation.album.AlbumLayoutTags.SONG_LIST
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlbumScreenTest : ComposeTest() {

    @Test
    fun `Ensure loading view is displayed when state is Loading`() {
        val message = "Loading album..."
        composeTestRule.setContent {
            MusicPlayerTheme {
                AlbumScreen(
                    viewState = AlbumViewState.Loading(message),
                    onBack = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(LoadingLayoutTags.LOADING_LAYOUT).assertIsDisplayed()
        composeTestRule.onNodeWithText(message).assertIsDisplayed()
    }

    @Test
    fun `Ensure error view is displayed when state is Error`() {
        val errorMessage = "Failed to load album"
        composeTestRule.setContent {
            MusicPlayerTheme {
                AlbumScreen(
                    viewState = AlbumViewState.Error(errorMessage),
                    onBack = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(ErrorLayoutTags.ERROR_LAYOUT).assertIsDisplayed()
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun `Ensure album content is displayed when state is Success`() {
        val albumName = "Test Album"
        val artistName = "Test Artist"
        val songs = FakeSongRepository.songsList

        composeTestRule.setContent {
            MusicPlayerTheme {
                AlbumScreen(
                    viewState = AlbumViewState.Success(
                        albumName = albumName,
                        artistName = artistName,
                        albumArtUrl = "",
                        songs = songs
                    ),
                    onBack = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(ALBUM_LAYOUT).assertIsDisplayed()
        composeTestRule.onNodeWithText(albumName).assertIsDisplayed()
        composeTestRule.onNodeWithText(artistName).assertIsDisplayed()

        // Verify song list by scrolling to each item
        songs.forEach { song ->
            composeTestRule.onNodeWithTag(SONG_LIST)
                .performScrollToNode(hasText(song.title))

            composeTestRule.onNodeWithText(song.title).assertIsDisplayed()
        }
    }
}
