package com.bmstu.iu3.features.schedule.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.toLong
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bmstu.iu3.core.designsystem.WeekDays
import com.bmstu.iu3.core.designsystem.WeekNavigation
import com.bmstu.iu3.features.schedule.ui.format.weekMonday
import com.bmstu.iu3.features.schedule.ui.format.weekPage
import java.time.LocalDate
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

@Composable
internal fun ScheduleWeekPicker(
    date: LocalDate,
    select: (LocalDate) -> Unit,
    selectWeek: (LocalDate) -> Unit,
    hasLessons: (LocalDate) -> Boolean,
    openCalendar: () -> Unit,
) {
    val selectedWeek = weekPage(date = date)
    val weeks = rememberPagerState(initialPage = selectedWeek, pageCount = { Int.MAX_VALUE })
    val scope = rememberCoroutineScope()
    val latestDate by rememberUpdatedState(date)
    val latestSelectWeek by rememberUpdatedState(selectWeek)
    LaunchedEffect(selectedWeek) {
        if (!weeks.isScrollInProgress && weeks.currentPage != selectedWeek) weeks.scrollToPage(
            selectedWeek
        )
    }
    LaunchedEffect(weeks) {
        snapshotFlow { weeks.settledPage }.drop(1).collect { page ->
            if (page != weekPage(date = latestDate)) {
                latestSelectWeek(weekMonday(page = page).plusDays((latestDate.dayOfWeek.value - 1).toLong()))
            }
        }
    }
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        WeekNavigation(
            date = date,
            select = { target -> scope.launch { weeks.animateScrollToPage(weekPage(date = target)) } },
            openCalendar = openCalendar
        )
        HorizontalPager(
            state = weeks,
            modifier = Modifier.fillMaxWidth().height(74.dp),
            pageSpacing = 12.dp,
            key = { it }) { page ->
            val monday = weekMonday(page = page)
            val selectedDay = monday.plusDays((date.dayOfWeek.value - 1).toLong())
            WeekDays(
                monday = monday,
                selectedDate = selectedDay,
                select = select,
                hasLessons = hasLessons
            )
        }
    }
}
