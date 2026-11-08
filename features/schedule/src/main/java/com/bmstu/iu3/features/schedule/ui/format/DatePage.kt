package com.bmstu.iu3.features.schedule.ui.format

import androidx.compose.runtime.toLong
import com.bmstu.iu3.features.schedule.ui.components.DayPageOrigin
import java.time.LocalDate

internal fun datePage(date: LocalDate): Int =
    (DayPageOrigin.toLong() + date.toEpochDay()).coerceIn(0L, Int.MAX_VALUE.toLong() - 1).toInt()
