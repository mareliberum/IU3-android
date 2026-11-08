package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class FacultyDetail(
    val biography: String,
    val interests: List<String>,
    val disciplines: List<String>,
    val publications: List<Publication>,
)
