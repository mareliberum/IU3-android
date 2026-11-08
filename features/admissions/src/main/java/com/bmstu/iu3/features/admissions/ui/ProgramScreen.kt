package com.bmstu.iu3.features.admissions.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bmstu.iu3.core.designsystem.AppCard
import com.bmstu.iu3.core.designsystem.FacultyImage
import com.bmstu.iu3.core.designsystem.ItemCard
import com.bmstu.iu3.core.designsystem.Page
import com.bmstu.iu3.core.designsystem.Section
import com.bmstu.iu3.core.designsystem.contentKey
import com.bmstu.iu3.core.model.AdmissionProgram
import com.bmstu.iu3.core.model.AdmissionProgramId
import com.bmstu.iu3.core.model.DepartmentLinks
import com.bmstu.iu3.core.model.ProgramCareer
import com.bmstu.iu3.core.model.ProgramSection
import com.bmstu.iu3.core.model.ProgramStat
import com.bmstu.iu3.core.model.StudyProgram
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.RouteArguments
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.admissions.R
import com.bmstu.iu3.features.admissions.ui.components.ProgramChips
import com.bmstu.iu3.features.admissions.ui.components.ProgramSectionContent

@Composable
fun ProgramScreen(
    program: AdmissionProgram,
    back: () -> Unit,
    navigate: (Route) -> Unit,
    open: (String) -> Unit,
) {
    Page(title = program.shortTitle, back = back) {
        item {
            FacultyImage(
                name = program.heroImage,
                modifier = Modifier.fillMaxWidth().height(240.dp)
            )
            Text(
                program.title,
                style = MaterialTheme.typography.headlineMedium
            ); Text(program.summary)
        }
        if (program.tags.isNotEmpty()) item { ProgramChips(labels = program.tags) }
        item { ProgramChips(labels = program.badges) }
        items(
            program.stats,
            key = { contentKey<ProgramStat>(id = it.label) },
            contentType = { ProgramStat::class }) { stat ->
            ItemCard(
                title = stat.value,
                subtitle = stat.label
            )
        }
        items(
            program.perks,
            key = { contentKey<String>(id = it) },
            contentType = { String::class }) { Text(it) }
        item {
            Section(title = stringResource(if (program.codes.size == 1) R.string.admissions_code_single else R.string.admissions_codes)); Text(
            program.codes.joinToString("\n")
        )
        }
        val plan = when (program.id) {
            AdmissionProgramId.Bachelor -> StudyProgram.Bachelor; AdmissionProgramId.Master -> StudyProgram.Master; AdmissionProgramId.GameDevelopment -> StudyProgram.GameDevelopment; else -> null
        }
        if (plan != null) item {
            ItemCard(
                stringResource(R.string.admissions_plan),
                plan.code,
                onClick = {
                    navigate(
                        Route(
                            Screen.StudyPlan,
                            arguments = RouteArguments.StudyPlan(plan)
                        )
                    )
                })
        }
        items(
            program.sections,
            key = { contentKey<ProgramSection>(id = it.type.code + it.title) },
            contentType = { ProgramSection::class }) { section -> ProgramSectionContent(section = section) }
        item { Section(title = stringResource(R.string.admissions_careers)) }
        items(
            program.careers.chunked(2),
            key = { contentKey<ProgramCareer>(id = it.joinToString { career -> career.title }) },
            contentType = { ProgramCareer::class }) { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { career ->
                    Card(
                        Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = AppCard),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            career.image?.let {
                                FacultyImage(
                                    it,
                                    Modifier.size(48.dp),
                                    crop = false,
                                    background = Color.Transparent
                                )
                            }
                            Text(career.title, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
        if (program.professions.isNotEmpty()) item {
            Section(title = stringResource(R.string.admissions_professions)); ProgramChips(
            labels = program.professions
        )
        }
        item { Section(title = program.callToAction.title); Text(program.callToAction.text) }
        item {
            Button({ open(DepartmentLinks.ADMISSIONS) }, Modifier.fillMaxWidth()) {
                Text(
                    stringResource(R.string.admissions_apply)
                )
            }
            OutlinedButton({ open(DepartmentLinks.STUDY_PLANS) }, Modifier.fillMaxWidth()) {
                Text(
                    stringResource(R.string.admissions_plans)
                )
            }
        }
    }
}
