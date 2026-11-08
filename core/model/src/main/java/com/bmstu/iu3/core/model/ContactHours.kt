package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ContactHours(
    val total: Double = 0.0,
    val lectures: Double = 0.0,
    val seminars: Double = 0.0,
    val labs: Double = 0.0,
    val other: Double = 0.0,
)
