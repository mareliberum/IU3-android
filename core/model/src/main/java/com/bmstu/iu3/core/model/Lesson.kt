package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Lesson(
    val slot: Int = 0,
    val from: String = "",
    val to: String = "",
    val type: LessonType = LessonType.Seminar,
    val title: String,
    val teacher: String = "",
    val room: String = "",
    val week: LessonWeek = LessonWeek.All,
    val day: Int = 0,
    val groups: List<String> = emptyList(),
    val weeks: List<Int>? = null,
    val fromDate: String? = null,
    val toDate: String? = null,
    val fromWeek: Int? = null,
    val note: String? = null,
)
