package com.example.contents_generator.ui.screens.name_generator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * 名前を生成するための画面を表す、コンポーザブル関数です。
 *
 * この画面では、ユーザーは指定されたプロンプトに基づいて名前を生成できます。
 * 生成されたテキストを表示し、設定や生成ボタンによるユーザーインタラクションを処理します。
 *
 * @param modifier スタイルとレイアウト調整のための修飾子。デフォルトは空の修飾子です。
 * @param navigateToSetting 設定画面へ遷移するためのコールバック関数。
 *                          この関数は設定ボタンがクリックされた際に呼び出されます。
 *
 */
@Composable
fun NameGeneratorScreen(
    modifier: Modifier = Modifier,
    navigateToSetting: () -> Unit,
    viewModel: NameGeneratorViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NameGeneratorScreen(
        modifier = modifier.fillMaxSize(),
        uiState = uiState,
        onSettings = navigateToSetting,
        onPromptChange = viewModel::updatePrompt,
        onGenerate = viewModel::generateName,
        onRetry = viewModel::retry,
    )
}

/**
 * 名前生成画面用のコンポーザブル関数。
 *
 * この画面には、ユーザー入力用のテキストフィールド（プロンプト）、名前生成をトリガーするボタン、
 * 生成されたテキストを表示する領域があります。また、上部のアプリバーには設定ボタンも表示されます。
 *
 * @param modifier レイアウトの修飾子。
 * @param uiState UIの現在の状態。
 * @param onSettings 設定ボタンがクリックされたときに呼び出されるコールバック関数。
 * @param onPromptChange プロンプトテキストが変更されたときに呼び出されるコールバック関数。
 * 新しいプロンプトテキストをパラメーターとして受け取ります。
 * @param onGenerate 生成ボタンがクリックされたときに呼び出されるコールバック関数。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameGeneratorScreen(
    modifier: Modifier = Modifier,
    uiState: NameGeneratorUiState,
    onSettings: () -> Unit,
    onPromptChange: (String) -> Unit,
    onGenerate: () -> Unit,
    onRetry: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text(text = "Name Generator") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                actions = {
                    IconButton(onClick = onSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "設定",
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
            else -> {
                MainContent(
                    modifier = contentModifier,
                    isGenerating = uiState.isGenerating,
                    prompt = uiState.prompt,
                    onPromptChange = onPromptChange,
                    onGenerate = onGenerate,
                    generatedText = uiState.generatedText,
                )
            }
        }
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
 * ユーザー指定のプロンプトに基づいて名前を生成するためのコンポーザブル関数。
 *
 * @param modifier このレイアウトに適用する修飾子。
 * @param prompt ユーザーが入力した現在のテキスト。希望する名前の特徴を表します。
 * @param onPromptChange ユーザーが入力テキストを変更したときに呼び出されるコールバック関数。
 * 新しいプロンプトテキストをパラメーターとして受け取ります。
 * @param onGenerate ユーザーが「生成」ボタンを押したときに呼び出されるコールバック関数。
 * @param generatedText プロンプトに基づいて生成された名前を表すテキスト。ユーザーに表示されます。
 */
@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    isGenerating: Boolean,
    prompt: String,
    onPromptChange: (String) -> Unit,
    onGenerate: () -> Unit,
    generatedText: String,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "どんな条件で名前を作成しますか？") },
            value = prompt,
            onValueChange = onPromptChange,
            minLines = 5,
            trailingIcon = {
                IconButton(
                    enabled = prompt.isNotEmpty(),
                    onClick = { onPromptChange("") },
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                    )
                }
            }
        )

        if(isGenerating) {
            CircularProgressIndicator()
        } else {
            Button(
                enabled = prompt.isNotEmpty(),
                onClick = onGenerate,
            ) {
                Text(text = "生成")
            }
        }

        HorizontalDivider(
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.outline,
        )

        Text(
            text = generatedText,
        )
    }
}

@Preview
@Composable
private fun MainPreview() {
    NameGeneratorScreen(
        uiState = NameGeneratorUiState(
            prompt = "プロンプト",
            generatedText = "生成されたテキスト",
        ),
        onSettings = {},
        onPromptChange = {},
        onGenerate = {},
        onRetry = {},
    )
}

@Preview
@Composable
private fun ErrorPreview() {
    NameGeneratorScreen(
        uiState = NameGeneratorUiState(
            errorMessage = "エラーメッセージ",
        ),
        onSettings = {},
        onPromptChange = {},
        onGenerate = {},
        onRetry = {},
    )
}