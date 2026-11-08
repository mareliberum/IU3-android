package com.bmstu.iu3.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TechnologyStack {
    @SerialName("Программирование")
    Programming,

    @SerialName("Информационные технологии")
    InformationTechnology,

    @SerialName("Сети")
    Networks,

    @SerialName("Искусственный интеллект")
    ArtificialIntelligence,

    @SerialName("Электроника")
    Electronics,
}
