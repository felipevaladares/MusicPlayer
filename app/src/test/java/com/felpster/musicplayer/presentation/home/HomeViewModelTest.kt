package com.felpster.musicplayer.presentation.home

import app.cash.turbine.test
import com.felpster.musicplayer.MainDispatcherRule
import com.felpster.musicplayer.data.repository.FakeSongRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: HomeViewModel
    private lateinit var repository: FakeSongRepository

    @Before
    fun setUp() {
        repository = FakeSongRepository()
        viewModel = HomeViewModel(repository, mainDispatcherRule.testDispatcher)
    }

    @Test
    fun `initial state should be Loading while fetching recently played`() = runTest {
        val state = viewModel.homeViewState
        assertThat(state).isInstanceOf(HomeViewState.Loading::class.java)
    }

    @Test
    fun `initial state should be Success after emitting recently played songs`() = runTest {
        repository.emitRecentlyPlayed(FakeSongRepository.songsList)

        // Give time for the state to update
        advanceUntilIdle()

        val state = viewModel.homeViewState
        assertThat(state).isInstanceOf(HomeViewState.Success::class.java)
        assertThat((state as HomeViewState.Success).songs).isEqualTo(FakeSongRepository.songsList)
    }

    @Test
    fun `state should be Success after searching songs`() = runTest {
        viewModel.onEvent(HomeEvent.SearchQueryChanged("song"))

        repository.emitSongs(FakeSongRepository.songsList)

        val state = viewModel.homeViewState
        assertThat(state).isInstanceOf(HomeViewState.Success::class.java)
        assertThat((state as HomeViewState.Success).songs).isEqualTo(FakeSongRepository.songsList)
    }

    @Test
    fun `state should be Success with empty list when query is empty`() = runTest {
        viewModel.onEvent(HomeEvent.SearchQueryChanged(""))

        val state = viewModel.homeViewState
        assertThat(state).isInstanceOf(HomeViewState.Success::class.java)
        assertThat((state as HomeViewState.Success).songs).isEmpty()
    }

    @Test
    fun `navigationEvents should emit NavigateToPlayer when SongSelected event is triggered`() = runTest {
        val song = FakeSongRepository.songsList[0]
        
        viewModel.navigationEvents.test {
            viewModel.onEvent(HomeEvent.SongSelected(song))
            
            val event = awaitItem()
            assertThat(event).isEqualTo(HomeNavEvent.NavigateToPlayer(song.id))
        }
    }

    @Test
    fun `navigationEvents should emit NavigateToAlbum when AlbumOptionSelected event is triggered`() = runTest {
        val song = FakeSongRepository.songsList[0]

        viewModel.navigationEvents.test {
            viewModel.onEvent(HomeEvent.AlbumOptionSelected(song))

            val event = awaitItem()
            assertThat(event).isEqualTo(HomeNavEvent.NavigateToAlbum(song.albumId))
        }
    }

    @Test
    fun `actionSheetSong should be set when MenuOptionSelected event is triggered`() = runTest {
        // First emit recently played to get to Success state
        repository.emitRecentlyPlayed(FakeSongRepository.songsList)
        advanceUntilIdle()

        val song = FakeSongRepository.songsList[0]
        viewModel.onEvent(HomeEvent.MenuOptionSelected(song))

        val state = viewModel.homeViewState
        assertThat(state).isInstanceOf(HomeViewState.Success::class.java)
        assertThat((state as HomeViewState.Success).actionSheetSong).isEqualTo(song)
    }

    @Test
    fun `actionSheetSong should be null when MenuOptionDismissed event is triggered`() = runTest {
        // First emit recently played to get to Success state
        repository.emitRecentlyPlayed(FakeSongRepository.songsList)
        advanceUntilIdle()

        val song = FakeSongRepository.songsList[0]
        viewModel.onEvent(HomeEvent.MenuOptionSelected(song))
        viewModel.onEvent(HomeEvent.MenuOptionDismissed)

        val state = viewModel.homeViewState
        assertThat(state).isInstanceOf(HomeViewState.Success::class.java)
        assertThat((state as HomeViewState.Success).actionSheetSong).isNull()
    }

    @Test
    fun `state should be Success with emptyList when repository fails`() = runTest {
        val errorMessage = "Database Error"
        repository.setError(Exception(errorMessage))

        viewModel.onEvent(HomeEvent.SearchQueryChanged("song"))

        val item = viewModel.homeViewState
        assertThat(item).isInstanceOf(HomeViewState.Error::class.java)
        assertThat((item as HomeViewState.Error).message).isEqualTo(errorMessage)
    }
}
