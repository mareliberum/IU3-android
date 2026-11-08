package com.bmstu.iu3.features.instructors.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmstu.iu3.core.common.di.DefaultDispatcher
import com.bmstu.iu3.core.data.api.CatalogRepository
import com.bmstu.iu3.core.data.api.CatalogState
import com.bmstu.iu3.core.model.AcademicTime
import com.bmstu.iu3.core.model.Lesson
import com.bmstu.iu3.core.model.LinkScheme
import com.bmstu.iu3.core.model.Profile
import com.bmstu.iu3.core.model.Publication
import com.bmstu.iu3.core.model.Role
import com.bmstu.iu3.core.model.Staff
import com.bmstu.iu3.core.model.StaffAssignments
import com.bmstu.iu3.core.model.StudyCalendar
import com.bmstu.iu3.features.instructors.domain.publications
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class InstructorsViewModel @Inject constructor(
    private val repository: CatalogRepository,
    private val savedState: SavedStateHandle,
    private val clock: java.time.Clock,
    @DefaultDispatcher private val computationDispatcher: CoroutineDispatcher,
) : ViewModel() {
    val catalog = repository.state


    private val query = savedState.getStateFlow(QUERY_KEY, "")
    val directory = combine(catalog, query) { state, query ->
        (state as? CatalogState.Ready)?.let { InstructorDirectory.filter(it.catalog, query) }
            ?: InstructorsUiState(query = query)
    }.flowOn(computationDispatcher)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), InstructorsUiState())

    fun query(value: String) {
        savedState[QUERY_KEY] = value
    }

    fun works(person: Staff): List<Publication> = (catalog.value as? CatalogState.Ready)?.let {
        publications(
            person = person,
            catalog = it.catalog
        )
    }.orEmpty()

    private companion object {
        const val QUERY_KEY = "query"
        const val INTERESTS_HEADING = "Области научных интересов:"
    }

    fun detail(person: Staff): InstructorDetailUiState? {
        val data = (catalog.value as? CatalogState.Ready)?.catalog ?: return null
        val scholar = data.scholars[person.name]
        val web = data.webFaculty[person.name]
        val today = java.time.LocalDate.now(clock.withZone(AcademicTime.Zone))
        val firstDate = (0..13).asSequence().map { today.plusDays(it.toLong()) }
            .firstOrNull { lessons(person, it).isNotEmpty() } ?: today
        return InstructorDetailUiState(
            person = person,
            biography = web?.biography ?: person.about?.substringBefore(INTERESTS_HEADING)
                ?.takeIf(String::isNotBlank),
            interests = data.interests[person.name] ?: web?.interests.orEmpty(),
            teaching = StaffAssignments.isTeaching(person),
            profiles = scholar?.profiles.orEmpty(),
            works = works(person),
            websites = person.web.filter {
                it.startsWith(LinkScheme.Https.prefix + "//") && !it.contains(
                    com.bmstu.iu3.features.instructors.domain.WebsiteKind.LinkedIn.domain,
                    true
                )
            },
            courses = data.courses.filter { person.name in it.instructors },
            initialDate = firstDate,
        )
    }

    fun lessons(person: Staff, date: java.time.LocalDate): List<Lesson> =
        (catalog.value as? CatalogState.Ready)?.let {
            StudyCalendar.lessons(
                it.catalog,
                Profile(role = Role.Teacher, teacher = person.name),
                date
            )
        }.orEmpty()
}
