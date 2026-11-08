package com.bmstu.iu3.features.home.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmstu.iu3.core.data.api.CatalogState
import com.bmstu.iu3.core.designsystem.LoadingContent
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.home.presentation.HomeViewModel
import com.bmstu.iu3.features.home.ui.EventScreen
import com.bmstu.iu3.features.home.ui.EventsScreen
import com.bmstu.iu3.features.home.ui.HomeScreen
import com.bmstu.iu3.features.home.ui.NewsListScreen
import com.bmstu.iu3.features.home.ui.NewsScreen
import com.bmstu.iu3.features.home.ui.ProjectScreen
import com.bmstu.iu3.features.home.ui.ProjectsScreen

@Composable
fun HomeRoute(
    route: Route,
    back: () -> Unit,
    navigate: (Route) -> Unit,
    open: (String) -> Unit,
    share: (String) -> Unit,
    parentBack: (() -> Unit)? = null,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.catalog.collectAsStateWithLifecycle()
    val ready = state as? CatalogState.Ready
    if (ready == null) {
        LoadingContent(); return
    }
    val catalog = ready.catalog
    when (route.screen) {
        Screen.Home -> {
            val feed by viewModel.feed.collectAsStateWithLifecycle()
            feed?.let { HomeScreen(state = it, refresh = viewModel::refresh, navigate = navigate) }
                ?: LoadingContent()
        }

        Screen.Events -> {
            val events by viewModel.events.collectAsStateWithLifecycle()
            EventsScreen(
                state = events,
                updateQuery = viewModel::queryEvents,
                back = back,
                navigate = navigate
            )
        }

        Screen.Event -> catalog.events.firstOrNull { it.id == route.id }
            ?.let { EventScreen(event = it, back = back, open = open, share = share) }

        Screen.NewsList -> NewsListScreen(catalog = catalog, back = back, navigate = navigate)
        Screen.News -> catalog.news.firstOrNull { it.id == route.id }
            ?.let { NewsScreen(news = it, back = back, share = share, open = open) }

        Screen.Projects -> ProjectsScreen(catalog = catalog, back = back, navigate = navigate)
        Screen.Project -> catalog.projects.firstOrNull { it.title == route.id }
            ?.let { ProjectScreen(project = it, back = back, open = open) }

        else -> Unit
    }
}
