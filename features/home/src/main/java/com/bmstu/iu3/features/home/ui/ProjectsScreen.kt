package com.bmstu.iu3.features.home.ui

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bmstu.iu3.core.designsystem.ItemCard
import com.bmstu.iu3.core.designsystem.Page
import com.bmstu.iu3.core.designsystem.contentKey
import com.bmstu.iu3.core.model.Catalog
import com.bmstu.iu3.core.model.Project
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.home.R

@Composable
fun ProjectsScreen(
    catalog: Catalog,
    back: () -> Unit,
    navigate: (Route) -> Unit,
) {
    Page(title = stringResource(R.string.home_projects_03323), back = back) {
        items(
            catalog.projects,
            key = { contentKey<Project>(id = it.title) },
            contentType = { Project::class }) { project ->
            ItemCard(
                project.title,
                project.subtitle,
                project.image,
                onClick = { navigate(Route(screen = Screen.Project, id = project.title)) })
        }
    }
}
