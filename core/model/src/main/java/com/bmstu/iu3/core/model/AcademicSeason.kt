package com.bmstu.iu3.core.model

enum class AcademicSeason(val code: String) {
    Autumn("Осень"), Spring("Весна");

    fun semester(year: Int): String = "$code $year"
}
