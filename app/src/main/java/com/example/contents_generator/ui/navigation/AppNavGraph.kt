package com.example.contents_generator.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.contents_generator.ui.screens.name_generator.NameGeneratorScreen
import com.example.contents_generator.ui.screens.settings.SettingsScreen
import kotlinx.serialization.Serializable

sealed class Screen() {
    @Serializable
    object NameGenerator : Screen()
    @Serializable
    object Settings : Screen()
}

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: Screen = Screen.NameGenerator,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<Screen.NameGenerator> {
            NameGeneratorScreen(
                navigateToSetting = { navController.navigate(Screen.Settings) }
            )
        }
        composable<Screen.Settings> {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}