package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Honor(
    val name: String,
    val subtitle: String,
    val photoAsset: String = "",
    val detailPhotoAsset: String? = null,
    val biography: String? = null,
    val kind: HonorKind,
    val facts: List<String> = emptyList(),
    val works: List<Publication> = emptyList(),
    val role: String? = null,
    val company: String? = null,
    val graduationYear: String? = null,
    val achievements: List<String> = emptyList(),
    val accent: Boolean = false,
    val photoSize: Float = 193f,
    val photoOffset: HonorPhotoOffset = HonorPhotoOffset(),
)
