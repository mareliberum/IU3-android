package com.bmstu.iu3.features.courses.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmstu.iu3.core.data.api.CatalogRepository
import com.bmstu.iu3.core.data.api.CatalogState
import com.bmstu.iu3.core.data.api.UserRepository
import com.bmstu.iu3.core.model.Course
import com.bmstu.iu3.core.model.EducationLevel
import com.bmstu.iu3.core.model.StudyPlans
import com.bmstu.iu3.core.model.TechnologyStack
import com.bmstu.iu3.features.courses.domain.CourseCatalogPolicy
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class CoursesViewModel @Inject constructor(
    private val repository: CatalogRepository,
    private val users: UserRepository,
) : ViewModel() {
    val catalog = repository.state
    val profile = users.profile.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    fun courses(stack: TechnologyStack?, technology: String): List<Course> =
        (catalog.value as? CatalogState.Ready)?.let {
            CourseCatalogPolicy.courses(
                it.catalog,
                stack,
                technology
            )
        }.orEmpty()

    fun detail(course: Course): CourseUiState? {
        val data = (catalog.value as? CatalogState.Ready)?.catalog ?: return null
        val group = profile.value?.group.orEmpty()
        return CourseUiState(
            course = course,
            semesterLoads = (1..if (course.level == EducationLevel.Master) 4 else 8).associateWith { semester ->
                course.loads.firstOrNull {
                    it.firstOrNull()?.toInt() == semester
                }?.getOrNull(1)
            },
            plans = StudyPlans.forCourse(data, course, group).map { plan ->
                CoursePlanUiState(
                    plan = plan,
                    disciplines = plan.disciplines.filter { StudyPlans.matchesCourse(course, it) })
            },
            teachers = data.staff.filter { it.name in course.instructors },
        )
    }
}
