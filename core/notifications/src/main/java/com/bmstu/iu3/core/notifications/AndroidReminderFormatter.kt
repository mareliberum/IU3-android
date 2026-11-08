package com.bmstu.iu3.core.notifications

import android.content.Context
import com.bmstu.iu3.core.model.AcademicTime
import com.bmstu.iu3.core.model.PlannedReminder
import com.bmstu.iu3.core.model.ReminderKind
import com.bmstu.iu3.core.model.ReminderPlanner
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class AndroidReminderFormatter @Inject constructor(
    @ApplicationContext private val context: Context,
) : ReminderFormatter {
    override fun title(reminder: PlannedReminder): String = when (reminder.type) {
        ReminderKind.Lesson -> context.getString(
            R.string.notifications_lesson_reminder,
            reminder.minutes,
            reminder.title
        )

        ReminderKind.Laboratories -> context.getString(R.string.notifications_labs_title)
        ReminderKind.EventTomorrow -> context.getString(
            R.string.notifications_tomorrow_reminder,
            reminder.title
        )

        ReminderKind.DaySummary -> context.getString(R.string.notifications_day_summary)
        ReminderKind.Controls -> context.getString(
            R.string.notifications_control_reminder,
            reminder.title
        )

        ReminderKind.NextControls -> context.getString(
            R.string.notifications_next_control_reminder,
            reminder.title
        )

        else -> context.getString(R.string.notifications_today_reminder, reminder.title)
    }

    override fun body(reminder: PlannedReminder): String {
        if (reminder.type == ReminderKind.Lesson) return listOfNotNull(
            reminder.room.takeIf(String::isNotBlank)
                ?.let { context.getString(R.string.notifications_room, it) },
            reminder.detail.takeIf(String::isNotBlank),
        ).joinToString(" · ")
        val eventStart = reminder.eventStart
        if (eventStart != null) {
            val time = if (reminder.allDay) context.getString(R.string.notifications_all_day)
            else context.getString(
                R.string.notifications_starts_at, eventStart.atZone(ReminderPlanner.zone)
                    .format(AcademicTime.Clock)
            )
            return listOf(time, reminder.detail).filter(String::isNotBlank).joinToString(" · ")
        }
        return reminder.detail
    }


}
