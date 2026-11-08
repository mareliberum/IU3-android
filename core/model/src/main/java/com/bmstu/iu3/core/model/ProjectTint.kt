package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class ProjectTint(val code: String) {
    override fun toString(): String = code

    companion object {
        val Blue = ProjectTint(code = "blue")
        val Black = ProjectTint(code = "black")
    }
}
