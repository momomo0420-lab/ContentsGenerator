package com.example.contents_generator.ui.screens.name_generator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 名前生成画面の UI 状態を表します。
 *
 * このデータクラスは、ユーザーが入力したプロンプト、生成されたテキスト、システムが現在名前を生成中かどうか、
 * エラーメッセージなど、UI の現在の状態を保持します。
 *
 * @property prompt 名前を生成するためにユーザーが入力したテキストプロンプト。デフォルトは空の文字列です。
 * @property generatedText 生成された名前のテキスト。デフォルトは空の文字列です。
 * @property isGenerating 名前生成器が現在リクエストを処理中かどうかを示します。デフォルトは false です。
 * @property errorMessage ユーザーに表示されるオプションのエラーメッセージ。エラーがない場合は null です。
 */
data class NameGeneratorUiState(
    val prompt: String = "",
    val generatedText: String = "",
    val isGenerating: Boolean = false,
    val errorMessage: String? = null,
)

/**
 * [NameGeneratorViewModel] は、名前生成機能の状態とロジックを管理する ViewModel です。
 *
 * [NameGeneratorUiState] の [StateFlow] を介して UI とやり取りします。これは、UI の現在の状態を表します。
 *
 * この ViewModel は次の処理を行います。
 * - ユーザーの入力プロンプトの更新。
 * - 名前生成プロセスのトリガー。
 * - 生成中の読み込み状態の管理。
 * - 生成された名前の表示。
 * - エラーメッセージの処理とクリア。
 * - 以前のエラーをクリアして操作を再試行。
 */
class NameGeneratorViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<NameGeneratorUiState>(NameGeneratorUiState())
    val uiState: StateFlow<NameGeneratorUiState> = _uiState.asStateFlow()

    /**
     * 指定された値で UI 状態を更新します。
     *
     * この関数を使用すると、すべてのフィールドを指定することなく、UI 状態の特定の部分を更新できます。
     * パラメータが明示的に指定されていない場合は、現在の UI 状態のデフォルト値が使用されます。
     *
     * @param prompt 表示される新しいプロンプト。デフォルトは、UI 状態の現在のプロンプトです。
     * @param generatedText 表示される新しい生成テキスト。デフォルトは、UI 状態の現在の生成テキストです。
     * @param isGenerating システムが現在テキストを生成中かどうかを示すブール値。
     * デフォルトは、UI 状態の現在の isGenerating 値です。
     * @param errorMessage 表示されるオプションのエラーメッセージ。デフォルトは、
     * UI 状態の現在のエラーメッセージです（null の場合もあります）。
     */
    private fun updateUiState(
        prompt: String = uiState.value.prompt,
        generatedText: String = uiState.value.generatedText,
        isGenerating: Boolean = uiState.value.isGenerating,
        errorMessage: String? = uiState.value.errorMessage,
    ) {
        _uiState.update {
            it.copy(
                prompt = prompt,
                generatedText = generatedText,
                isGenerating = isGenerating,
                errorMessage = errorMessage,
            )
        }
    }

    /**
     * UI 状態における現在のプロンプトを更新します。
     *
     * この関数は、新しいプロンプトを表す文字列を受け取り、
     * それを使ってアプリケーションの UI 状態を更新します。
     * これにより、以前のプロンプトが指定されたプロンプトに置き換えられます。
     *
     * @param prompt UI に表示される新しいプロンプト文字列。
     */
    fun updatePrompt(prompt: String) {
        updateUiState(prompt = prompt)
    }

    /**
     * 前回エラーとなった操作を再試行します。
     *
     * この関数は、UI 状態にある既存のエラーメッセージをクリアし、操作を再度実行することを通知します。
     * 通常、この関数は、ユーザーが「再試行」ボタンなどの UI 要素を操作したときに呼び出されます。
     */
    fun retry() {
        updateUiState(errorMessage = null)
    }


    /**
     * 名前を非同期に生成します。
     *
     * この関数は名前生成プロセスを開始します。UI の状態を更新し、名前生成中であることを示します (`isGenerating = true`)。
     * その後、`Dispatchers.Default` を使用してバックグラウンドスレッドで実際の名前生成を実行します。
     * 名前が生成された後 (またはこの場合は、シミュレートされた遅延の後)、
     * 生成された名前 (`generatedText`) で UI の状態を更新し、`isGenerating` を `false` に設定します。
     *
     * 現在、名前生成は 5 秒の遅延と固定文字列でシミュレートされています。
     * 実際の実装では、これを名前生成の実際のロジックに置き換える必要があります。
     *
     * UI の状態は `updateUiState` 関数を使用して更新されます。
     *
     * この関数は `viewModelScope` を使用して、ViewModel がクリアされたときにコルーチンが自動的にキャンセルされるようにします。
     * *
     * **注:** これはプレースホルダ実装です。「TODO」コメントは、実際の名前生成ロジックを実装する場所を示しています。
     */
    fun generateName() {
        viewModelScope.launch {
            updateUiState(isGenerating = true)

            //TODO: 名前生成処理をここに記述する
            val response = withContext(Dispatchers.Default) {
                Thread.sleep(5000)
                "これはテスト用の文字列です。実際は生成した名前をここに表示します。"
            }

            updateUiState(
                generatedText = response,
                isGenerating = false,
            )
        }
    }
}
