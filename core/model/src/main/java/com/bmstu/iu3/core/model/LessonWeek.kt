package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class LessonWeek(val code: String) {
    override fun toString(): String = code

    companion object {
        val All = LessonWeek(code = "all")
        val Numerator = LessonWeek(code = "numerator")
        val Denominator = LessonWeek(code = "denominator")
    }
}
