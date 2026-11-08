package com.bmstu.iu3.features.courses.presentation

import com.bmstu.iu3.core.model.Discipline
import com.bmstu.iu3.core.model.Plan

data class CoursePlanUiState(val plan: Plan, val disciplines: List<Discipline>)
