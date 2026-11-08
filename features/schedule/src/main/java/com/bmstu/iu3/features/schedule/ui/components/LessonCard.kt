package com.bmstu.iu3.features.schedule.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmstu.iu3.core.designsystem.AppGray
import com.bmstu.iu3.core.designsystem.AppLessonChip
import com.bmstu.iu3.core.designsystem.AppLessonText
import com.bmstu.iu3.core.designsystem.AppLink
import com.bmstu.iu3.core.designsystem.AppRoomChip
import com.bmstu.iu3.core.model.Lesson
import com.bmstu.iu3.core.model.LessonType
import com.bmstu.iu3.features.schedule.R

@Composable
fun LessonCard(
    lesson: Lesson,
    teacherMode: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val type = when (lesson.type) {
        LessonType.Lecture -> stringResource(R.string.schedule_lecture)
        LessonType.Laboratory -> stringResource(R.string.schedule_lab)
        LessonType.Practice -> stringResource(R.string.schedule_practice)
        LessonType.SelfStudy -> stringResource(R.string.schedule_self_study)
        LessonType.Other -> stringResource(R.string.schedule_other)
        else -> stringResource(R.string.schedule_seminar)
    }
    Column(
        Modifier.fillMaxWidth().clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                "${lesson.from}–${lesson.to}",
                color = AppGray,
                fontSize = 13.sp,
                modifier = Modifier.weight(1f)
            )
            Surface(color = AppLessonChip, shape = RoundedCornerShape(20.dp)) {
                Text(
                    type,
                    color = AppLessonText,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                )
            }
            if (lesson.room.isNotBlank()) Surface(
                color = AppRoomChip,
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    stringResource(R.string.schedule_room, lesson.room),
                    color = AppLink,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                )
            }
        }
        Text(lesson.title, fontSize = 16.sp, lineHeight = 20.sp, fontWeight = FontWeight.SemiBold)
        Text(
            if (teacherMode) lesson.groups.joinToString() else listOfNotNull(
                lesson.teacher,
                lesson.note
            ).joinToString(" · "), color = AppGray, fontSize = 12.sp
        )
    }
}
