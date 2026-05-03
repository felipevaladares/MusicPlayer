package com.felpster.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.felpster.core_ui.theme.MusicPlayerTheme
import com.felpster.musicplayer.presentation.album.AlbumScreen
import com.felpster.musicplayer.presentation.album.AlbumViewModel
import com.felpster.musicplayer.presentation.home.HomeNavEvent
import com.felpster.musicplayer.presentation.home.HomeScreen
import com.felpster.musicplayer.presentation.home.HomeViewModel
import com.felpster.musicplayer.presentation.player.PlayerScreen
import com.felpster.musicplayer.presentation.player.PlayerViewModel
import com.felpster.musicplayer.presentation.splash.SplashScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


sealed class Destination(val route: String)  {
    object Home: Destination("home_screen")
    object Splash: Destination("splash_screen")
    object Player: Destination("player_screen/{songId}")

    object Album: Destination("album_screen/{albumId}")
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        setContent {
            MusicPlayerTheme {
                AppNavHost()
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Destination.Splash.route) {
        composable(Destination.Splash.route) {
            SplashScreen {
                navController.navigate(Destination.Home.route) {
                    popUpTo(Destination.Splash.route) {
                        inclusive = true
                    }
                }
            }
        }

        composable(Destination.Home.route) {
            val viewModel = hiltViewModel<HomeViewModel>()

            // Listen to navigation events and navigate to the player screen
            LaunchedEffect(Unit) {
                viewModel.navigationEvents.collectLatest { destination ->
                    when (destination) {
                        is HomeNavEvent.NavigateToPlayer -> {
                            navController.navigate("player_screen/${destination.songId}")
                        }

                        is HomeNavEvent.NavigateToAlbum -> {
                            navController.navigate("album_screen/${destination.albumId}")
                        }
                    }
                }
            }

            HomeScreen(
                viewState = viewModel.homeViewState,
                onEvent = viewModel::onEvent,
            )
        }

        composable(
            route = Destination.Player.route,
            arguments = listOf(navArgument("songId") { type = NavType.StringType })
        ){
            val viewModel = hiltViewModel<PlayerViewModel>()
            PlayerScreen(
                viewState = viewModel.playerViewState,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Destination.Album.route,
            arguments = listOf(navArgument("albumId") { type = NavType.StringType })
        ){
            val viewModel = hiltViewModel<AlbumViewModel>()
            AlbumScreen (
                viewState = viewModel.albumViewState,
                onBack = { navController.popBackStack() }
            )
        }


    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MusicPlayerTheme {
        AppNavHost()
    }
}