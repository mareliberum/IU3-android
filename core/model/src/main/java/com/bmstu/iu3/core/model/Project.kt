package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val title: String,
    val subtitle: String,
    val details: String = "",
    val image: String? = null,
    val tint: ProjectTint = ProjectTint.Blue,
    val gallery: List<Photo> = emptyList(),
    val galleryTitle: String = "",
    val facts: List<ProjectFact> = emptyList(),
    val links: List<ProjectLink> = emptyList(),
    val featured: Boolean = false,
)
