package com.bmstu.iu3.features.schedule.ui.format

import androidx.compose.runtime.toLong
import com.bmstu.iu3.features.schedule.ui.components.WeekPageOrigin
import java.time.LocalDate

internal fun weekPage(date: LocalDate): Int =
    (WeekPageOrigin.toLong() + Math.floorDiv(date.toEpochDay() - 4, 7L))
        .coerceIn(0L, Int.MAX_VALUE.toLong() - 1).toInt()
