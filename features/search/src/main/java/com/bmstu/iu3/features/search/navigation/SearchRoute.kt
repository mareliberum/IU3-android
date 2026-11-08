package com.bmstu.iu3.features.search.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmstu.iu3.core.designsystem.DataWriteErrorDialog
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.features.search.presentation.SearchViewModel
import com.bmstu.iu3.features.search.ui.SearchScreen

@Composable
fun SearchRoute(
    focusRequest: Int,
    navigate: (Route) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val writeError by viewModel.hasWriteError.collectAsStateWithLifecycle()
    if (writeError) DataWriteErrorDialog(onDismiss = viewModel::clearWriteError)
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SearchScreen(
        state = state,
        focusRequest = focusRequest,
        updateQuery = viewModel::query,
        remember = viewModel::remember,
        forget = viewModel::forget,
        navigate = navigate
    )
}
