package com.bmstu.iu3.features.schedule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmstu.iu3.core.data.api.CatalogRepository
import com.bmstu.iu3.core.data.api.CatalogState
import com.bmstu.iu3.core.data.api.UserRepository
import com.bmstu.iu3.core.model.EventLogic
import com.bmstu.iu3.core.model.FacultyEvent
import com.bmstu.iu3.core.model.Lesson
import com.bmstu.iu3.core.model.StudyCalendar
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: CatalogRepository,
    private val users: UserRepository,
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

    val catalog = repository.state
    val profile = users.profile.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    fun lessons(date: java.time.LocalDate): List<Lesson> {
        val ready = catalog.value as? CatalogState.Ready ?: return emptyList()
        return StudyCalendar.lessons(ready.catalog, profile.value ?: return emptyList(), date)
    }

    fun upcoming(): List<FacultyEvent> =
        (catalog.value as? CatalogState.Ready)?.let { EventLogic.upcoming(it.catalog.events) }
            .orEmpty()

    fun groups(query: String): List<String> =
        (catalog.value as? CatalogState.Ready)?.catalog?.timetable?.groups?.keys.orEmpty()
            .filter { it.contains(query, true) }.sorted()

    fun selectGroup(group: String) {
        change {
            val current = users.profile.first(); users.save(
            current.copy(
                group = group,
                teacher = ""
            )
        )
        }
    }
}
