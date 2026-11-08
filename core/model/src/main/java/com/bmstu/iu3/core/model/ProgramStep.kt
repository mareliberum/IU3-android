package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ProgramStep(
    val stage: String,
    val title: String,
    val text: String,
)
