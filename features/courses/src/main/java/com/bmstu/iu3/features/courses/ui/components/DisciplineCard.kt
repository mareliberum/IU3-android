package com.bmstu.iu3.features.courses.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmstu.iu3.core.designsystem.AppBlue
import com.bmstu.iu3.core.designsystem.AppCard
import com.bmstu.iu3.core.designsystem.AppGray
import com.bmstu.iu3.core.designsystem.AppLink
import com.bmstu.iu3.core.designsystem.AppSurface
import com.bmstu.iu3.core.model.AcademicTime
import com.bmstu.iu3.core.model.Attestation
import com.bmstu.iu3.core.model.Discipline
import com.bmstu.iu3.core.model.Plan
import com.bmstu.iu3.core.model.StudyActivity
import com.bmstu.iu3.core.model.StudyPlans
import com.bmstu.iu3.features.courses.R
import com.bmstu.iu3.features.courses.ui.format.studyNumber
import com.bmstu.iu3.features.courses.ui.format.weekRange
import java.time.LocalDate

@Composable
internal fun DisciplineCard(
    plan: Plan,
    discipline: Discipline,
    showTitle: Boolean = true,
    openCourse: (() -> Unit)? = null,
) {
    val current = StudyPlans.currentWeek(plan, LocalDate.now(AcademicTime.Zone))
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppCard)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                if (showTitle) discipline.title else stringResource(
                    R.string.courses_semester,
                    plan.semester
                ),
                style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold
            )
            Text(
                if (showTitle) discipline.dept + " · " + stringResource(
                    R.string.courses_discipline_hours,
                    studyNumber(value = discipline.ze),
                    studyNumber(value = discipline.hours)
                )
                else planTitle(program = plan.program) + " · " + plan.groups.joinToString() + " · " + stringResource(
                    R.string.courses_credits,
                    studyNumber(value = discipline.ze)
                ),
                color = AppGray, fontSize = 12.sp
            )
            val hours = listOf(
                stringResource(R.string.courses_hours_lectures) to discipline.contact.lectures,
                stringResource(R.string.courses_hours_seminars) to discipline.contact.seminars,
                stringResource(R.string.courses_hours_labs) to discipline.contact.labs,
                stringResource(R.string.courses_hours_self) to (discipline.hours - discipline.contact.total).coerceAtLeast(
                    0.0
                ),
            ).filter { it.second > 0 }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                hours.forEach { (label, amount) ->
                    Surface(color = AppSurface, shape = RoundedCornerShape(10.dp)) {
                        Column(Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                            Text(
                                stringResource(
                                    R.string.courses_hours_amount,
                                    studyNumber(value = amount)
                                ),
                                color = AppLink,
                                fontWeight = FontWeight.Bold
                            )
                            Text(label, color = AppGray, fontSize = 10.sp)
                        }
                    }
                }
            }
            val assessment = when (discipline.attestation) {
                Attestation.Exam -> stringResource(R.string.courses_exam)
                Attestation.RatingExam -> stringResource(R.string.courses_rating_exam)
                Attestation.Pass -> stringResource(R.string.courses_pass)
                Attestation.GradedPass -> stringResource(R.string.courses_graded_pass)
                else -> ""
            }
            val extra = when (discipline.extra) {
                StudyActivity.Coursework -> stringResource(R.string.courses_coursework)
                StudyActivity.CourseProject -> stringResource(R.string.courses_control_project)
                StudyActivity.Research -> stringResource(R.string.courses_research)
                StudyActivity.PracticalTraining, StudyActivity.Practice, StudyActivity.WorkshopPractice -> stringResource(
                    R.string.courses_practice
                )

                StudyActivity.FinalPractice -> stringResource(R.string.courses_final_practice)
                StudyActivity.Practicum -> stringResource(R.string.courses_practicum)
                else -> discipline.extra?.code.orEmpty()
            }
            val assessmentText =
                listOf(assessment, extra).filter(String::isNotBlank).joinToString(" · ")
            if (assessmentText.isNotBlank()) Text(
                assessmentText,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            if (discipline.points.isNotEmpty()) Text(
                stringResource(
                    R.string.courses_points,
                    discipline.points.joinToString(" · ") { studyNumber(value = it) }),
                color = AppGray,
                fontSize = 12.sp
            )
            if (discipline.events.isNotEmpty()) {
                Text(
                    stringResource(R.string.courses_control_weeks),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    (1..plan.weeks).toList().chunked(9).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            row.forEach { week ->
                                val types =
                                    discipline.events.filter { it.week == week }.map { it.type }
                                        .distinct()
                                val description = stringResource(
                                    R.string.courses_week_accessibility,
                                    week,
                                    types.joinToString(),
                                    if (week == current) stringResource(R.string.courses_now) else ""
                                )
                                Surface(
                                    color = if (week == current) AppBlue else AppSurface,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f).clearAndSetSemantics {
                                        contentDescription = description
                                    }) {
                                    Column(
                                        Modifier.heightIn(min = 54.dp).padding(3.dp),
                                        verticalArrangement = Arrangement.spacedBy(2.dp),
                                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            week.toString(),
                                            fontSize = 10.sp,
                                            lineHeight = 12.sp,
                                            color = if (week == current) Color.White else AppGray,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        types.forEach { type ->
                                            Surface(
                                                color = controlColor(type = type),
                                                shape = RoundedCornerShape(3.dp),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    type.code,
                                                    fontSize = 9.sp,
                                                    lineHeight = 11.sp,
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold,
                                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                                    modifier = Modifier.padding(vertical = 1.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            repeat(9 - row.size) { Spacer(Modifier.weight(1f)) }
                        }
                    }
                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    discipline.events.sortedBy { it.week }.map { it.type }.distinct()
                        .forEach { type ->
                            Row(
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Surface(
                                    color = controlColor(type = type),
                                    shape = RoundedCornerShape(2.dp),
                                    modifier = Modifier.size(8.dp)
                                ) {}
                                Text(
                                    type.code + " — " + controlTitle(type = type).lowercase(russian),
                                    fontSize = 10.sp,
                                    color = AppGray
                                )
                            }
                        }
                }
                current?.let { week ->
                    discipline.events.sortedBy { it.week }.firstOrNull { it.week >= week }
                        ?.let { event ->
                            Text(
                                stringResource(
                                    R.string.courses_next_control,
                                    controlTitle(type = event.type).lowercase(russian),
                                    event.week,
                                    weekRange(plan = plan, week = event.week)
                                ), fontSize = 12.sp
                            )
                        }
                }
            }
            if (openCourse != null) TextButton(openCourse) { Text(stringResource(R.string.courses_open_course)) }
        }
    }
}
