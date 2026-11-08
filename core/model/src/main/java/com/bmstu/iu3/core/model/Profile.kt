package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val completed: Boolean = false,
    val role: Role? = null,
    val group: String = "",
    val teacher: String = "",
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val photo: String = "",
)
