package com.bmstu.iu3.features.courses.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmstu.iu3.core.designsystem.AppBlue
import com.bmstu.iu3.core.designsystem.AppCard
import com.bmstu.iu3.core.designsystem.AppGray
import com.bmstu.iu3.core.designsystem.AppLink
import com.bmstu.iu3.core.designsystem.AppStrongSelection
import com.bmstu.iu3.core.designsystem.AppText
import com.bmstu.iu3.core.designsystem.EmptyState
import com.bmstu.iu3.core.designsystem.Page
import com.bmstu.iu3.core.designsystem.contentKey
import com.bmstu.iu3.core.model.Discipline
import com.bmstu.iu3.core.model.StudyControl
import com.bmstu.iu3.core.model.StudyProgram
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.courses.R
import com.bmstu.iu3.features.courses.presentation.StudyPlanUiState
import com.bmstu.iu3.features.courses.ui.components.ControlWeekCard
import com.bmstu.iu3.features.courses.ui.components.CurrentControlsCard
import com.bmstu.iu3.features.courses.ui.components.DisciplineCard
import com.bmstu.iu3.features.courses.ui.components.PlanSummaryCard
import com.bmstu.iu3.features.courses.ui.components.planTitle

@Composable
fun StudyPlanScreen(
    state: StudyPlanUiState,
    selectProgram: (StudyProgram) -> Unit,
    selectSemester: (Int) -> Unit,
    back: () -> Unit,
    navigate: (Route) -> Unit,
) {
    val program = state.program
    val semester = state.semester
    val programs = state.programs
    val programScroll = rememberLazyListState(
        initialFirstVisibleItemIndex = programs.indexOf(program).coerceAtLeast(0)
    )
    val semesters = state.semesters
    val plan = state.plan
    Page(
        title = stringResource(R.string.courses_plan),
        back = back,
        profile = { navigate(Route(screen = Screen.Profile)) }) {
        item {
            LazyRow(
                state = programScroll, horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().selectableGroup()
            ) {
                items(
                    programs,
                    key = { contentKey<StudyProgram>(id = it.code) },
                    contentType = { StudyProgram::class }) { value ->
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = if (value == program) AppBlue else AppCard,
                        modifier = Modifier.selectable(
                            selected = value == program,
                            role = Role.Tab,
                            onClick = {
                                selectProgram(value)
                            })
                    ) {
                        Text(
                            planTitle(program = value),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = if (value == program) androidx.compose.ui.graphics.Color.White else AppText,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }
        item {
            Row(
                Modifier.fillMaxWidth().padding(vertical = 4.dp).selectableGroup(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                semesters.forEach { number ->
                    val label = stringResource(R.string.courses_semester, number)
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (number == semester) AppStrongSelection else AppCard,
                        modifier = Modifier.weight(1f).selectable(
                            selected = number == semester,
                            role = Role.Tab,
                            onClick = { selectSemester(number) })
                    ) {
                        Box(
                            Modifier.heightIn(min = 40.dp).padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                number.toString(), style = MaterialTheme.typography.titleMedium,
                                color = if (number == semester) androidx.compose.ui.graphics.Color.White else AppLink,
                                modifier = Modifier.semantics { contentDescription = label })
                        }
                    }
                }
            }
        }
        if (plan == null) item { EmptyState(title = stringResource(R.string.courses_no_plan)) }
        else {
            val currentWeek = state.currentWeek
            item { PlanSummaryCard(plan = plan) }
            if (currentWeek != null) item { CurrentControlsCard(plan = plan, week = currentWeek) }
            item {
                Text(
                    stringResource(R.string.courses_calendar_title),
                    fontSize = 20.sp,
                    lineHeight = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            items(
                state.controls,
                key = { contentKey<StudyControl>(id = it.first.toString()) },
                contentType = { StudyControl::class }) { (week, events) ->
                ControlWeekCard(plan = plan, week = week, events = events, current = currentWeek)
            }
            item {
                Text(
                    stringResource(R.string.courses_disciplines),
                    fontSize = 20.sp,
                    lineHeight = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            items(
                plan.disciplines,
                key = { contentKey<Discipline>(id = it.title) },
                contentType = { Discipline::class }) { discipline ->
                val courseId = state.courseIds[discipline.title]
                DisciplineCard(
                    plan,
                    discipline,
                    openCourse = courseId?.let { id ->
                        {
                            navigate(
                                Route(
                                    screen = Screen.Course,
                                    id = id
                                )
                            )
                        }
                    })
            }
            item {
                Text(
                    stringResource(R.string.courses_plan_source),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppGray
                )
            }
        }
    }
}
