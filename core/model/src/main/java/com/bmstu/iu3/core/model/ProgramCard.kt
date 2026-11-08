package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ProgramCard(
    val title: String,
    val text: String,
    val image: String? = null,
)
