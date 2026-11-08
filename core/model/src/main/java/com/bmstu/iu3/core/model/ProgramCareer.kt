package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ProgramCareer(val title: String, val image: String? = null)
