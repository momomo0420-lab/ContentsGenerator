package com.example.contents_generator.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.contents_generator.data.repositories.user_settings.UserSettingsRepository
import com.example.contents_generator.data.repositories.user_settings.UserSettingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val USER_SETTINGS_NAME = "user_settings"

@Module
@InstallIn(SingletonComponent::class)
object UserSettingsProvidingModule {
    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create {
        context.preferencesDataStoreFile(USER_SETTINGS_NAME)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class UserSettingsBindingModule {
    @Binds
    @Singleton
    abstract fun bindUserSettingsRepository(
        impl: UserSettingsRepositoryImpl
    ): UserSettingsRepository
}
