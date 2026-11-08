package com.bmstu.iu3.features.schedule.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bmstu.iu3.core.designsystem.AppBackground
import com.bmstu.iu3.core.designsystem.BlueHeader
import com.bmstu.iu3.core.designsystem.CompactCard
import com.bmstu.iu3.core.designsystem.EmptyState
import com.bmstu.iu3.core.designsystem.LocalNavigationInset
import com.bmstu.iu3.core.designsystem.SegmentedControl
import com.bmstu.iu3.core.designsystem.contentKey
import com.bmstu.iu3.core.model.AcademicTime
import com.bmstu.iu3.core.model.FacultyEvent
import com.bmstu.iu3.core.model.Lesson
import com.bmstu.iu3.core.model.Profile
import com.bmstu.iu3.core.model.Role
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.schedule.R
import com.bmstu.iu3.features.schedule.ui.components.ScheduleDayCard
import com.bmstu.iu3.features.schedule.ui.components.ScheduleWeekPicker
import com.bmstu.iu3.features.schedule.ui.components.weekTitle
import com.bmstu.iu3.features.schedule.ui.format.datePage
import com.bmstu.iu3.features.schedule.ui.format.pageDate
import java.time.LocalDate
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    profile: Profile,
    lessonsFor: (LocalDate) -> List<Lesson>,
    upcoming: List<FacultyEvent>,
    groupsFor: (String) -> List<String>,
    onGroup: (String) -> Unit,
    navigate: (Route) -> Unit,
) {
    val today = LocalDate.now(AcademicTime.Zone)
    val pager =
        rememberPagerState(initialPage = datePage(date = today), pageCount = { Int.MAX_VALUE })
    val scope = rememberCoroutineScope()
    val date = pageDate(page = pager.currentPage)
    var showCalendar by rememberSaveable { mutableStateOf(false) }
    var groupPicker by rememberSaveable { mutableStateOf(false) }
    var events by rememberSaveable { mutableStateOf(false) }
    var query by rememberSaveable { mutableStateOf("") }
    val dateFormat = remember { AcademicTime.DayMonth }
    val contentPadding = PaddingValues(
        start = 14.dp, end = 14.dp, top = 4.dp,
        bottom = LocalNavigationInset.current + WindowInsets.navigationBars.asPaddingValues()
            .calculateBottomPadding() + 16.dp
    )
    val selectDate: (LocalDate) -> Unit =
        { selected -> scope.launch { pager.animateScrollToPage(datePage(date = selected)) } }
    val selectWeek: (LocalDate) -> Unit =
        { selected -> scope.launch { pager.scrollToPage(datePage(date = selected)) } }

    Surface(color = AppBackground) {
        Column(Modifier.fillMaxSize()) {
            BlueHeader(
                title = today.format(dateFormat),
                subtitle = weekTitle(date = today),
                profile = { navigate(Route(screen = Screen.Profile)) })
            Box(Modifier.padding(horizontal = 14.dp, vertical = 14.dp)) {
                SegmentedControl(
                    first = stringResource(R.string.schedule_schedule),
                    second = stringResource(R.string.schedule_events),
                    secondSelected = events
                ) { events = it }
            }
            if (events) {
                LazyColumn(
                    Modifier.weight(1f).fillMaxWidth(), contentPadding = contentPadding,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (upcoming.isEmpty()) item { EmptyState(title = stringResource(R.string.schedule_no_events)) }
                    items(
                        upcoming,
                        key = { contentKey<FacultyEvent>(id = it.id) },
                        contentType = { FacultyEvent::class }) { event ->
                        val start = LocalDate.parse(event.start.take(10));
                        val end = LocalDate.parse((event.end ?: event.start).take(10))
                        val label =
                            if (start == end) start.format(dateFormat) else start.format(dateFormat) + " – " + end.format(
                                dateFormat
                            )
                        CompactCard(title = event.title, subtitle = label, image = event.image) {
                            navigate(
                                Route(
                                    screen = Screen.Event,
                                    id = event.id
                                )
                            )
                        }
                    }
                }
            } else {
                Box(Modifier.padding(start = 14.dp, end = 14.dp, top = 4.dp, bottom = 12.dp)) {
                    ScheduleWeekPicker(
                        date = date, select = selectDate, selectWeek = selectWeek,
                        hasLessons = { lessonsFor(it).isNotEmpty() },
                        openCalendar = { showCalendar = true })
                }
                HorizontalPager(
                    state = pager, modifier = Modifier.weight(1f).fillMaxWidth(),
                    key = { it }, verticalAlignment = Alignment.Top
                ) { page ->
                    val day = pageDate(page = page)
                    LazyColumn(
                        Modifier.fillMaxSize(), contentPadding = contentPadding,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            ScheduleDayCard(
                                lessons = lessonsFor(day),
                                profile = profile,
                                date = day,
                                format = dateFormat
                            )
                        }
                        if (profile.role != Role.Teacher) item {
                            TextButton({
                                groupPicker = true
                            }) { Text(profile.group.ifBlank { stringResource(R.string.schedule_choose_group) }) }
                        }
                    }
                }
            }
        }
    }
    if (showCalendar) {
        val picker = rememberDatePickerState(
            initialSelectedDateMillis = date.atStartOfDay(java.time.ZoneOffset.UTC).toInstant()
                .toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showCalendar = false },
            confirmButton = {
                TextButton({
                    picker.selectedDateMillis?.let { millis ->
                        val selected =
                            java.time.Instant.ofEpochMilli(millis).atZone(java.time.ZoneOffset.UTC)
                                .toLocalDate()
                        scope.launch { pager.scrollToPage(datePage(date = selected)) }
                    }
                    showCalendar = false
                }) { Text(stringResource(R.string.schedule_choose)) }
            },
            dismissButton = {
                TextButton({
                    showCalendar = false
                }) { Text(stringResource(R.string.schedule_cancel)) }
            }) { DatePicker(picker) }
    }
    if (groupPicker) AlertDialog(
        onDismissRequest = { groupPicker = false },
        title = { Text(stringResource(R.string.schedule_group)) },
        text = {
            Column {
                OutlinedTextField(
                    query,
                    { query = it },
                    label = { Text(stringResource(R.string.schedule_find_group)) })
                LazyColumn(Modifier.heightIn(max = 360.dp)) {
                    items(
                        groupsFor(query),
                        key = { contentKey<String>(id = it) },
                        contentType = { String::class }) { group ->
                        TextButton({ onGroup(group); groupPicker = false }) { Text(group) }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton({
                groupPicker = false
            }) { Text(stringResource(R.string.schedule_cancel)) }
        })
}
