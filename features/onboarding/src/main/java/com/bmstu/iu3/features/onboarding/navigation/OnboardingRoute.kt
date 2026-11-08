package com.bmstu.iu3.features.onboarding.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmstu.iu3.core.data.api.CatalogState
import com.bmstu.iu3.core.designsystem.DataWriteErrorDialog
import com.bmstu.iu3.core.designsystem.LoadingContent
import com.bmstu.iu3.features.onboarding.presentation.OnboardingViewModel
import com.bmstu.iu3.features.onboarding.ui.OnboardingScreen

@Composable
fun OnboardingRoute(
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val writeError by viewModel.hasWriteError.collectAsStateWithLifecycle()
    if (writeError) DataWriteErrorDialog(onDismiss = viewModel::clearWriteError)
    val state by viewModel.catalog.collectAsStateWithLifecycle()
    val ready = state as? CatalogState.Ready
    if (ready == null) {
        LoadingContent(); return
    }
    val catalog = ready.catalog
    OnboardingScreen(catalog = catalog, onComplete = viewModel::complete)
}
