package com.bmstu.iu3.features.courses.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bmstu.iu3.core.designsystem.AppGray
import com.bmstu.iu3.core.designsystem.AppLink
import com.bmstu.iu3.core.designsystem.FacultyImage
import com.bmstu.iu3.core.designsystem.ItemCard
import com.bmstu.iu3.core.designsystem.Page
import com.bmstu.iu3.core.designsystem.Section
import com.bmstu.iu3.core.designsystem.contentKey
import com.bmstu.iu3.core.model.EducationLevel
import com.bmstu.iu3.core.model.Staff
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.RouteArguments
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.courses.R
import com.bmstu.iu3.features.courses.presentation.CoursePlanUiState
import com.bmstu.iu3.features.courses.presentation.CourseUiState
import com.bmstu.iu3.features.courses.ui.components.DisciplineCard
import com.bmstu.iu3.features.courses.ui.components.planTitle

@Composable
fun CourseScreen(
    state: CourseUiState,
    back: () -> Unit,
    navigate: (Route) -> Unit,
) {
    val course = state.course
    Page(title = stringResource(R.string.courses_course_context), back = back) {
        item {
            FacultyImage(
                name = course.image,
                modifier = Modifier.fillMaxWidth().height(240.dp)
            )
        }
        item { Text(course.title, style = MaterialTheme.typography.headlineMedium) }
        item {
            Section(
                title = if (course.level == EducationLevel.Master) stringResource(R.string.courses_master_semesters) else stringResource(
                    R.string.courses_semesters
                )
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                state.semesterLoads.forEach { (semester, load) ->
                    Column {
                        Text(semester.toString(), color = if (load == null) AppGray else AppLink)
                        if (load != null) Text(
                            stringResource(
                                R.string.courses_credits,
                                load.toString().removeSuffix(".0")
                            ), style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
        if (course.description.isNotBlank()) item {
            Section(title = stringResource(R.string.courses_annotation)); Text(
            course.description
        )
        }
        if (course.technologies.isNotEmpty()) item {
            Section(title = stringResource(R.string.courses_technologies)); Text(
            course.technologies.joinToString(" · ")
        )
        }
        val plans = state.plans
        if (plans.isNotEmpty()) item { Section(title = stringResource(R.string.courses_plan)) }
        items(
            plans,
            key = { contentKey<CoursePlanUiState>(id = it.plan.program.code + it.plan.semester) },
            contentType = { CoursePlanUiState::class }) { entry ->
            val plan = entry.plan
            ItemCard(
                planTitle(program = plan.program),
                stringResource(R.string.courses_semester, plan.semester),
                onClick = {
                    navigate(
                        Route(
                            Screen.StudyPlan,
                            arguments = RouteArguments.StudyPlan(plan.program, plan.semester)
                        )
                    )
                })
            entry.disciplines.forEach { discipline ->
                Spacer(Modifier.height(8.dp)); DisciplineCard(
                plan = plan,
                discipline = discipline,
                showTitle = false
            )
            }
        }
        val teachers = state.teachers
        if (teachers.isNotEmpty()) item { Section(title = stringResource(R.string.courses_teachers)) }
        items(
            teachers,
            key = { contentKey<Staff>(id = it.name) },
            contentType = { Staff::class }) { staff ->
            ItemCard(
                staff.name,
                staff.position,
                staff.photo,
                onClick = { navigate(Route(screen = Screen.Instructor, id = staff.name)) })
        }
    }
}
