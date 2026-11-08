package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Staff(
    val name: String,
    val position: String,
    val about: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val photo: String? = null,
    val web: List<String> = emptyList(),
)
