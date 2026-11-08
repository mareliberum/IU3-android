package com.bmstu.iu3.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class HonorKind {
    @SerialName("legend")
    Legend,

    @SerialName("graduate")
    Graduate
}
