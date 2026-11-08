package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class FaceFrame(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
)
