package com.bmstu.iu3.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmstu.iu3.core.common.di.DefaultDispatcher
import com.bmstu.iu3.core.data.api.CatalogRepository
import com.bmstu.iu3.core.data.api.CatalogState
import com.bmstu.iu3.core.data.api.UserRepository
import com.bmstu.iu3.core.model.AcademicTime
import com.bmstu.iu3.core.model.EventLogic
import com.bmstu.iu3.core.model.StudyCalendar
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CatalogRepository,
    private val users: UserRepository,
    private val clock: java.time.Clock,
    private val savedState: androidx.lifecycle.SavedStateHandle,
    @DefaultDispatcher private val computationDispatcher: CoroutineDispatcher,
) : ViewModel() {
    val catalog = repository.state
    private val moments = flow {
        while (true) {
            emit(java.time.ZonedDateTime.now(clock.withZone(AcademicTime.Zone)))
            kotlinx.coroutines.delay(30_000)
        }
    }
    val feed = combine(repository.state, users.profile, moments) { state, profile, moment ->
        (state as? CatalogState.Ready)?.let { ready ->
            val remaining =
                StudyCalendar.lessons(ready.catalog, profile, moment.toLocalDate()).filter {
                    runCatching { java.time.LocalTime.parse(it.to) > moment.toLocalTime() }.getOrDefault(
                        false
                    )
                }
            HomeUiState(
                catalog = ready.catalog,
                profile = profile,
                moment = moment,
                offline = ready.offline,
                remaining = remaining,
                featured = EventLogic.featured(
                    ready.catalog.events,
                    profile.role,
                    moment.toLocalDate()
                ),
                projects = ready.catalog.projects.filter { it.featured })
        }
    }.flowOn(computationDispatcher)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun refresh() {
        viewModelScope.launch { repository.refresh(force = true) }
    }

    private val eventQuery = savedState.getStateFlow(EVENT_QUERY_KEY, "")
    val events = combine(catalog, eventQuery) { state, query ->
        EventsUiState(
            query = query,
            events = (state as? CatalogState.Ready)?.let {
                EventLogic.upcoming(it.catalog.events)
                    .filter { event -> EventLogic.matches(event, query) }
            }.orEmpty()
        )
    }.flowOn(computationDispatcher)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), EventsUiState())

    fun queryEvents(query: String) {
        savedState[EVENT_QUERY_KEY] = query
    }

    private companion object {
        const val EVENT_QUERY_KEY = "eventQuery"
    }
}
