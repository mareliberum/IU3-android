package com.bmstu.iu3.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bmstu.iu3.R
import com.bmstu.iu3.core.data.api.CatalogState
import com.bmstu.iu3.core.designsystem.EmptyState
import com.bmstu.iu3.core.designsystem.LocalFaceCenters
import com.bmstu.iu3.core.designsystem.LocalImages
import com.bmstu.iu3.core.designsystem.Page
import com.bmstu.iu3.features.onboarding.navigation.OnboardingRoute

@Composable
fun IU3App(vm: AppViewModel = viewModel()) {
    val state by vm.catalog.collectAsStateWithLifecycle()
    val profile by vm.profile.collectAsStateWithLifecycle()
    val ready = state as? CatalogState.Ready
    if (profile == null || state is CatalogState.Loading) {
        Box(
            Modifier.fillMaxSize().safeDrawingPadding(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) { CircularProgressIndicator() }
        return
    }
    if (ready == null) {
        Page(title = stringResource(R.string.app_name)) {
            item {
                EmptyState(title = stringResource(R.string.app_load_error)); Button(
                vm::refresh
            ) { Text(stringResource(R.string.app_retry)) }
            }
        }
        return
    }
    val centers = remember(ready.catalog.faces) {
        ready.catalog.faces.mapValues { (_, element) ->
            val face = element
            (face.x + face.width / 2) to
                (face.y + face.height / 2)
        }
    }
    CompositionLocalProvider(
        LocalImages provides ready.catalog.images,
        LocalFaceCenters provides centers
    ) {
        if (profile?.completed != true) OnboardingRoute()
        else key(profile?.role) {
            MainNavigation(
                profile = profile!!,
                contactEmail = ready.catalog.contacts.email,
                foreground = vm::foreground
            )
        }
    }
}
