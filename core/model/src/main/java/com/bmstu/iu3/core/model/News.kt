package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class News(
    val id: String,
    val title: String,
    val text: String,
    val date: String,
    val images: List<String> = emptyList(),
    val likes: Int = 0,
    val views: Int = 0,
)
