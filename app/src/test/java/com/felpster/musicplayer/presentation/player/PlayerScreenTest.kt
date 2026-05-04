package com.felpster.musicplayer.presentation.player

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.felpster.core_ui.components.ErrorLayoutTags
import com.felpster.core_ui.components.LoadingLayoutTags
import com.felpster.core_ui.theme.MusicPlayerTheme
import com.felpster.musicplayer.ComposeTest
import com.felpster.musicplayer.data.repository.FakeSongRepository
import com.felpster.musicplayer.presentation.player.PlayerLayoutTags.PLAYER_LAYOUT
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(qualifiers = "w350dp-h700dp") // Set a larger screen size for testing
@RunWith(AndroidJUnit4::class)
class PlayerScreenTest : ComposeTest() {

    @Test
    fun `Ensure loading view is displayed when state is Loading`() {
        val message = "Loading song..."
        composeTestRule.setContent {
            MusicPlayerTheme {
                PlayerScreen(
                    viewState = PlayerViewState.Loading(message),
                    onEvent = {},
                    onBack = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(LoadingLayoutTags.LOADING_LAYOUT).assertIsDisplayed()
        composeTestRule.onNodeWithText(message).assertIsDisplayed()
    }

    @Test
    fun `Ensure error view is displayed when state is Error`() {
        val errorMessage = "Failed to load song"
        composeTestRule.setContent {
            MusicPlayerTheme {
                PlayerScreen(
                    viewState = PlayerViewState.Error(errorMessage),
                    onEvent = {},
                    onBack = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(ErrorLayoutTags.ERROR_LAYOUT).assertIsDisplayed()
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun `Ensure player content is displayed when state is Success`() {
        val song = FakeSongRepository.songsList.first()

        composeTestRule.setContent {
            MusicPlayerTheme {
                PlayerScreen(
                    viewState = PlayerViewState.Success(song = song),
                    onEvent = {},
                    onBack = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(PLAYER_LAYOUT).assertIsDisplayed()
        composeTestRule.onNodeWithText(song.title).assertIsDisplayed()
        composeTestRule.onNodeWithText(song.artist).assertIsDisplayed()
        
        // Check for playback controls content descriptions
        composeTestRule.onNodeWithContentDescription("Play").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Previous").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Next").assertIsDisplayed()
    }
}
