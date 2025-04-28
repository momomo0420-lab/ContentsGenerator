package com.example.contents_generator.data.models

/**
 * アプリケーションにおけるユーザー設定を表します。
 *
 * このデータクラスは、API キーやその他の個人設定など、ユーザー固有のさまざまな設定オプションを保持します。
 *
 * @property apiKey 外部サービスとの認証に使用される API キー。
 * 指定されていない場合は、デフォルトで空の文字列になります。
 */
data class UserSettings(
    val apiKey: String = "",
)
