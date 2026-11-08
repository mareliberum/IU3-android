package com.bmstu.iu3.features.courses.presentation

import com.bmstu.iu3.core.model.Course
import com.bmstu.iu3.core.model.Staff

data class CourseUiState(
    val course: Course,
    val semesterLoads: Map<Int, Double?>,
    val plans: List<CoursePlanUiState>,
    val teachers: List<Staff>,
)
