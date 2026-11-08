package com.bmstu.iu3.features.honor.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bmstu.iu3.core.designsystem.AppGray
import com.bmstu.iu3.core.designsystem.EmptyState
import com.bmstu.iu3.core.designsystem.FacultyImage
import com.bmstu.iu3.core.designsystem.ItemCard
import com.bmstu.iu3.core.designsystem.Page
import com.bmstu.iu3.core.designsystem.Section
import com.bmstu.iu3.core.designsystem.contentKey
import com.bmstu.iu3.core.model.Catalog
import com.bmstu.iu3.core.model.Honor
import com.bmstu.iu3.core.model.Publication
import com.bmstu.iu3.core.navigation.external.ExternalUris
import com.bmstu.iu3.features.honor.R
import com.bmstu.iu3.features.honor.ui.format.isLegend

@Composable
fun HonorDetailScreen(
    person: Honor,
    catalog: Catalog,
    back: () -> Unit,
    open: (String) -> Unit,
) {
    var works by rememberSaveable(person.name) { mutableStateOf(false) }
    Page(
        title = if (person.isLegend()) stringResource(R.string.honor_teacher) else stringResource(R.string.honor_graduate),
        back = back
    ) {
        item {
            FacultyImage(
                name = person.detailPhotoAsset ?: person.photoAsset,
                modifier = Modifier.fillMaxWidth().height(320.dp)
            )
            Text(person.subtitle, color = AppGray); Text(
            person.name,
            style = MaterialTheme.typography.headlineMedium
        )
            Text(
                listOfNotNull(
                    person.role,
                    person.company,
                    person.graduationYear?.let {
                        stringResource(
                            R.string.honor_graduation_year,
                            it
                        )
                    }).joinToString(" · ")
            )
        }
        if (person.isLegend()) item {
            Row {
                FilterChip(
                    !works,
                    { works = false },
                    { Text(stringResource(R.string.honor_bio)) }); Spacer(Modifier.width(8.dp)); FilterChip(
                works,
                { works = true },
                { Text(stringResource(R.string.honor_works)) })
            }
        }
        if (!works) {
            val bio = person.biography ?: catalog.webFaculty[person.name]?.biography
            ?: catalog.staff.firstOrNull {
                it.name.substringBefore(' ') == person.name.substringBefore(
                    ' '
                )
            }?.about
            if (!bio.isNullOrBlank()) item {
                Section(title = stringResource(R.string.honor_info)); Text(
                bio
            )
            }
            item {
                Section(
                    title = if (person.isLegend()) stringResource(R.string.honor_facts) else stringResource(
                        R.string.honor_achievements
                    )
                )
            }
            items(
                person.facts.ifEmpty { listOf(person.subtitle) },
                key = { contentKey<String>(id = it) },
                contentType = { String::class }) { fact -> Text(fact) }
        } else {
            val publications = catalog.legendWorks[person.name].orEmpty()
                .ifEmpty { catalog.webFaculty[person.name]?.publications.orEmpty() }
            val combined = publications + person.works
            if (combined.isEmpty()) item { EmptyState(title = stringResource(R.string.honor_no_works)) }
            items(
                combined.distinctBy { it.title.lowercase() }
                    .sortedByDescending { it.year },
                key = { contentKey<Publication>(id = it.title + it.year) },
                contentType = { Publication::class }) { work ->
                val title = work.title
                ItemCard(title, work.year.toString(), onClick = {
                    open(
                        work.url
                            ?: ExternalUris.scholar(title)
                    )
                })
            }
        }
    }
}
