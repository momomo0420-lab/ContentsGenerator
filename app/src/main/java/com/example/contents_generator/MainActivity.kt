package com.example.contents_generator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.contents_generator.ui.screens.settings.SettingsScreen
import com.example.contents_generator.ui.theme.ContentsGeneratorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContentsGeneratorTheme {
                SettingsScreen()
            }
        }
    }
}
