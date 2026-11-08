package com.bmstu.iu3.features.courses.ui.format

import com.bmstu.iu3.features.courses.ui.components.sourceDate
import java.time.LocalDate

internal fun planPeriod(value: String): String = value.split(';').joinToString(", ") { period ->
    val dates = planDatePattern.findAll(period).map { it.value }.toList()
    runCatching {
        planDateRange(
            start = LocalDate.parse(dates[0], sourceDate),
            end = LocalDate.parse(dates[1], sourceDate)
        )
    }.getOrDefault(period)
}

private val planDatePattern = Regex("\\d{2}\\.\\d{2}\\.\\d{4}")
