package com.bmstu.iu3.core.data.source

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bmstu.iu3.core.model.Profile
import com.bmstu.iu3.core.model.ReminderSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

private const val STORE_NAME = "user"
private val Context.userStore by preferencesDataStore(STORE_NAME)

@Singleton
internal class UserPreferencesDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json,
) {
    private val profileKey = stringPreferencesKey(PROFILE_KEY)
    private val recentKey = stringPreferencesKey(RECENT_KEY)
    private val reminderKey = stringPreferencesKey(REMINDERS_KEY)
    private val preferences = context.userStore.data.catch { error ->
        if (error is IOException) emit(emptyPreferences()) else throw error
    }
    val profile: Flow<Profile> = preferences.map { prefs ->
        prefs[profileKey]?.let { runCatching { json.decodeFromString<Profile>(it) }.getOrNull() }
            ?: Profile()
    }.distinctUntilChanged()
    val recent: Flow<List<String>> = preferences.map(::readRecent).distinctUntilChanged()
    val reminders: Flow<ReminderSettings> = preferences.map { prefs ->
        prefs[reminderKey]?.let { runCatching { json.decodeFromString<ReminderSettings>(it) }.getOrNull() }
            ?: ReminderSettings()
    }.distinctUntilChanged()

    suspend fun save(profile: Profile) {
        context.userStore.edit {
            it[profileKey] = json.encodeToString(Profile.serializer(), profile)
        }
    }

    suspend fun updateRecent(transform: (List<String>) -> List<String>) {
        context.userStore.edit { prefs ->
            prefs[recentKey] = json.encodeToString(
                ListSerializer(String.serializer()),
                transform(readRecent(prefs))
            )
        }
    }

    suspend fun save(settings: ReminderSettings) {
        context.userStore.edit {
            it[reminderKey] = json.encodeToString(ReminderSettings.serializer(), settings)
        }
    }

    suspend fun reset() {
        context.userStore.edit { it.clear() }
    }

    private fun readRecent(prefs: Preferences): List<String> = prefs[recentKey]?.let {
        runCatching { json.decodeFromString<List<String>>(it) }.getOrNull()
    }.orEmpty()

    private companion object {
        const val PROFILE_KEY = "profile"
        const val RECENT_KEY = "recent"
        const val REMINDERS_KEY = "reminders"
    }
}
