package com.bmstu.iu3.core.data.api

import com.bmstu.iu3.core.model.Profile
import com.bmstu.iu3.core.model.ReminderSettings
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val profile: Flow<Profile>
    val recent: Flow<List<String>>
    val reminders: Flow<ReminderSettings>
    suspend fun save(profile: Profile)
    suspend fun save(settings: ReminderSettings)
    suspend fun remember(query: String)
    suspend fun forget(query: String)
    suspend fun updatePhoto(source: String): Boolean
    suspend fun reset()
}
