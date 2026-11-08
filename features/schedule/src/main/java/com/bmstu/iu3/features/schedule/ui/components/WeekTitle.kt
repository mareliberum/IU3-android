package com.bmstu.iu3.features.schedule.ui.components

import androidx.compose.runtime.Composable
import com.bmstu.iu3.core.designsystem.studyWeekTitle
import java.time.LocalDate

@Composable
fun weekTitle(date: LocalDate): String = studyWeekTitle(date = date)
