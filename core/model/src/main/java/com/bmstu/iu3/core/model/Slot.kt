package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Slot(
    val n: Int,
    val from: String,
    val to: String,
)
