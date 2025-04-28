package com.example.contents_generator.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contents_generator.data.repositories.user_settings.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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
 * この ViewModel は設定画面の UI 状態を管理し、API キーの取得、更新、保存などの設定に関連するユーザー操作を処理します。
 * また、データ取得や保存時のエラーハンドリングも行います。
 *
 * @property userSettingsRepository ユーザー設定のデータアクセスを提供するリポジトリ。
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    val userSettingsRepository: UserSettingsRepository,
) : ViewModel() {
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
     * リポジトリからユーザー設定を取得して UI の状態を初期化します。
     *
     * 主な目的は、初期設定データ（特に API キー）を読み込み、
     * このプロセス中に発生する可能性のあるエラーを適切に処理することです。
     */
    fun initialize() {
        viewModelScope.launch {
            try {
                val userSettings = userSettingsRepository.getUserSettings()
                updateUiState(apiKey = userSettings.apiKey)
            } catch (e: Exception) {
                updateUiState(errorMessage = e.message)
            }
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
     * API キーを含む現在のユーザー設定を保存します。
     *
     * 保存プロセスが成功した場合、`onFinished` コールバックを呼び出します。
     * 保存中に例外が発生した場合、エラーメッセージで UI の状態を更新します。
     *
     * @param onFinished 設定が正常に保存された後に呼び出されるラムダ関数です。
     * デフォルトは空のラムダです。
     */
    fun saveSettings(
        onFinished: () -> Unit = {}
    ) {
        viewModelScope.launch {
            updateUiState(isSaving = true)

            try {
                userSettingsRepository.saveUserSettings(
                    apiKey = uiState.value.apiKey ?: "",
                )
                updateUiState(isSaving = false)
                onFinished()
            } catch (e: Exception) {
                updateUiState(
                    isSaving = false,
                    errorMessage = e.message
                )
            }
        }
    }
}