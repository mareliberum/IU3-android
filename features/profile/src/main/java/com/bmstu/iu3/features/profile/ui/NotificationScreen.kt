package com.bmstu.iu3.features.profile.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role as AccessibilityRole
import androidx.compose.ui.unit.dp
import com.bmstu.iu3.core.designsystem.AppGray
import com.bmstu.iu3.core.designsystem.ItemCard
import com.bmstu.iu3.core.designsystem.Page
import com.bmstu.iu3.core.designsystem.Section
import com.bmstu.iu3.core.designsystem.contentKey
import com.bmstu.iu3.core.model.AcademicTime
import com.bmstu.iu3.core.model.ControlType
import com.bmstu.iu3.core.model.LessonType
import com.bmstu.iu3.core.model.PlannedReminder
import com.bmstu.iu3.core.model.ReminderSettings
import com.bmstu.iu3.core.model.Role
import com.bmstu.iu3.features.profile.R
import com.bmstu.iu3.features.profile.ui.components.SettingToggle

@Composable
fun NotificationScreen(
    settings: ReminderSettings,
    role: Role?,
    granted: Boolean,
    back: () -> Unit,
    save: (ReminderSettings) -> Unit,
    planned: List<PlannedReminder>,
    requestPermission: () -> Unit,
    openSettings: () -> Unit,
    alumniMeetingYear: Int,
    exactAlarms: Boolean,
    openExactAlarms: () -> Unit,
) {
    Page(title = stringResource(R.string.profile_notifications), back = back) {
        item {
            SettingToggle(
                title = stringResource(R.string.profile_reminders),
                checked = settings.enabled && granted
            ) { enabled ->
                if (enabled && !granted) requestPermission() else save(settings.copy(enabled = enabled))
            }
        }
        if (!granted) item {
            Text(stringResource(R.string.profile_permission))
            TextButton(openSettings) { Text(stringResource(R.string.profile_open_settings)) }
        }
        if (settings.enabled && granted) {
            if (!exactAlarms) item {
                Text(stringResource(R.string.profile_exact_hint), color = AppGray)
                TextButton(openExactAlarms) { Text(stringResource(R.string.profile_exact_allow)) }
            }
            if (role == Role.Student || role == Role.Teacher || role == null) {
                item {
                    SettingToggle(
                        title = stringResource(R.string.profile_lessons),
                        checked = settings.lessons
                    ) { save(settings.copy(lessons = it)) }
                    Row(
                        Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf(5, 10, 15, 30, 60).forEach { minutes ->
                            FilterChip(
                                settings.minutes == minutes,
                                { save(settings.copy(minutes = minutes)) },
                                { Text(stringResource(R.string.profile_minutes, minutes)) })
                        }
                    }
                }
                item {
                    val types = listOf(
                        LessonType.Lecture to stringResource(R.string.profile_type_lecture),
                        LessonType.Seminar to stringResource(R.string.profile_type_seminar),
                        LessonType.Laboratory to stringResource(R.string.profile_type_lab)
                    )
                    types.forEach { (type, label) ->
                        val checked = type in settings.lessonTypes
                        Row(
                            Modifier.fillMaxWidth().heightIn(min = 48.dp).toggleable(
                                checked,
                                role = AccessibilityRole.Checkbox,
                                onValueChange = { selected ->
                                    val updated =
                                        if (selected) settings.lessonTypes + type else settings.lessonTypes - type
                                    if (updated.isNotEmpty()) save(settings.copy(lessonTypes = updated))
                                }), verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(label, Modifier.weight(1f))
                            Checkbox(checked, onCheckedChange = null)
                        }
                    }
                }
                item {
                    SettingToggle(
                        title = stringResource(R.string.profile_labs),
                        checked = settings.labs
                    ) {
                        save(
                            settings.copy(labs = it)
                        )
                    }
                    Row(
                        Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        (18..21).forEach { hour ->
                            FilterChip(
                                settings.labHour == hour,
                                { save(settings.copy(labHour = hour)) },
                                { Text("$hour:00") })
                        }
                    }
                }
                item {
                    SettingToggle(
                        title = stringResource(R.string.profile_summary),
                        checked = settings.summary
                    ) { save(settings.copy(summary = it)) }
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        (7..9).forEach { hour ->
                            FilterChip(
                                settings.summaryHour == hour,
                                { save(settings.copy(summaryHour = hour)) },
                                { Text("$hour:00") })
                        }
                    }
                }
                item {
                    SettingToggle(
                        title = stringResource(R.string.profile_controls),
                        checked = settings.controls
                    ) { save(settings.copy(controls = it)) }
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(
                            ControlType.Checkpoint,
                            ControlType.Module,
                            ControlType.Test,
                            ControlType.Homework
                        ).forEach { type ->
                            FilterChip(type in settings.controlTypes, {
                                val updated =
                                    if (type in settings.controlTypes) settings.controlTypes - type else settings.controlTypes + type
                                if (updated.isNotEmpty()) save(settings.copy(controlTypes = updated))
                            }, { Text(type.code) })
                        }
                    }
                    TextButton({ save(settings.copy(controlFriday = !settings.controlFriday)) }) {
                        Text(
                            if (settings.controlFriday) stringResource(R.string.profile_friday) else stringResource(
                                R.string.profile_monday
                            )
                        )
                    }
                }
            }
            item {
                SettingToggle(
                    title = when (role) {
                        Role.Applicant -> stringResource(R.string.profile_open_days)
                        Role.Graduate -> stringResource(R.string.profile_meetings)
                        else -> stringResource(R.string.profile_important)
                    }, checked = settings.events
                ) { save(settings.copy(events = it)) }
            }
            item { Section(title = stringResource(R.string.profile_upcoming)) }
            if (planned.isEmpty()) item {
                Text(
                    stringResource(R.string.profile_no_upcoming),
                    color = AppGray
                )
            }
            if (planned.isEmpty() && role == Role.Graduate) item {
                Text(
                    stringResource(R.string.profile_alumni_hint, alumniMeetingYear),
                    color = AppGray
                )
            }
            items(
                planned.take(5),
                key = { contentKey<PlannedReminder>(id = it.key) },
                contentType = { PlannedReminder::class }) { reminder ->
                val date = reminder.instant.atZone(AcademicTime.Zone)
                ItemCard(
                    title = reminder.title,
                    subtitle = date.format(
                        AcademicTime.DayMonthTime
                    ) + "\n" + reminder.detail
                )
            }
        }
    }
}
