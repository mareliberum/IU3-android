package com.bmstu.iu3.features.courses.ui.format

import com.bmstu.iu3.features.courses.ui.components.shortDate
import java.time.LocalDate

internal fun planDateRange(start: LocalDate, end: LocalDate): String =
    if (start.month == end.month && start.year == end.year) "${start.dayOfMonth}–${
        end.format(
            shortDate
        )
    }"
    else "${start.format(shortDate)} – ${end.format(shortDate)}"
