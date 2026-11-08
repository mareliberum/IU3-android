package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ScholarEntry(
    val profiles: List<ScholarProfile> = emptyList(),
    val links: Map<String, String> = emptyMap(),
    val extra: List<Publication> = emptyList(),
)
