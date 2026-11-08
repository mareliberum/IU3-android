package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class EventEngagement(
    val likes: Int = 0,
    val reposts: Int = 0,
    val views: Int = 0,
)
