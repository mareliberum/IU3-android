package com.bmstu.iu3.features.instructors.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmstu.iu3.core.data.api.CatalogState
import com.bmstu.iu3.core.designsystem.LoadingContent
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.instructors.presentation.InstructorsViewModel
import com.bmstu.iu3.features.instructors.ui.InstructorScreen
import com.bmstu.iu3.features.instructors.ui.InstructorsScreen

@Composable
fun InstructorsRoute(
    route: Route,
    back: () -> Unit,
    navigate: (Route) -> Unit,
    open: (String) -> Unit,
    share: (String) -> Unit,
    parentBack: (() -> Unit)? = null,
    viewModel: InstructorsViewModel = hiltViewModel(),
) {
    val state by viewModel.catalog.collectAsStateWithLifecycle()
    val ready = state as? CatalogState.Ready
    if (ready == null) {
        LoadingContent(); return
    }
    val catalog = ready.catalog
    when (route.screen) {
        Screen.Instructors -> {
            val directory by viewModel.directory.collectAsStateWithLifecycle()
            InstructorsScreen(
                state = directory,
                updateQuery = viewModel::query,
                back = back,
                navigate = navigate
            )
        }

        Screen.Instructor -> catalog.staff.firstOrNull { it.name == route.id }?.let { person ->
            viewModel.detail(person)?.let {
                InstructorScreen(
                    state = it,
                    lessonsFor = { date -> viewModel.lessons(person, date) },
                    back = back,
                    navigate = navigate,
                    open = open
                )
            }
        }

        else -> Unit
    }
}
