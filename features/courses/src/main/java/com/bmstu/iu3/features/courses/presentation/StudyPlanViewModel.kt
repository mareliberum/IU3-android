package com.bmstu.iu3.features.courses.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmstu.iu3.core.common.di.DefaultDispatcher
import com.bmstu.iu3.core.data.api.CatalogRepository
import com.bmstu.iu3.core.data.api.CatalogState
import com.bmstu.iu3.core.data.api.UserRepository
import com.bmstu.iu3.core.model.AcademicTime
import com.bmstu.iu3.core.model.StudyPlans
import com.bmstu.iu3.core.model.StudyProgram
import com.bmstu.iu3.core.navigation.RouteArguments
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Clock
import java.time.LocalDate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn

@HiltViewModel(assistedFactory = StudyPlanViewModelFactory::class)
class StudyPlanViewModel @AssistedInject constructor(
    catalog: CatalogRepository,
    users: UserRepository,
    private val savedState: SavedStateHandle,
    private val clock: Clock,
    @param:Assisted private val arguments: RouteArguments.StudyPlan,
    @DefaultDispatcher private val computationDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val selection = savedState.getStateFlow(SELECTION_KEY, PlanSelection())
    val uiState = combine(catalog.state, users.profile, selection) { state, profile, selected ->
        (state as? CatalogState.Ready)?.catalog?.let { data ->
            val default =
                data.plans.firstOrNull { arguments.program != null && it.program == arguments.program }
                    ?: StudyPlans.forGroup(data, profile.group) ?: data.plans.firstOrNull()
            val program =
                selected.programCode?.let(::StudyProgram) ?: arguments.program ?: default?.program
                ?: StudyProgram.Unspecified
            val semesters =
                data.plans.filter { it.program == program }.map { it.semester }.distinct().sorted()
            val desired = selected.semester ?: arguments.semester ?: default?.semester ?: 1
            val semester = desired.takeIf { it in semesters } ?: semesters.firstOrNull() ?: 1
            val plan = data.plans.firstOrNull { it.program == program && it.semester == semester }
            StudyPlanUiState(
                program = program,
                semester = semester,
                programs = data.plans.map { it.program }.distinct(),
                semesters = semesters,
                plan = plan,
                currentWeek = plan?.let {
                    StudyPlans.currentWeek(
                        it,
                        LocalDate.now(clock.withZone(AcademicTime.Zone))
                    )
                },
                controls = plan?.let { StudyPlans.controls(it).entries.map { entry -> entry.key to entry.value } }
                    .orEmpty(),
                courseIds = plan?.disciplines.orEmpty().mapNotNull { discipline ->
                    data.courses.firstOrNull {
                        StudyPlans.normalize(it.title) == StudyPlans.normalize(
                            discipline.title
                        )
                    }?.let { discipline.title to it.id }
                }.toMap(),
            )
        }
    }.flowOn(computationDispatcher)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun selectProgram(program: StudyProgram) {
        val current = uiState.value ?: return
        savedState[SELECTION_KEY] =
            PlanSelection(programCode = program.code, semester = current.semester)
    }

    fun selectSemester(semester: Int) {
        val current = uiState.value ?: return
        savedState[SELECTION_KEY] =
            PlanSelection(programCode = current.program.code, semester = semester)
    }

    private companion object {
        const val SELECTION_KEY = "planSelection"
    }
}
