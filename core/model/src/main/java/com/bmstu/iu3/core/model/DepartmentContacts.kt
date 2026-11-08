package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class DepartmentContacts(
    val email: String,
    val phone: String,
    val website: String,
    val address: String,
    val officeHours: String,
    val building: String,
    val latitude: Double,
    val longitude: Double,
)
