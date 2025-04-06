package org.project.we3.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "admin_prefs")

class AdminPreferences(private val context: Context) {

    companion object {
        private val IS_ADMIN_LOGGED_IN = booleanPreferencesKey("is_admin_logged_in")
    }

    // Save admin login state
    suspend fun setAdminLoggedIn(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_ADMIN_LOGGED_IN] = isLoggedIn
        }
    }

    // Read admin login state
    val isAdminLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_ADMIN_LOGGED_IN] ?: false }
}
