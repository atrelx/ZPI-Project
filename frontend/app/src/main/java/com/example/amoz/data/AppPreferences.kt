package com.example.amoz.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreferences(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("app_preferences")

        private val CURRENCY_KEY = stringPreferencesKey("currency")
    }

    val currency: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[CURRENCY_KEY] ?: "USD" }


    suspend fun saveCurrency(currencyCode: String) {
        context.dataStore.edit { preferences ->
            preferences[CURRENCY_KEY] = currencyCode
        }
    }
}
