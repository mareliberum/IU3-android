package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ScholarProfile(val kind: ScholarProfileKind, val url: String)
