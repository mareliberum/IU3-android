package com.bmstu.iu3.core.model

import java.time.Instant

data class PlannedReminder(
    val key: String,
    val instant: Instant,
    val type: ReminderKind,
    val title: String,
    val detail: String = "",
    val minutes: Int = 0,
    val room: String = "",
    val eventStart: Instant? = null,
    val allDay: Boolean = false,
)
