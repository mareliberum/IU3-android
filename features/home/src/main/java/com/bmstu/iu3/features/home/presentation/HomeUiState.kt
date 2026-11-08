package com.bmstu.iu3.features.home.presentation

import com.bmstu.iu3.core.model.Catalog
import com.bmstu.iu3.core.model.FacultyEvent
import com.bmstu.iu3.core.model.Lesson
import com.bmstu.iu3.core.model.Profile
import com.bmstu.iu3.core.model.Project
import java.time.ZonedDateTime

data class HomeUiState(
    val catalog: Catalog,
    val profile: Profile,
    val moment: ZonedDateTime,
    val offline: Boolean,
    val remaining: List<Lesson>,
    val featured: List<FacultyEvent>,
    val projects: List<Project>,
)
