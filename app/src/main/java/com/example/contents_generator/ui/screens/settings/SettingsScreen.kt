package com.example.contents_generator.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * アプリケーション設定を表示する画面。現在、この画面はプレースホルダであり、
 * 「戻る」および「保存」機能がまだ実装されていないことを示す Toast メッセージが表示されます。
 *
 * @param modifier 画面のルート要素に適用する修飾子。
 * @param onBack ユーザーが戻ろうとしたときに呼び出されるコールバック。
 */
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: SettingsViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.initialize()
    }

    SettingsScreen(
        modifier = modifier.fillMaxSize(),
        uiState = uiState,
        onApiKeyChange = viewModel::updateApiKey,
        onBack = onBack,
        onSave = { viewModel.saveSettings(onBack) },
        onRetry = viewModel::retry,
    )
}

/**
 * アプリケーション設定画面。
 *
 * この画面では、ユーザーは API キーなどのアプリケーション設定を表示および変更できます。
 *
 * @param modifier このコンポーザブルに適用する修飾子。
 * @param uiState UIの現在の状態。
 * @param onApiKeyChange ユーザーが API キーを変更したときに呼び出されるコールバック。新しい API キーをパラメーターとして受け取ります。
 * @param onBack ユーザーが戻るボタンを押したときに呼び出されるコールバック。
 * @param onSave ユーザーが保存ボタンを押したときに呼び出されるコールバック。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    uiState: SettingsUiState,
    onApiKeyChange: (String) -> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onRetry: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text(text = "設定") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "戻る",
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val contentModifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(8.dp)

        when {
            uiState.errorMessage != null -> {
                ErrorContent(
                    modifier = contentModifier,
                    message = uiState.errorMessage,
                    onRetry = onRetry,
                )
            }
            uiState.apiKey == null -> {
                InitContent(contentModifier)
            }
            else -> {
                MainContent(
                    modifier = contentModifier,
                    apiKey = uiState.apiKey,
                    onApiKeyChange = onApiKeyChange,
                    onSave = onSave,
                    isSaving = uiState.isSaving,
                )
            }
        }
    }
}

/**
 * 中央揃えの円形のプログレスバーを表示します。
 *
 * このコンポーザブルは、データの読み込みやバックグラウンドタスクの実行など、
 * プロセスが進行中であることを視覚的に示すために使用されます。
 * `modifier` によって提供される利用可能なスペースの中央に、円形のプログレスバーをレンダリングします。
 *
 * @param modifier レイアウトに適用する修飾子。これにより、サイズ、パディング、
 * 背景などのプロパティを制御できます。Box コンポーザブルは、この修飾子によって提供されるサイズ制約に従います。
 */
@Composable
private fun InitContent(
    modifier: Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

/**
 * エラーメッセージと再試行ボタンを表示します。
 *
 * このコンポーザブルは、ユーザーにエラー状態を表示するために使用され、
 * 明確なメッセージと、失敗した操作を再試行するためのアクションを提供します。
 *
 * @param modifier エラーコンテンツのスタイルとレイアウトの修飾子。
 * @param message 表示されるエラーメッセージ。
 * @param onRetry 再試行ボタンがクリックされたときに呼び出されるコールバック関数。
 */
@Composable
private fun ErrorContent(
    modifier: Modifier,
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(message)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text(text = "再試行")
        }
    }
}

/**
 * 設定コンテンツを表示し、ユーザーがAPIキーを入力して保存できるようにするコンポーザブル関数。
 *
 * @param modifier レイアウトの修飾子。
 * @param apiKey 現在のAPIキーの値。
 * @param onApiKeyChange APIキーの入力内容が変更されたときに呼び出されるコールバック関数。新しいAPIキーの値をパラメーターとして受け取ります。
 * @param onSave 「保存」ボタンがクリックされたときに呼び出されるコールバック関数。
 */
@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    isSaving: Boolean,
    apiKey: String,
    onApiKeyChange: (String) -> Unit,
    onSave: () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "APIキー") },
            placeholder = { Text(text = "APIキーを入力してください") },
            value = apiKey,
            onValueChange = onApiKeyChange,
            singleLine = true,
            trailingIcon = {
                IconButton(
                    enabled = apiKey.isNotEmpty(),
                    onClick = { onApiKeyChange("") },
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (isSaving) {
            CircularProgressIndicator()
        } else {
            Button(
                enabled = apiKey.isNotEmpty(),
                onClick = onSave,
            ) {
                Text(text = "保存")
            }
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen(
        uiState = SettingsUiState(
            apiKey = "test",
        ),
        onApiKeyChange = {},
        onBack = {},
        onSave = {},
        onRetry = {},
    )
}
