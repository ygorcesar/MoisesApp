package com.example.moiseschallenge.automotive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.moiseschallenge.automotive.ui.navigation.AutomotiveNavigation
import com.example.moiseschallenge.automotive.ui.theme.AutomotiveTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AutomotiveMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AutomotiveTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AutomotiveNavigation()
                }
            }
        }
    }
}
