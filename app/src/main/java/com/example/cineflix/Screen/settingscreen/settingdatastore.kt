package com.example.cineflix.Screen.settingscreen

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// SettingsDataStore.kt
object SettingsDataStore {
    private val Context.dataStore by preferencesDataStore(name = "app_settings")

    val NOTIFICATION_TOGGLE = booleanPreferencesKey("allow_notifications")
    val WIFI_ONLY_TOGGLE = booleanPreferencesKey("wifi_only")

    suspend fun saveNotificationSetting(context: Context, value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[NOTIFICATION_TOGGLE] = value
        }
    }

    suspend fun saveWiFiOnlySetting(context: Context, value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[WIFI_ONLY_TOGGLE] = value
        }
    }

    fun getNotificationSetting(context: Context): Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[NOTIFICATION_TOGGLE] ?: false }

    fun getWiFiOnlySetting(context: Context): Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[WIFI_ONLY_TOGGLE] ?: false }
}
