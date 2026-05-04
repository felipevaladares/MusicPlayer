package com.felpster.musicplayer.presentation.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.felpster.core_ui.components.ErrorLayoutTags
import com.felpster.core_ui.components.LoadingLayoutTags
import com.felpster.core_ui.theme.MusicPlayerTheme
import com.felpster.musicplayer.ComposeTest
import com.felpster.musicplayer.data.repository.FakeSongRepository
import com.felpster.musicplayer.presentation.home.HomeLayoutTags.EMPTY_STATE
import com.felpster.musicplayer.presentation.home.HomeLayoutTags.HOME_LAYOUT
import com.felpster.musicplayer.presentation.home.HomeLayoutTags.SEARCH_BAR
import com.felpster.musicplayer.presentation.home.HomeLayoutTags.SONG_LIST
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest : ComposeTest() {

    @Test
    fun `Ensure loading view is displayed when state is Loading`() {
        val message = "Loading songs..."
        composeTestRule.setContent {
            MusicPlayerTheme {
                HomeScreen(
                    viewState = HomeViewState.Loading(message),
                    onEvent = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(LoadingLayoutTags.LOADING_LAYOUT).assertIsDisplayed()
        composeTestRule.onNodeWithText(message).assertIsDisplayed()
    }

    @Test
    fun `Ensure error view is displayed when state is Error`() {
        val errorMessage = "Failed to load songs"
        composeTestRule.setContent {
            MusicPlayerTheme {
                HomeScreen(
                    viewState = HomeViewState.Error(errorMessage),
                    onEvent = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(ErrorLayoutTags.ERROR_LAYOUT).assertIsDisplayed()
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun `Ensure empty state is displayed when state is Success with no songs`() {
        composeTestRule.setContent {
            MusicPlayerTheme {
                HomeScreen(
                    viewState = HomeViewState.Success(emptyList()),
                    onEvent = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(HOME_LAYOUT).assertIsDisplayed()
        composeTestRule.onNodeWithTag(EMPTY_STATE).assertIsDisplayed()
        composeTestRule.onNodeWithText("No songs found", substring = true).assertIsDisplayed()
    }

    @Test
    fun `Ensure song list is displayed when state is Success with songs`() {
        val songs = FakeSongRepository.songsList

        composeTestRule.setContent {
            MusicPlayerTheme {
                HomeScreen(
                    viewState = HomeViewState.Success(songs),
                    onEvent = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(HOME_LAYOUT).assertIsDisplayed()
        composeTestRule.onNodeWithTag(SONG_LIST).assertExists()

        // Verify song list by scrolling to each item
        songs.forEach { song ->
            composeTestRule.onNodeWithTag(SONG_LIST)
                .performScrollToNode(hasText(song.title))

            composeTestRule.onNodeWithText(song.title).assertExists()
        }
    }

    @Test
    fun `Ensure more options sheet is displayed when actionSheetSong is not null`() {
        val song = FakeSongRepository.songsList.first()

        composeTestRule.setContent {
            MusicPlayerTheme {
                HomeScreen(
                    viewState = HomeViewState.Success(
                        songs = FakeSongRepository.songsList,
                        actionSheetSong = song
                    ),
                    onEvent = {}
                )
            }
        }

        // Verify the sheet content (e.g., View Album option)
        composeTestRule.onNodeWithText("View Album", ignoreCase = true).assertExists()
    }

    @Test
    fun `Ensure SearchQueryChanged event is triggered when typing in search bar`() {
        val onEvent: (HomeEvent) -> Unit = mockk(relaxed = true)
        val query = "Prince"

        composeTestRule.setContent {
            MusicPlayerTheme {
                HomeScreen(
                    viewState = HomeViewState.Success(emptyList()),
                    onEvent = onEvent
                )
            }
        }

        composeTestRule.onNodeWithTag(SEARCH_BAR).performTextInput(query)

        verify { onEvent(HomeEvent.SearchQueryChanged(query)) }
    }

    @Test
    fun `Ensure SongSelected event is triggered when clicking a song`() {
        val onEvent: (HomeEvent) -> Unit = mockk(relaxed = true)
        val songs = FakeSongRepository.songsList
        val songToSelect = songs.first()

        composeTestRule.setContent {
            MusicPlayerTheme {
                HomeScreen(
                    viewState = HomeViewState.Success(songs),
                    onEvent = onEvent
                )
            }
        }

        composeTestRule.onNodeWithText(songToSelect.title).performClick()

        verify { onEvent(HomeEvent.SongSelected(songToSelect)) }
    }
}
