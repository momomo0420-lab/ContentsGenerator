package com.example.contents_generator.ui.screens.settings

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
 * 設定画面の UI 状態を表します。
 *
 * このデータクラスは、設定画面の現在の状態（保存操作が進行中かどうか、API キー、エラーメッセージなど）を保持します。
 *
 * @property isSaving 保存操作が現在進行中かどうかを示します。
 * デフォルトは `false` です。
 * @property apiKey ユーザーが入力した API キー。
 * API キーが入力されていない場合、またはクリアされている場合は `null` になります。
 * @property errorMessage ユーザーに表示されるエラーメッセージ（ある場合）。
 * エラーがない場合は `null` になります。
 */
data class SettingsUiState(
    val isSaving: Boolean = false,
    val apiKey: String? = null,
    val errorMessage: String? = null,
)

/**
 * 設定画面の ViewModel。
 *
 * この ViewModel は UI の状態を管理し、API キーの取得、更新、保存など、設定に関連するユーザー操作を処理します。
 */
class SettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    /**
     * 指定された値で UI の状態を更新します。
     *
     * この関数は、UI の現在の状態を変更するために使用されます。この関数では、
     * `isSaving`、`apiKey`、`errorMessage` プロパティを個別に、または同時に更新できます。
     * プロパティが指定されていない場合は、現在の値が保持されます。
     *
     * @param isSaving (オプション) 保存操作が進行中かどうかを示すブール値。
     * デフォルトは `_uiState.value.isSaving` の現在の値です。
     * @param apiKey (オプション) API キー。null にすることができます。
     * デフォルトは `_uiState.value.apiKey` の現在の値です。
     * @param errorMessage (オプション) 表示されるエラーメッセージ。null にすることができます。
     * デフォルトは `_uiState.value.errorMessage` の現在の値です。
     */
    private fun updateUiState(
        isSaving: Boolean = _uiState.value.isSaving,
        apiKey: String? = _uiState.value.apiKey,
        errorMessage: String? = _uiState.value.errorMessage,
    ) {
        _uiState.update {
            it.copy(
                isSaving = isSaving,
                apiKey = apiKey,
                errorMessage = errorMessage,
            )
        }
    }

    /**
     * アプリケーションに必要なデータと設定を初期化します。
     *
     * この関数は以下のアクションを実行します。
     * 1. ViewModel のスコープ内でコルーチンを起動します。
     * 2. 現在の設定の取得をシミュレートします (TODO: 実際のロジックに置き換えてください)。
     * 3. IO ディスパッチャで `Thread.sleep()` を使用して 1 秒間実行を一時停止し、
     * ネットワークまたはデータベース操作をシミュレートします。
     * 4. シミュレートされたデータ取得が完了したら、空の API キーを使用して UI の状態を更新します。
     *
     * 注:
     * - `TODO` コメントは、実際の設定を取得するためのロジックを実装する必要があることを示しています。
     * - `Thread.sleep()` 呼び出しはシミュレーションのみを目的としており、本番環境では適切な非同期操作に置き換える必要があります。
     * - この関数は viewModelScope を使用しており、ViewModel がクリアされるとキャンセルされます。
     */
    fun initialize() {
        viewModelScope.launch {
            // TODO: 現在の設定を取得する処理
            withContext(Dispatchers.IO) {
                Thread.sleep(1000)
            }

            updateUiState(apiKey = "")
        }
    }

    /**
     * リクエストに使用する API キーを更新します。
     *
     * この関数は新しい API キーを入力として受け取り、内部状態を更新してこの変更を反映します。
     * その後、UI を更新して新しい API キーを表示します。
     *
     * @param apiKey 使用する新しい API キー。
     */
    fun updateApiKey(apiKey: String) {
        updateUiState(apiKey = apiKey)
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
     * 現在の設定を非同期に保存します。
     *
     * この関数は、アプリケーションの設定を保存するプロセスを開始します。
     * 1 秒の遅延を伴うバックグラウンド保存操作をシミュレートします。
     * 保存中は、保存操作が進行中であることを示すために UI の状態を更新します。
     * シミュレートされた保存操作が完了すると、保存が完了したことを示すために UI の状態を再度更新し、
     * 指定されたコールバック関数を呼び出します。
     *
     * 実際の保存ロジック (現在は `Thread.sleep(1000)` で表されています) は、
     * ファイルやデータベースへの書き込みなど、実際の設定の永続化メカニズムに置き換える必要があります。
     *
     * @param onFinished 保存操作の完了後に呼び出されるラムダ関数です。
     * 指定されていない場合は、デフォルトで空のラムダになります。
     * これは、別の画面への移動や確認メッセージの表示など、保存後のアクションを実行するために使用できます。
     */
    fun saveSettings(
        onFinished: () -> Unit = {}
    ) {
        viewModelScope.launch {
            updateUiState(isSaving = true)

            //TODO: 保存処理
            withContext(Dispatchers.IO) {
                Thread.sleep(1000)
            }

            updateUiState(isSaving = false)

            onFinished()
        }
    }
}