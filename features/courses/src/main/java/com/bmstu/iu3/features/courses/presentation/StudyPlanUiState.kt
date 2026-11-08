package com.bmstu.iu3.features.courses.presentation

import com.bmstu.iu3.core.model.Plan
import com.bmstu.iu3.core.model.StudyControl
import com.bmstu.iu3.core.model.StudyProgram

data class StudyPlanUiState(
    val program: StudyProgram,
    val semester: Int,
    val programs: List<StudyProgram>,
    val semesters: List<Int>,
    val plan: Plan?,
    val currentWeek: Int?,
    val controls: List<Pair<Int, List<StudyControl>>>,
    val courseIds: Map<String, String>,
)
