package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ProgramDisciplineGroup(
    val title: String,
    val subtitle: String,
    val items: List<String>,
)
