package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Discipline(
    val title: String,
    val dept: String = "",
    val ze: Double = 0.0,
    val hours: Double = 0.0,
    val contact: ContactHours = ContactHours(),
    val attestation: Attestation = Attestation.None,
    val extra: StudyActivity? = null,
    val points: List<Double> = emptyList(),
    val events: List<ControlEvent> = emptyList(),
)
