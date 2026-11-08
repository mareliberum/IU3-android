package com.bmstu.iu3.features.courses.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bmstu.iu3.core.model.AcademicTime
import com.bmstu.iu3.core.model.Plan
import com.bmstu.iu3.features.courses.R
import java.time.LocalDate

@Composable
internal fun planSeason(plan: Plan): String {
    val start = runCatching {
        LocalDate.parse(
            plan.theory.substringBefore(AcademicTime.PeriodSeparator).trim(),
            sourceDate
        )
    }.getOrNull()
    return start?.let {
        stringResource(
            if (it.monthValue >= 8) R.string.courses_autumn else R.string.courses_spring,
            it.year
        )
    }.orEmpty()
}
