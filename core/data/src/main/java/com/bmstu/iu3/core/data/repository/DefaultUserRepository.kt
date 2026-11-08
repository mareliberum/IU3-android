package com.bmstu.iu3.core.data.repository

import android.util.Log
import com.bmstu.iu3.core.common.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import com.bmstu.iu3.core.data.api.UserRepository
import com.bmstu.iu3.core.data.source.ProfilePhotoDataSource
import com.bmstu.iu3.core.data.source.UserPreferencesDataSource
import com.bmstu.iu3.core.model.Role
import com.bmstu.iu3.core.model.Profile
import com.bmstu.iu3.core.model.ReminderSettings
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Singleton
internal class DefaultUserRepository @Inject constructor(
    private val preferences: UserPreferencesDataSource,
    private val photos: ProfilePhotoDataSource,
    @ApplicationScope private val applicationScope: CoroutineScope,
) : UserRepository {
    private val mutation = Mutex()
    override val profile = preferences.profile
    override val recent = preferences.recent
    override val reminders = preferences.reminders
    override suspend fun save(profile: Profile) =
        commit { preferences.save(normalize(profile)) }

    override suspend fun save(settings: ReminderSettings) =
        commit { preferences.save(settings) }

    override suspend fun remember(query: String) = commit {
        val clean = query.trim()
        if (clean.isBlank()) return@commit
        preferences.updateRecent { old ->
            (listOf(clean) + old.filterNot {
                it.equals(
                    clean,
                    true
                )
            }).take(RECENT_LIMIT)
        }
    }

    override suspend fun forget(query: String) =
        commit { preferences.updateRecent { it - query } }

    override suspend fun reset() = commit {
        preferences.reset()
        photos.delete()
    }

    override suspend fun updatePhoto(source: String): Boolean = commit {
        val current = profile.first()
        if (!current.completed) return@commit false
        try {
            val photo = photos.import(source)
            preferences.save(normalize(current.copy(photo = photo)))
            true
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (_: Exception) {
            false
        }
    }

    private suspend fun <T> commit(action: suspend () -> T): T =
        applicationScope.async(start = CoroutineStart.UNDISPATCHED) {
            try {
                mutation.withLock { action() }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                Log.w(TAG, WRITE_FAILURE, error)
                throw error
            }
        }.await()

    private fun normalize(profile: Profile): Profile = profile.copy(
        name = profile.name.trim(),
        phone = profile.phone.trim(),
        email = profile.email.trim(),
        group = profile.group.takeIf { profile.role == Role.Student }.orEmpty(),
        teacher = profile.teacher.takeIf { profile.role == Role.Teacher }.orEmpty(),
    )

    private companion object {
        const val RECENT_LIMIT = 10
        const val TAG = "UserRepository"
        const val WRITE_FAILURE = "Cannot persist user data"
    }
}
