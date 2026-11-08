package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class EducationLevel(val code: String) {
    override fun toString(): String = code

    companion object {
        val Bachelor = EducationLevel(code = "Бакалавр")
        val Master = EducationLevel(code = "Магистр")
    }
}
