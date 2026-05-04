package com.felpster.musicplayer.presentation.player

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.felpster.musicplayer.MainDispatcherRule
import com.felpster.musicplayer.data.repository.FakeSongRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: PlayerViewModel
    private lateinit var repository: FakeSongRepository
    private val songId = 1L

    @Before
    fun setUp() {
        repository = FakeSongRepository()
        viewModel = PlayerViewModel(
            savedStateHandle = SavedStateHandle(mapOf("songId" to songId)),
            repository = repository
        )
    }

    @Test
    fun `state should be Loading initially`() = runTest {
        viewModel.state.test {
            val item = awaitItem()
            assertThat(item).isInstanceOf(PlayerViewState.Loading::class.java)
        }
    }

    @Test
    fun `state should be Success after loading song`() = runTest {
        val song = FakeSongRepository.songsList[0]
        
        viewModel.state.test {
            // Initial loading
            assertThat(awaitItem()).isInstanceOf(PlayerViewState.Loading::class.java)
            
            // Emit the song
            launch {
                repository.emitSong(song)
            }

            // Should now be Success
            val state = awaitItem()
            assertThat(state).isInstanceOf(PlayerViewState.Success::class.java)
            assertThat((state as PlayerViewState.Success).song).isEqualTo(song)
        }
    }

    @Test
    fun `state should be Error when repository fails`() = runTest {
        val errorMessage = "Network Error"
        repository.setError(Exception(errorMessage))

        viewModel.state.test {
            val errorState = awaitItem()
            assertThat(errorState).isInstanceOf(PlayerViewState.Error::class.java)
        }
    }
}
