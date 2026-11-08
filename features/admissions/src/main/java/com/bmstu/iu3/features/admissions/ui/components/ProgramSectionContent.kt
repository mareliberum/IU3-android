package com.bmstu.iu3.features.admissions.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bmstu.iu3.core.designsystem.AppGray
import com.bmstu.iu3.core.designsystem.FacultyImage
import com.bmstu.iu3.core.designsystem.ItemCard
import com.bmstu.iu3.core.designsystem.Section
import com.bmstu.iu3.core.model.ProgramSection
import com.bmstu.iu3.core.model.ProgramSectionType
import com.bmstu.iu3.features.admissions.R

@Composable
internal fun ProgramSectionContent(section: ProgramSection) {
    val title = section.title
    if (title.isNotBlank()) Section(title = title)
    if (!section.subtitle.isNullOrBlank()) Text(section.subtitle.orEmpty(), color = AppGray)
    when (section.type) {
        ProgramSectionType.Journey -> section.steps.forEach { step ->
            ItemCard(
                title = step.title,
                subtitle = step.stage + "\n" + step.text
            )
        }

        ProgramSectionType.Cards -> section.cards.forEach { card ->
            ItemCard(
                title = card.title,
                subtitle = card.text,
                image = card.image
            )
        }

        ProgramSectionType.Chips -> ProgramChips(labels = section.items)
        ProgramSectionType.Disciplines -> section.groups.forEach { group ->
            Section(title = group.title); Text(group.subtitle); Text(
            group.items.joinToString("\n")
        )
        }

        ProgramSectionType.Companies -> section.companies.chunked(3).forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { company ->
                    Column(Modifier.weight(1f)) {
                        FacultyImage(
                            name = company.logo,
                            modifier = Modifier.fillMaxWidth().height(72.dp),
                            description = company.name,
                            crop = false
                        ); Text(company.name, style = MaterialTheme.typography.labelSmall)
                    }
                }
                repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }

        ProgramSectionType.Scores -> {
            Section(title = stringResource(R.string.admissions_minimum))
            section.minimum.forEach { stat -> Text(stat.label + ": " + stat.value) }
            Section(title = stringResource(R.string.admissions_passing))
            section.passing.forEach { stat -> Text(stat.label + ": " + stat.value) }
        }

        ProgramSectionType.ScoreHistory -> {
            Text(stringResource(R.string.admissions_score_columns), color = AppGray)
            section.years.sortedByDescending { it.year }.forEach { year ->
                Text(
                    listOf(
                        year.year.toString(),
                        year.budget.toString(),
                        year.average,
                        year.paid?.toString() ?: "—"
                    ).joinToString(" · ")
                )
            }
        }
    }
}
