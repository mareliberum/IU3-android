package com.bmstu.iu3.features.home.ui.format

import com.bmstu.iu3.core.model.AcademicTime
import com.bmstu.iu3.core.model.FacultyEvent
import java.time.LocalDate

internal fun eventDate(event: FacultyEvent): String {
    val start = LocalDate.parse(event.start.take(10))
    val end = event.end?.take(10)?.let(LocalDate::parse) ?: start
    val format = AcademicTime.DayMonth
    return if (start == end) start.format(format) else if (start.month == end.month) "${start.dayOfMonth}–${
        end.format(
            format
        )
    }" else start.format(format) + " – " + end.format(format)
}
