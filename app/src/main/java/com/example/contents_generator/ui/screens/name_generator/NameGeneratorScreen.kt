package com.example.contents_generator.ui.screens.name_generator

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp



/**
 * 名前を生成するための画面を表す、コンポーザブル関数です。
 *
 * この画面では、ユーザーは指定されたプロンプトに基づいて名前を生成できます。
 * 生成されたテキストを表示し、設定や生成ボタンによるユーザーインタラクションを処理します。
 *
 * @param modifier スタイルとレイアウト調整のための修飾子。デフォルトは空の修飾子です。
 */
@Composable
fun NameGeneratorScreen(
    modifier: Modifier = Modifier,
) {
    var generatedText by remember { mutableStateOf("") }
    val context = LocalContext.current

    NameGeneratorScreen(
        modifier = modifier.fillMaxWidth(),
        onSettings = {
            Toast.makeText(
                context,
                "設定機能は未実装です。",
                Toast.LENGTH_SHORT
            ).show()
        },
        onGenerate = { namePrompt ->
            generatedText = namePrompt
        },
        generatedText = generatedText
    )
}

/**
 * 名前生成画面用のコンポーザブル関数。
 *
 * この画面には、設定アイコンとメインコンテンツ領域を含むトップアプリバーが表示されます。
 * メインコンテンツ領域には、テキスト入力フィールドと名前生成ボタンがあります。生成された名前は下に表示されます。
 *
 * @param modifier 画面レイアウトの修飾子。デフォルトは [Modifier] です。
 * @param onSettings トップアプリバーの設定アイコンがクリックされたときに呼び出されるコールバック関数。
 * @param onGenerate 生成ボタンがクリックされたときに呼び出されるコールバック関数。
 * 名前生成時にユーザーが入力する文字列パラメータを受け取ります。
 * @param generatedText 生成された名前として表示されるテキスト。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameGeneratorScreen(
    modifier: Modifier = Modifier,
    onSettings: () -> Unit,
    onGenerate: (String) -> Unit,
    generatedText: String,
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
        NameGeneratorContent(
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp),
            onGenerate = onGenerate,
            generatedText = generatedText,
        )
    }
}


/**
 * 名前生成インターフェースを表示するコンポーザブル関数。
 *
 * ユーザーは、名前生成のプロンプトを入力し、生成をトリガーし、生成されたテキストを表示できます。
 *
 * @param modifier コンテンツのレイアウトと外観をカスタマイズするための修飾子。
 * @param onGenerate 「生成」ボタンがクリックされたときに呼び出されるコールバック関数。
 * ユーザーのプロンプトを文字列として受け取ります。
 * @param generatedText 名前生成プロセスによって生成され、UIに表示されるテキスト。
 */
@Composable
fun NameGeneratorContent(
    modifier: Modifier = Modifier,
    onGenerate: (String) -> Unit,
    generatedText: String,
) {
    var namePrompt by remember { mutableStateOf("") }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "どんな条件で名前を作成しますか？") },
            value = namePrompt,
            onValueChange = { namePrompt = it },
            minLines = 5,
            trailingIcon = {
                IconButton(
                    enabled = namePrompt.isNotEmpty(),
                    onClick = { namePrompt = "" },
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                    )
                }
            }
        )

        Button(
            enabled = namePrompt.isNotEmpty(),
            onClick = { onGenerate(namePrompt) },
        ) {
            Text(text = "生成")
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
private fun NameGeneratorScreenPreview() {
    NameGeneratorScreen()
}