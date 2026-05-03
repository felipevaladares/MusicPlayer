package com.felpster.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.felpster.core_ui.theme.MusicPlayerTheme
import com.felpster.musicplayer.presentation.home.HomeScreen
import com.felpster.musicplayer.presentation.home.HomeViewModel
import com.felpster.musicplayer.presentation.splash.SplashScreen
import dagger.hilt.android.AndroidEntryPoint

sealed class Destination(val route: String) {
    object Home: Destination("home_screen")
    object Splash: Destination("splash_screen")
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
            SplashScreen{
                navController.navigate(Destination.Home.route) {
                    popUpTo(Destination.Splash.route) {
                        inclusive = true
                    }
                }
            }
        }

        composable(Destination.Home.route) {
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(
                viewState = viewModel.homeViewState,
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