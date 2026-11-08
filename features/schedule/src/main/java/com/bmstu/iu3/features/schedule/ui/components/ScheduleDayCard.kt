package com.bmstu.iu3.features.schedule.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmstu.iu3.core.designsystem.AppGray
import com.bmstu.iu3.core.designsystem.AppSeparator
import com.bmstu.iu3.core.designsystem.AppSurface
import com.bmstu.iu3.core.designsystem.EmptyState
import com.bmstu.iu3.core.model.Lesson
import com.bmstu.iu3.core.model.Profile
import com.bmstu.iu3.core.model.Role
import com.bmstu.iu3.features.schedule.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
internal fun ScheduleDayCard(
    lessons: List<Lesson>,
    profile: Profile,
    date: LocalDate,
    format: DateTimeFormatter,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, AppSeparator),
        color = AppSurface
    ) {
        Column(Modifier.fillMaxWidth()) {
            Text(
                date.format(format), color = AppGray, fontSize = 13.sp,
                modifier = Modifier.padding(start = 16.dp, top = 30.dp, bottom = 10.dp)
            )
            HorizontalDivider(color = AppSeparator)
            if (lessons.isEmpty()) EmptyState(
                title = if (profile.group.isBlank() && profile.teacher.isBlank())
                    stringResource(R.string.schedule_select_group) else stringResource(R.string.schedule_no_lessons)
            )
            lessons.forEachIndexed { index, lesson ->
                LessonCard(lesson = lesson, teacherMode = profile.role == Role.Teacher)
                if (index < lessons.lastIndex) HorizontalDivider(color = AppSeparator)
            }
        }
    }
}
