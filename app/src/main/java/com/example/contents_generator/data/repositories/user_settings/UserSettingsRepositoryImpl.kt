package com.example.contents_generator.data.repositories.user_settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.contents_generator.data.models.UserSettings
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * [UserSettingsRepository]インターフェースの実装。
 * このクラスは、Android の DataStore と設定を使用して、ユーザー設定（特に API キー）の保存と取得を処理します。
 *
 * @property dataStore ユーザー設定の永続化に使用される DataStore インスタンス。
 */
class UserSettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
): UserSettingsRepository {
    private object PreferencesKey {
        val API_KEY = stringPreferencesKey("api_key")
    }

    override suspend fun getUserSettings(): UserSettings = dataStore.data.map { preferences ->
        UserSettings(
            apiKey = preferences[PreferencesKey.API_KEY] ?: "",
        )
    }.first()

    override suspend fun saveUserSettings(
        apiKey: String,
    ) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.API_KEY] = apiKey
        }
    }
}
