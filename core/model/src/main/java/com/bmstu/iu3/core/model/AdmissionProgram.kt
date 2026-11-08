package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class AdmissionProgram(
    val id: AdmissionProgramId,
    val level: String,
    val shortTitle: String,
    val title: String,
    val tagline: String,
    val summary: String,
    val heroImage: String,
    val tags: List<String>,
    val badges: List<String>,
    val stats: List<ProgramStat>,
    val perks: List<String>,
    val codes: List<String>,
    val sections: List<ProgramSection>,
    val careers: List<ProgramCareer>,
    val callToAction: ProgramCallToAction,
    val professions: List<String> = emptyList(),
)
