package com.bmstu.iu3.features.honor.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmstu.iu3.core.data.api.CatalogState
import com.bmstu.iu3.core.designsystem.LoadingContent
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.RouteArguments
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.honor.presentation.HonorViewModel
import com.bmstu.iu3.features.honor.ui.HonorDetailScreen
import com.bmstu.iu3.features.honor.ui.HonorGridScreen
import com.bmstu.iu3.features.honor.ui.HonorScreen

@Composable
fun HonorRoute(
    route: Route,
    back: () -> Unit,
    navigate: (Route) -> Unit,
    open: (String) -> Unit,
    share: (String) -> Unit,
    parentBack: (() -> Unit)? = null,
    viewModel: HonorViewModel = hiltViewModel(),
) {
    val state by viewModel.catalog.collectAsStateWithLifecycle()
    val ready = state as? CatalogState.Ready
    if (ready == null) {
        LoadingContent(); return
    }
    val catalog = ready.catalog
    when (route.screen) {
        Screen.Honor -> HonorScreen(catalog = catalog, back = back, navigate = navigate)
        Screen.HonorList -> HonorGridScreen(
            catalog = catalog,
            legends = (route.arguments as? RouteArguments.HonorList)?.kind == com.bmstu.iu3.core.model.HonorKind.Legend,
            back = back,
            navigate = navigate
        )

        Screen.Legend, Screen.Graduate -> catalog.honor.firstOrNull { it.name == route.id }
            ?.let { HonorDetailScreen(person = it, catalog = catalog, back = back, open = open) }

        else -> Unit
    }
}
