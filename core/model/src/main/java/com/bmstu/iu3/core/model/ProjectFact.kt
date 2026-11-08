package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ProjectFact(val label: String, val value: String)
