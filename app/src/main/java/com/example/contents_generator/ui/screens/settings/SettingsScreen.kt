package com.example.contents_generator.ui.screens.settings

import android.widget.Toast
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
) {
    var apiKey by remember { mutableStateOf("") }
    val context = LocalContext.current

    SettingsScreen(
        modifier = modifier.fillMaxSize(),
        onBack = onBack,
        onSave = {
            Toast.makeText(
                context,
                "保存機能は未実装です。",
                Toast.LENGTH_SHORT
            ).show()

            onBack()
        },
        apiKey = apiKey,
        onApiKeyChange = { apiKey = it },
    )
}

/**
 * アプリケーション設定画面。
 *
 * この画面では、ユーザーは API キーなどのアプリケーション設定を表示および変更できます。
 *
 * @param modifier このコンポーザブルに適用する修飾子。
 * @param apiKey 現在の API キー。
 * @param onApiKeyChange ユーザーが API キーを変更したときに呼び出されるコールバック。新しい API キーをパラメーターとして受け取ります。
 * @param onBack ユーザーが戻るボタンを押したときに呼び出されるコールバック。
 * @param onSave ユーザーが保存ボタンを押したときに呼び出されるコールバック。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    apiKey: String,
    onApiKeyChange: (String) -> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit,
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
        SettingsContent(
            modifier = Modifier.padding(innerPadding).padding(8.dp),
            apiKey = apiKey,
            onApiKeyChange = onApiKeyChange,
            onSave = onSave,
        )
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
fun SettingsContent(
    modifier: Modifier = Modifier,
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

        Button(
            enabled = apiKey.isNotEmpty(),
            onClick = onSave,
        ) {
            Text(text = "保存")
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen(
        onBack = {},
    )
}
