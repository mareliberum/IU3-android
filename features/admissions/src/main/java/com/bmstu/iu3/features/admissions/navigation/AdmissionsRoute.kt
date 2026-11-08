package com.bmstu.iu3.features.admissions.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmstu.iu3.core.data.api.CatalogState
import com.bmstu.iu3.core.designsystem.LoadingContent
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.RouteArguments
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.admissions.presentation.AdmissionsViewModel
import com.bmstu.iu3.features.admissions.ui.AdmissionsScreen
import com.bmstu.iu3.features.admissions.ui.ProgramScreen

@Composable
fun AdmissionsRoute(
    route: Route,
    back: () -> Unit,
    navigate: (Route) -> Unit,
    open: (String) -> Unit,
    share: (String) -> Unit,
    parentBack: (() -> Unit)? = null,
    viewModel: AdmissionsViewModel = hiltViewModel(),
) {
    val state by viewModel.catalog.collectAsStateWithLifecycle()
    val ready = state as? CatalogState.Ready
    if (ready == null) {
        LoadingContent(); return
    }
    val catalog = ready.catalog
    when (route.screen) {
        Screen.Admissions -> AdmissionsScreen(
            catalog = catalog,
            back = parentBack,
            navigate = navigate
        )

        Screen.Program -> catalog.programs.firstOrNull { it.id == (route.arguments as? RouteArguments.Admission)?.program }
            ?.let { ProgramScreen(program = it, back = back, navigate = navigate, open = open) }

        else -> Unit
    }
}
