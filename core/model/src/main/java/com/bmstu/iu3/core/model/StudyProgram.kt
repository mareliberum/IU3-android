package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class StudyProgram(val code: String) {
    override fun toString(): String = code
    val isMaster: Boolean get() = code.startsWith(MASTER_PREFIX)

    companion object {
        val Unspecified = StudyProgram(code = "")
        private const val MASTER_PREFIX = "09.04"
        val Bachelor = StudyProgram(code = "09.03.02/01")
        val InternationalBachelor = StudyProgram(code = "09.03.02/71")
        val Master = StudyProgram(code = "09.04.02/02")
        val GameDevelopment = StudyProgram(code = "09.04.02/03")
    }
}
