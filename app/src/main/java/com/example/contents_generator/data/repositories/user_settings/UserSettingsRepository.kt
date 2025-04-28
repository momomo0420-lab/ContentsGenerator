package com.example.contents_generator.data.repositories.user_settings

import com.example.contents_generator.data.models.UserSettings

/**
 * UserSettingsRepository インターフェースは、ユーザー設定を管理するための規約を定義します。
 * ユーザー固有の設定を取得および保存するためのメソッドを提供します。
 */
interface UserSettingsRepository {
    /**
     * ユーザーの設定を取得します。
     *
     * この関数は、データソースからユーザーの設定と構成を取得します。
     * これはサスペンド関数であり、長時間実行される操作（ネットワークリクエストやデータベースクエリなど）
     * を実行する可能性があるため、コルーチン内から呼び出す必要があります。
     *
     * @return [UserSettings] ユーザーの設定。
     * @throws Exception 設定の取得中にエラーが発生した場合
     * （例：ネットワークエラー、データベースエラー）。必要に応じて、より具体的な例外を使用することを検討してください。
     */
    suspend fun getUserSettings(): UserSettings
    /**
     * ユーザーの設定を永続ストレージに保存します。
     *
     * この関数は、ユーザーの設定を安全に保存し、
     * 後続のセッションで取得して使用できるようにします。
     *
     * @param apiKey 保存する API キー。空または null にすることはできません。
     * @throws Exception 設定の保存中にエラーが発生した場合
     */
    suspend fun saveUserSettings(apiKey: String)
}

