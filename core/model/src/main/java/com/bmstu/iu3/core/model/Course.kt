package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val id: String,
    val title: String,
    val level: EducationLevel,
    val educationType: String,
    val category: CourseCategory,
    val loads: List<List<Double>>,
    val instructors: List<String>,
    val technologies: List<String> = emptyList(),
    val description: String = "",
    val image: String? = null,
)
