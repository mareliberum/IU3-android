package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class LessonType(val code: String) {
    override fun toString(): String = code

    companion object {
        val Lecture = LessonType(code = "lecture")
        val Seminar = LessonType(code = "seminar")
        val Laboratory = LessonType(code = "lab")
        val Practice = LessonType(code = "practice")
        val SelfStudy = LessonType(code = "self_study")
        val Other = LessonType(code = "other")
        val Exam = LessonType(code = "exam")
        val Consultation = LessonType(code = "consultation")
    }
}
