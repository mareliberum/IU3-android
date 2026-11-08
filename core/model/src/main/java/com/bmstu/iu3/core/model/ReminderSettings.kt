package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ReminderSettings(
    val enabled: Boolean = false,
    val lessons: Boolean = true,
    val minutes: Int = 15,
    val lessonTypes: Set<LessonType> = setOf(
        LessonType.Lecture,
        LessonType.Seminar,
        LessonType.Laboratory
    ),
    val labs: Boolean = true,
    val labHour: Int = 19,
    val summary: Boolean = false,
    val summaryHour: Int = 8,
    val controls: Boolean = true,
    val controlFriday: Boolean = false,
    val controlTypes: Set<ControlType> = setOf(
        ControlType.Checkpoint,
        ControlType.Module,
        ControlType.Test
    ),
    val events: Boolean = true,
)
