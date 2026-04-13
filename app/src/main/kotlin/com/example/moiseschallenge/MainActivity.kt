package com.example.moiseschallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.moiseschallenge.ui.navigation.MoisesNavHost
import com.example.moiseschallenge.ui.theme.MoisesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoisesTheme {
                val navController = rememberNavController()
                MoisesNavHost(navController = navController)
            }
        }
    }
}
