package com.felpster.musicplayer.presentation.album

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.felpster.musicplayer.MainDispatcherRule
import com.felpster.musicplayer.data.repository.FakeSongRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: AlbumViewModel
    private lateinit var repository: FakeSongRepository
    private val albumId = 10L

    @Before
    fun setUp() {
        repository = FakeSongRepository()
        viewModel = AlbumViewModel(
            savedStateHandle = SavedStateHandle(mapOf("albumId" to albumId)),
            repository = repository
        )
    }

    @Test
    fun `state should be Loading initially`() = runTest {
        viewModel.state.test {
            val item = awaitItem()
            assertThat(item).isInstanceOf(AlbumViewState.Loading::class.java)
        }
    }

    @Test
    fun `state should be Success after loading album`() = runTest {
        val album = FakeSongRepository.album

        viewModel.state.test {
            // Initial loading
            assertThat(awaitItem()).isInstanceOf(AlbumViewState.Loading::class.java)

            repository.emitAlbum(album)

            val state = awaitItem()
            assertThat(state).isInstanceOf(AlbumViewState.Success::class.java)
            val successState = state as AlbumViewState.Success
            assertThat(successState.albumName).isEqualTo(album.name)
            assertThat(successState.songs).isEqualTo(album.songs)
        }
    }

    @Test
    fun `state should be Error when repository fails`() = runTest {
        val errorMessage = "Database Error"
        repository.setError(Exception(errorMessage))

        viewModel.state.test {
            val item = expectMostRecentItem()
            assertThat(item).isInstanceOf(AlbumViewState.Error::class.java)
            assertThat((item as AlbumViewState.Error).message).isEqualTo(errorMessage)
        }
    }
}
