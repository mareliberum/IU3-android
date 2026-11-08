package com.bmstu.iu3.features.department.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmstu.iu3.core.data.api.CatalogState
import com.bmstu.iu3.core.designsystem.LoadingContent
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.department.presentation.DepartmentViewModel
import com.bmstu.iu3.features.department.ui.AchievementsScreen
import com.bmstu.iu3.features.department.ui.ContactsScreen
import com.bmstu.iu3.features.department.ui.DepartmentScreen

@Composable
fun DepartmentRoute(
    route: Route,
    back: () -> Unit,
    navigate: (Route) -> Unit,
    open: (String) -> Unit,
    share: (String) -> Unit,
    parentBack: (() -> Unit)? = null,
    viewModel: DepartmentViewModel = hiltViewModel(),
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
        Screen.Department -> DepartmentScreen(profile = profile, navigate = navigate)
        Screen.Achievements -> AchievementsScreen(catalog = catalog, back = back)
        Screen.Contacts -> ContactsScreen(
            catalog = catalog,
            keyStaff = viewModel.contacts(),
            profile = profile,
            back = back,
            open = open,
            navigate = navigate
        )

        else -> Unit
    }
}
