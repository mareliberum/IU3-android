package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ProgramSection(
    val type: ProgramSectionType,
    val title: String = "",
    val subtitle: String? = null,
    val steps: List<ProgramStep> = emptyList(),
    val cards: List<ProgramCard> = emptyList(),
    val groups: List<ProgramDisciplineGroup> = emptyList(),
    val items: List<String> = emptyList(),
    val companies: List<ProgramCompany> = emptyList(),
    val minimum: List<ProgramStat> = emptyList(),
    val passing: List<ProgramStat> = emptyList(),
    val years: List<ProgramScoreYear> = emptyList(),
)
