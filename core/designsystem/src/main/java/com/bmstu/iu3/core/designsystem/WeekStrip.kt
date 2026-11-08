package com.bmstu.iu3.core.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmstu.iu3.core.model.AcademicTime
import com.bmstu.iu3.core.model.StudyCalendar
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Composable
fun studyWeekTitle(date: LocalDate): String {
    val week = StudyCalendar.week(date)
    return when {
        date < StudyCalendar.semesterStart(date) -> stringResource(R.string.ds_before_semester)
        week !in 1..18 || date.monthValue == 1 -> stringResource(R.string.ds_after_semester)
        else -> stringResource(
            R.string.ds_week_title, week,
            stringResource(if (week % 2 == 1) R.string.ds_numerator else R.string.ds_denominator)
        )
    }
}

@Composable
fun WeekStrip(
    date: LocalDate,
    select: (LocalDate) -> Unit,
    hasLessons: (LocalDate) -> Boolean,
    openCalendar: (() -> Unit)? = null,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        WeekNavigation(date = date, select = select, openCalendar = openCalendar)
        WeekDays(
            monday = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
            selectedDate = date,
            select = select,
            hasLessons = hasLessons
        )
    }
}

@Composable
fun WeekNavigation(
    date: LocalDate,
    select: (LocalDate) -> Unit,
    openCalendar: (() -> Unit)? = null,
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FilledIconButton(
            { select(date.minusWeeks(1)) }, modifier = Modifier.size(32.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = AppCard,
                contentColor = AppLink
            )
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.ds_previous_week))
        }
        if (openCalendar == null) Text(
            studyWeekTitle(date = date),
            color = AppGray,
            fontSize = 13.sp
        )
        else TextButton(openCalendar) {
            Text(
                studyWeekTitle(date = date),
                color = AppGray,
                fontSize = 13.sp
            )
        }
        FilledIconButton(
            { select(date.plusWeeks(1)) }, modifier = Modifier.size(32.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = AppCard,
                contentColor = AppLink
            )
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, stringResource(R.string.ds_next_week))
        }
    }
}

@Composable
fun WeekDays(
    monday: LocalDate,
    selectedDate: LocalDate,
    select: (LocalDate) -> Unit,
    hasLessons: (LocalDate) -> Boolean,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
        val locale = AcademicTime.Russian
        (0..6).forEach { offset ->
            val day = monday.plusDays(offset.toLong());
            val active = day == selectedDate
            val lessons = hasLessons(day)
            val fullDate = day.format(AcademicTime.FullWeekdayDate)
            Surface(
                onClick = { select(day) },
                modifier = Modifier.weight(1f).height(74.dp).semantics {
                    this.selected = active; contentDescription = fullDate
                },
                color = if (active) AppBlue else AppCard,
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    Modifier.padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        day.format(AcademicTime.ShortWeekday).uppercase(),
                        fontSize = 11.sp,
                        lineHeight = 14.sp,
                        color = if (active) Color.White.copy(alpha = .8f) else AppGray
                    )
                    Text(
                        day.dayOfMonth.toString(),
                        fontSize = 20.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (active) Color.White else AppText
                    )
                    Box(
                        Modifier.size(4.dp).background(
                            if (lessons) {
                                if (active) Color.White else AppLink
                            } else Color.Transparent, CircleShape
                        )
                    )
                }
            }
        }
    }
}
