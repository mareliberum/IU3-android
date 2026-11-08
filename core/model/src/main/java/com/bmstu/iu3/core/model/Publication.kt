package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Publication(
    val title: String,
    val year: Int,
    val type: PublicationType = PublicationType.Journal,
    val url: String? = null,
    val pinned: Boolean = false,
    val venue: String? = null,
)
