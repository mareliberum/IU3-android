package com.bmstu.iu3.features.home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bmstu.iu3.core.designsystem.AppGray
import com.bmstu.iu3.core.designsystem.FacultyImage
import com.bmstu.iu3.core.designsystem.ItemCard
import com.bmstu.iu3.core.designsystem.Page
import com.bmstu.iu3.core.designsystem.PhotoGallery
import com.bmstu.iu3.core.designsystem.Section
import com.bmstu.iu3.core.designsystem.contentKey
import com.bmstu.iu3.core.model.Photo
import com.bmstu.iu3.core.model.Project
import com.bmstu.iu3.core.model.ProjectFact
import com.bmstu.iu3.core.model.ProjectLink
import com.bmstu.iu3.features.home.R

@Composable
fun ProjectScreen(
    project: Project,
    back: () -> Unit,
    open: (String) -> Unit,
) {
    val photos = project.gallery.map { it.image to it.caption }
    var gallery by rememberSaveable(project.title) { mutableIntStateOf(-1) }
    if (gallery >= 0) PhotoGallery(photos = photos, index = gallery) { gallery = -1 }
    Page(title = stringResource(R.string.home_project_context), back = back) {
        item {
            FacultyImage(
                name = project.image,
                modifier = Modifier.fillMaxWidth().height(240.dp)
            )
        }
        item {
            Text(
                project.title,
                style = MaterialTheme.typography.headlineMedium
            ); Text(project.subtitle, color = AppGray)
        }
        item { Section(title = stringResource(R.string.home_about_project)); Text(project.details.ifBlank { project.subtitle }) }
        items(
            project.facts,
            key = { contentKey<ProjectFact>(id = it.label) },
            contentType = { ProjectFact::class }) { fact ->
            ItemCard(title = fact.value, subtitle = fact.label)
        }
        if (project.gallery.isNotEmpty()) item {
            Section(title = project.galleryTitle.ifBlank {
                stringResource(
                    R.string.home_photos
                )
            })
        }
        items(
            photos.indices.toList(),
            key = { contentKey<Photo>(id = photos[it].first) },
            contentType = { Photo::class }) { index ->
            FacultyImage(
                name = photos[index].first,
                modifier = Modifier.fillMaxWidth().height(240.dp).clickable { gallery = index })
            Text(photos[index].second, color = AppGray)
        }
        if (project.links.isNotEmpty()) item { Section(title = stringResource(R.string.home_more_details)) }
        items(
            project.links,
            key = { contentKey<ProjectLink>(id = it.url) },
            contentType = { ProjectLink::class }) { link ->
            TextButton({ open(link.url) }) { Text(link.title) }
        }
    }
}
