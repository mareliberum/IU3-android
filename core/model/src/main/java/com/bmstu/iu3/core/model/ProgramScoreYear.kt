package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ProgramScoreYear(
    val year: Int,
    val budget: Int,
    val average: String,
    val paid: Int? = null,
)
