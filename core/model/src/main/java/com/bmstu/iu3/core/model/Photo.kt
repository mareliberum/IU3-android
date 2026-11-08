package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Photo(val image: String, val caption: String = "")
