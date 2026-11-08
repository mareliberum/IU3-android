package com.bmstu.iu3.features.courses.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmstu.iu3.core.designsystem.LoadingContent
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.RouteArguments
import com.bmstu.iu3.features.courses.presentation.StudyPlanViewModel
import com.bmstu.iu3.features.courses.presentation.StudyPlanViewModelFactory
import com.bmstu.iu3.features.courses.ui.StudyPlanScreen

@Composable
fun StudyPlanRoute(
    arguments: RouteArguments.StudyPlan,
    back: () -> Unit,
    navigate: (Route) -> Unit,
) {
    val viewModel =
        hiltViewModel<StudyPlanViewModel, StudyPlanViewModelFactory>(creationCallback = {
            it.create(arguments)
        })
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val ready = state
    if (ready == null) {
        LoadingContent(); return
    }
    StudyPlanScreen(
        state = ready,
        selectProgram = viewModel::selectProgram,
        selectSemester = viewModel::selectSemester,
        back = back,
        navigate = navigate
    )
}
