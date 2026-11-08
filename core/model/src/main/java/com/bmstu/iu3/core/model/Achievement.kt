package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Achievement(
    val title: String,
    val description: String,
    val year: Int,
)
