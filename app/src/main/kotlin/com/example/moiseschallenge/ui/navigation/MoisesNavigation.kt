package com.example.moiseschallenge.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.moiseschallenge.ui.album.AlbumScreen
import com.example.moiseschallenge.ui.player.PlayerScreen
import com.example.moiseschallenge.ui.songs.SongsScreen
import com.example.moiseschallenge.ui.splash.SplashScreen

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Songs : Screen("songs")
    data object Player : Screen("player/{trackId}") {
        fun createRoute(trackId: Long) = "player/$trackId"
        const val CURRENT = "player/-1"  // Route for current playing track
    }
    data object Album : Screen("album/{albumId}") {
        fun createRoute(albumId: Long) = "album/$albumId"
    }
}

@Composable
fun MoisesNavHost(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToSongs = {
                    navController.navigate(Screen.Songs.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Songs.route) {
            SongsScreen(
                onTrackClick = { trackId ->
                    navController.navigate(Screen.Player.createRoute(trackId))
                },
                onAlbumClick = { albumId ->
                    navController.navigate(Screen.Album.createRoute(albumId))
                },
                onPlayerClick = {
                    navController.navigate(Screen.Player.CURRENT)
                }
            )
        }

        composable(
            route = Screen.Player.route,
            arguments = listOf(
                navArgument("trackId") { type = NavType.LongType }
            )
        ) {
            PlayerScreen(
                onNavigateBack = { navController.popBackStack() },
                onAlbumClick = { albumId ->
                    navController.navigate(Screen.Album.createRoute(albumId))
                }
            )
        }

        composable(
            route = Screen.Album.route,
            arguments = listOf(
                navArgument("albumId") { type = NavType.LongType }
            )
        ) {
            AlbumScreen(
                onNavigateBack = { navController.popBackStack() },
                onTrackClick = { trackId ->
                    navController.navigate(Screen.Player.createRoute(trackId))
                }
            )
        }
    }
}
