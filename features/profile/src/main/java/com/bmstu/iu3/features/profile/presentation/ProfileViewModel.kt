package com.bmstu.iu3.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmstu.iu3.core.common.di.DefaultDispatcher
import com.bmstu.iu3.core.data.api.CatalogRepository
import com.bmstu.iu3.core.data.api.CatalogState
import com.bmstu.iu3.core.data.api.UserRepository
import com.bmstu.iu3.core.model.AcademicTime
import com.bmstu.iu3.core.model.Profile
import com.bmstu.iu3.core.model.ReminderPlanner
import com.bmstu.iu3.core.model.ReminderSettings
import com.bmstu.iu3.core.model.StudyCalendar
import com.bmstu.iu3.core.notifications.ReminderFormatter
import com.bmstu.iu3.core.notifications.ReminderScheduler
import com.bmstu.iu3.core.notifications.SynchronizeReminders
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: CatalogRepository,
    private val users: UserRepository,
    private val scheduler: ReminderScheduler,
    private val formatter: ReminderFormatter,
    private val synchronize: SynchronizeReminders,
    private val clock: java.time.Clock,
    @DefaultDispatcher private val computationDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val writeError = MutableStateFlow(false)
    val hasWriteError = writeError.asStateFlow()
    fun clearWriteError() {
        writeError.value = false
    }

    private fun change(action: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                action()
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                writeError.value = true
            }
        }
    }

    val uiState =
        combine(repository.state, users.profile, users.reminders) { state, profile, settings ->
            (state as? CatalogState.Ready)?.let { ready ->
                val now = clock.instant()
                val today = now.atZone(AcademicTime.Zone).toLocalDate()
                val year =
                    if (today > StudyCalendar.alumniMeeting(today.year)) today.year + 1 else today.year
                val planned = ReminderPlanner.plan(ready.catalog, profile, settings, now = now)
                    .map { it.copy(title = formatter.title(it), detail = formatter.body(it)) }
                ProfileUiState(
                    catalog = ready.catalog,
                    profile = profile,
                    reminders = settings,
                    planned = planned,
                    alumniMeetingYear = year
                )
            }
        }.flowOn(computationDispatcher)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun save(profile: Profile) {
        change { users.save(profile) }
    }

    fun save(settings: ReminderSettings) {
        change { users.save(settings) }
    }

    fun reset() {
        change { users.reset() }
    }

    fun photo(source: String) {
        change { if (!users.updatePhoto(source)) writeError.value = true }
    }

    fun canScheduleExact(): Boolean = scheduler.canScheduleExact()
    fun reschedule() {
        change { synchronize() }
    }
}
