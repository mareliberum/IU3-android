package com.bmstu.iu3.features.courses.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bmstu.iu3.core.designsystem.AppBlue
import com.bmstu.iu3.core.model.Attestation
import com.bmstu.iu3.core.model.Plan
import com.bmstu.iu3.features.courses.R
import com.bmstu.iu3.features.courses.ui.format.studyNumber

@Composable
internal fun PlanSummaryCard(plan: Plan) {
    Surface(
        color = AppBlue,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    stringResource(
                        R.string.courses_plan_heading,
                        plan.semester,
                        planSeason(plan = plan)
                    ),
                    style = MaterialTheme.typography.titleMedium, color = Color.White
                )
                Text(
                    stringResource(
                        R.string.courses_plan_groups,
                        plan.groups.joinToString(),
                        plan.weeks
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = .8f)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SummaryTile(
                    value = studyNumber(value = plan.disciplines.sumOf { it.ze }),
                    label = stringResource(R.string.courses_credit_units),
                    modifier = Modifier.weight(1f)
                )
                SummaryTile(value = plan.disciplines.count {
                    it.attestation in setOf(
                        Attestation.Exam,
                        Attestation.RatingExam
                    )
                }
                    .toString(),
                    label = stringResource(R.string.courses_exam_count),
                    modifier = Modifier.weight(1f))
                SummaryTile(value = plan.disciplines.count {
                    it.attestation in setOf(
                        Attestation.Pass,
                        Attestation.GradedPass
                    )
                }
                    .toString(),
                    label = stringResource(R.string.courses_pass_count),
                    modifier = Modifier.weight(1f))
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                PeriodRow(
                    icon = Icons.AutoMirrored.Outlined.MenuBook,
                    label = stringResource(R.string.courses_theory),
                    dates = plan.theory
                )
                plan.exams?.takeIf(String::isNotBlank)?.let {
                    PeriodRow(
                        icon = Icons.AutoMirrored.Outlined.Assignment,
                        label = stringResource(R.string.courses_session),
                        dates = it
                    )
                }
                plan.research?.takeIf { it.isNotBlank() && it != plan.theory }?.let {
                    PeriodRow(
                        icon = Icons.Outlined.Science,
                        label = stringResource(R.string.courses_research),
                        dates = it
                    )
                }
                plan.practice?.takeIf(String::isNotBlank)?.let {
                    PeriodRow(
                        icon = Icons.Outlined.Build,
                        label = stringResource(R.string.courses_practice),
                        dates = it
                    )
                }
                plan.finalAttestation?.takeIf(String::isNotBlank)?.let {
                    PeriodRow(
                        icon = Icons.Outlined.School,
                        label = stringResource(R.string.courses_final_attestation),
                        dates = it
                    )
                }
                plan.holidays?.takeIf(String::isNotBlank)?.let {
                    PeriodRow(
                        icon = Icons.Outlined.WbSunny,
                        label = stringResource(R.string.courses_holidays),
                        dates = it
                    )
                }
            }
        }
    }
}
