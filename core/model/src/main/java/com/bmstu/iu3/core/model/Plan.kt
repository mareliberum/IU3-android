package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Plan(
    val program: StudyProgram,
    val groups: List<String>,
    val semester: Int,
    val weeks: Int,
    val theory: String,
    val exams: String? = null,
    val research: String? = null,
    val practice: String? = null,
    val finalAttestation: String? = null,
    val holidays: String? = null,
    val disciplines: List<Discipline>,
)
