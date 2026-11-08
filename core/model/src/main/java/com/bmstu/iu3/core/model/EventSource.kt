package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class EventSource(val code: String) {
    override fun toString(): String = code

    companion object {
        val Featured = EventSource(code = "featured")
        val University = EventSource(code = "bmstu")
        val Sports = EventSource(code = "sport")
    }
}
