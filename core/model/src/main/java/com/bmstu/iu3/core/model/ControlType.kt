package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class ControlType(val code: String) {
    override fun toString(): String = code

    companion object {
        val Homework = ControlType(code = "ДЗ")
        val Checkpoint = ControlType(code = "РК")
        val Module = ControlType(code = "М")
        val Test = ControlType(code = "КР")
        val CourseProject = ControlType(code = "КП")
        val Progress = ControlType(code = "%")
    }
}
