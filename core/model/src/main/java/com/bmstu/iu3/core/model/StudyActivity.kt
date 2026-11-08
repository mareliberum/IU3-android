package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class StudyActivity(val code: String) {
    override fun toString(): String = code

    companion object {
        val Coursework = StudyActivity(code = "КуР")
        val CourseProject = StudyActivity(code = "КП")
        val Research = StudyActivity(code = "НИРС")
        val PracticalTraining = StudyActivity(code = "Пр")
        val Practice = StudyActivity(code = "П")
        val WorkshopPractice = StudyActivity(code = "Упм")
        val FinalPractice = StudyActivity(code = "ПП")
        val Practicum = StudyActivity(code = "Пм")
    }
}
