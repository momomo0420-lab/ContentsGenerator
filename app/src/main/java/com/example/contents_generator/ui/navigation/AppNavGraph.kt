package com.example.contents_generator.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.contents_generator.ui.screens.name_generator.NameGeneratorScreen
import com.example.contents_generator.ui.screens.name_generator.NameGeneratorViewModel
import com.example.contents_generator.ui.screens.settings.SettingsScreen
import com.example.contents_generator.ui.screens.settings.SettingsViewModel
import kotlinx.serialization.Serializable

/**
 * アプリケーション内の様々な画面を表すシールクラス。
 *
 * このクラスは、シールクラス構造を使用して、画面タイプの限定された階層を定義します。
 * このアプローチにより、様々な画面を処理する際に徹底的なチェックが可能になり、
 * 考えられるすべての画面タイプが考慮されることが保証されます。
 *
 * 各画面は、[Screen] クラスを拡張するオブジェクトとして表現されます。
 */
sealed class Screen() {
    @Serializable
    object NameGenerator : Screen()
    @Serializable
    object Settings : Screen()
}


/**
 * [AppNavGraph] は、アプリケーションのナビゲーショングラフを定義する Composable 関数です。
 *
 * [NavHost] を使用して、アプリ内の異なる画面間のナビゲーションを管理します。
 * この関数はナビゲーション構造を設定し、各 [Screen] のデスティネーションを対応する Composable 画面関数に関連付けます。
 *
 * @param modifier [NavHost] に適用する修飾子。デフォルトは [Modifier] です。
 * @param navController ナビゲーション状態を管理する [NavHostController]。
 * デフォルトは、新しく作成された [rememberNavController] です。
 * @param startDestination ナビゲーショングラフの初期画面デスティネーション。
 * デフォルトは [Screen.NameGenerator] です。
 */
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
            val viewModel: NameGeneratorViewModel = hiltViewModel()
            NameGeneratorScreen(
                viewModel = viewModel,
                navigateToSetting = { navController.navigate(Screen.Settings) }
            )
        }
        composable<Screen.Settings> {
            val viewModel: SettingsViewModel = hiltViewModel()
            SettingsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}