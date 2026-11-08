package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ControlEvent(val week: Int, val type: ControlType)
