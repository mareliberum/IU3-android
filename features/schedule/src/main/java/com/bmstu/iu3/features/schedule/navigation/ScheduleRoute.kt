package com.bmstu.iu3.features.schedule.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmstu.iu3.core.data.api.CatalogState
import com.bmstu.iu3.core.designsystem.DataWriteErrorDialog
import com.bmstu.iu3.core.designsystem.LoadingContent
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.features.schedule.presentation.ScheduleViewModel
import com.bmstu.iu3.features.schedule.ui.ScheduleScreen

@Composable
fun ScheduleRoute(
    route: Route,
    back: () -> Unit,
    navigate: (Route) -> Unit,
    open: (String) -> Unit,
    share: (String) -> Unit,
    parentBack: (() -> Unit)? = null,
    viewModel: ScheduleViewModel = hiltViewModel(),
) {
    val writeError by viewModel.hasWriteError.collectAsStateWithLifecycle()
    if (writeError) DataWriteErrorDialog(onDismiss = viewModel::clearWriteError)
    val state by viewModel.catalog.collectAsStateWithLifecycle()
    val currentProfile by viewModel.profile.collectAsStateWithLifecycle()
    val profile = currentProfile
    val ready = state as? CatalogState.Ready
    if (ready == null || profile == null) {
        LoadingContent(); return
    }
    val catalog = ready.catalog
    ScheduleScreen(
        profile = profile,
        lessonsFor = viewModel::lessons,
        upcoming = viewModel.upcoming(),
        groupsFor = viewModel::groups,
        onGroup = viewModel::selectGroup,
        navigate = navigate
    )
}
