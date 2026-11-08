package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class PublicationType(val code: String) {
    override fun toString(): String = code

    companion object {
        val Journal = PublicationType(code = "journal")
        val Conference = PublicationType(code = "conference")
        val Book = PublicationType(code = "book")
    }
}
