package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class FacultyEvent(
    val id: String,
    val title: String,
    val start: String,
    val end: String? = null,
    val location: String = "",
    val organizer: String = "",
    val url: String = "",
    val tags: List<String> = emptyList(),
    val description: String = "",
    val image: String? = null,
    val source: EventSource = EventSource.Featured,
    val allDay: Boolean = false,
    val registrationURL: String? = null,
    val engagement: EventEngagement? = null,
)
