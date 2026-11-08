package com.bmstu.iu3.features.schedule.ui.format

import androidx.compose.runtime.toLong
import com.bmstu.iu3.features.schedule.ui.components.WeekPageOrigin
import java.time.LocalDate

internal fun weekMonday(page: Int): LocalDate =
    LocalDate.ofEpochDay(4L + (page.toLong() - WeekPageOrigin) * 7L)
