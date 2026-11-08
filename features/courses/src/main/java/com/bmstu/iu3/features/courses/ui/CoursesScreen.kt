package com.bmstu.iu3.features.courses.ui

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bmstu.iu3.core.designsystem.ItemCard
import com.bmstu.iu3.core.designsystem.Page
import com.bmstu.iu3.core.designsystem.contentKey
import com.bmstu.iu3.core.designsystem.educationLevelTitle
import com.bmstu.iu3.core.model.Course
import com.bmstu.iu3.core.model.TechnologyStack
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.courses.R
import com.bmstu.iu3.features.courses.ui.components.stackTitle

@Composable
fun CoursesScreen(
    courses: List<Course>,
    stack: TechnologyStack?,
    technology: String,
    back: () -> Unit,
    navigate: (Route) -> Unit,
) {
    Page(title = technology.ifBlank {
        stack?.let { stackTitle(stack = it) } ?: stringResource(R.string.courses_courses)
    }, back = back) {
        items(
            courses,
            key = { contentKey<Course>(id = it.id) }, contentType = { Course::class }) { course ->
            ItemCard(
                course.title,
                educationLevelTitle(level = course.level) + " · " + course.educationType,
                course.image,
                onClick = { navigate(Route(screen = Screen.Course, id = course.id)) })
        }
    }
}
