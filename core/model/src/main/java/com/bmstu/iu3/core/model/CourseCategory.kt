package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class CourseCategory(val code: String) {
    override fun toString(): String = code

    companion object {
        val Programming = CourseCategory(code = "Программирование")
        val Algorithms = CourseCategory(code = "Алгоритмы и структуры данных")
        val SoftwareEngineering = CourseCategory(code = "Программная инженерия")
        val Databases = CourseCategory(code = "Базы данных")
        val Networks = CourseCategory(code = "Сети и телекоммуникации")
        val InformationTechnology = CourseCategory(code = "Информационные технологии")
        val ArtificialIntelligence = CourseCategory(code = "Искусственный интеллект")
        val Electronics = CourseCategory(code = "Электроника")
        val Physics = CourseCategory(code = "Физика")
        val General = CourseCategory(code = "Общие дисциплины")
    }
}
