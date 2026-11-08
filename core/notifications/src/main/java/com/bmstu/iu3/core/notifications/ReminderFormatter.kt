package com.bmstu.iu3.core.notifications

import com.bmstu.iu3.core.model.PlannedReminder

interface ReminderFormatter {
    fun title(reminder: PlannedReminder): String
    fun body(reminder: PlannedReminder): String
}
