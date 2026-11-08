package com.bmstu.iu3.features.courses.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmstu.iu3.core.data.api.CatalogState
import com.bmstu.iu3.core.designsystem.LoadingContent
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.RouteArguments
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.courses.presentation.CoursesViewModel
import com.bmstu.iu3.features.courses.ui.CourseScreen
import com.bmstu.iu3.features.courses.ui.CoursesScreen
import com.bmstu.iu3.features.courses.ui.StacksScreen

@Composable
fun CoursesRoute(
    route: Route,
    back: () -> Unit,
    navigate: (Route) -> Unit,
    open: (String) -> Unit,
    share: (String) -> Unit,
    parentBack: (() -> Unit)? = null,
    viewModel: CoursesViewModel = hiltViewModel(),
) {
    val state by viewModel.catalog.collectAsStateWithLifecycle()
    val currentProfile by viewModel.profile.collectAsStateWithLifecycle()
    val profile = currentProfile
    val ready = state as? CatalogState.Ready
    if (ready == null || profile == null) {
        LoadingContent(); return
    }
    val catalog = ready.catalog
    when (route.screen) {
        Screen.Stacks -> StacksScreen(catalog = catalog, back = back, navigate = navigate)
        Screen.Courses -> CoursesScreen(
            courses = viewModel.courses(
                (route.arguments as? RouteArguments.Courses)?.stack,
                (route.arguments as? RouteArguments.Courses)?.technology.orEmpty()
            ),
            stack = (route.arguments as? RouteArguments.Courses)?.stack,
            technology = (route.arguments as? RouteArguments.Courses)?.technology.orEmpty(),
            back = back,
            navigate = navigate
        )

        Screen.Course -> catalog.courses.firstOrNull { it.id == route.id }?.let {
            viewModel.detail(it)
                ?.let { detail -> CourseScreen(state = detail, back = back, navigate = navigate) }
        }

        else -> Unit
    }
}
