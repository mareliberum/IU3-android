package com.bmstu.iu3.features.schedule.ui.format

import androidx.compose.runtime.toLong
import com.bmstu.iu3.features.schedule.ui.components.DayPageOrigin
import java.time.LocalDate

internal fun pageDate(page: Int): LocalDate = LocalDate.ofEpochDay(page.toLong() - DayPageOrigin)
