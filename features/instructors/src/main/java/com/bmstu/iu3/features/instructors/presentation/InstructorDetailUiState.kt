package com.bmstu.iu3.features.instructors.presentation

import com.bmstu.iu3.core.model.Course
import com.bmstu.iu3.core.model.Publication
import com.bmstu.iu3.core.model.ScholarProfile
import com.bmstu.iu3.core.model.Staff
import java.time.LocalDate

data class InstructorDetailUiState(
    val person: Staff,
    val biography: String?,
    val interests: List<String>,
    val teaching: Boolean,
    val profiles: List<ScholarProfile>,
    val works: List<Publication>,
    val websites: List<String>,
    val courses: List<Course>,
    val initialDate: LocalDate,
)
