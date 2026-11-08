package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Timetable(
    val semester: String,
    val slots: List<Slot>,
    val groups: Map<String, Map<String, List<Lesson>>>,
)
