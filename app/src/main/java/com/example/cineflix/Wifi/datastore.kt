package com.example.cineflix.Wifi

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore(name = "app_settings")

object SettingsDataStore {
    private val WIFI_ONLY_KEY = booleanPreferencesKey("wifi_only")
    private val NOTIFICATION_KEY = booleanPreferencesKey("notifications_enabled")

    suspend fun saveWiFiOnlySetting(context: Context, enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[WIFI_ONLY_KEY] = enabled }
    }

    fun getWiFiOnlySetting(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { prefs -> prefs[WIFI_ONLY_KEY] ?: false }
    }

    suspend fun saveNotificationSetting(context: Context, enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[NOTIFICATION_KEY] = enabled }
    }

    fun getNotificationSetting(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { prefs -> prefs[NOTIFICATION_KEY] ?: false }
    }
}