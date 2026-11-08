package com.bmstu.iu3.features.courses.ui.format

import com.bmstu.iu3.core.model.Plan
import com.bmstu.iu3.core.model.StudyPlans

internal fun weekRange(plan: Plan, week: Int): String {
    val start = StudyPlans.weekStart(plan, week) ?: return ""
    return planDateRange(start = start, end = start.plusDays(5))
}
