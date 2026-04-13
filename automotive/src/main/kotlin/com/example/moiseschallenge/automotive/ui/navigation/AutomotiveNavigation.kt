package com.example.moiseschallenge.automotive.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moiseschallenge.automotive.ui.album.AutoAlbumScreen
import com.example.moiseschallenge.automotive.ui.browse.AutoBrowseScreen
import com.example.moiseschallenge.automotive.ui.player.AutoPlayerScreen
import com.example.moiseschallenge.automotive.ui.splash.AutoSplashScreen

object AutoRoutes {
    const val SPLASH = "splash"
    const val BROWSE = "browse"
    const val PLAYER = "player"
    const val ALBUM = "album/{albumId}"

    fun album(albumId: Long) = "album/$albumId"
}

@Composable
fun AutomotiveNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AutoRoutes.SPLASH
    ) {
        composable(AutoRoutes.SPLASH) {
            AutoSplashScreen(
                onNavigateToBrowse = {
                    navController.navigate(AutoRoutes.BROWSE) {
                        popUpTo(AutoRoutes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(AutoRoutes.BROWSE) {
            AutoBrowseScreen(
                onTrackClick = {
                    navController.navigate(AutoRoutes.PLAYER)
                },
                onPlayerClick = {
                    navController.navigate(AutoRoutes.PLAYER)
                }
            )
        }

        composable(AutoRoutes.PLAYER) {
            AutoPlayerScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAlbumClick = { albumId ->
                    navController.navigate(AutoRoutes.album(albumId))
                }
            )
        }

        composable(
            route = AutoRoutes.ALBUM,
            arguments = listOf(navArgument("albumId") { type = NavType.LongType })
        ) {
            AutoAlbumScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onTrackClick = {
                    navController.navigate(AutoRoutes.PLAYER)
                }
            )
        }
    }
}
