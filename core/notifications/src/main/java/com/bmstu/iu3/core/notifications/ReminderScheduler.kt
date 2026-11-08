package com.bmstu.iu3.core.notifications

import com.bmstu.iu3.core.model.PlannedReminder

interface ReminderScheduler {
    fun canScheduleExact(): Boolean
    suspend fun replace(reminders: List<PlannedReminder>)
}
