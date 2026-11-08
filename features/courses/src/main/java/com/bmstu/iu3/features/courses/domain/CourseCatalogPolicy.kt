package com.bmstu.iu3.features.courses.domain

import com.bmstu.iu3.core.model.Catalog
import com.bmstu.iu3.core.model.Course
import com.bmstu.iu3.core.model.CourseCategory
import com.bmstu.iu3.core.model.TechnologyStack

internal object CourseCatalogPolicy {
    private val categories = mapOf(
        TechnologyStack.Programming to setOf(
            CourseCategory.Programming,
            CourseCategory.Algorithms,
            CourseCategory.SoftwareEngineering
        ),
        TechnologyStack.InformationTechnology to setOf(
            CourseCategory.InformationTechnology,
            CourseCategory.Databases
        ),
        TechnologyStack.Networks to setOf(CourseCategory.Networks),
        TechnologyStack.ArtificialIntelligence to setOf(CourseCategory.ArtificialIntelligence),
        TechnologyStack.Electronics to setOf(CourseCategory.Electronics),
    )

    fun courses(
        catalog: Catalog,
        stack: TechnologyStack?,
        technology: String,
    ): List<Course> {
        val all =
            catalog.courses.filter { stack == null || it.category in categories[stack].orEmpty() }
        return all.filter { c -> c.technologies.any { it.equals(technology, true) } }
            .ifEmpty { all }.sortedBy { it.loads.firstOrNull()?.firstOrNull() ?: 0.0 }
    }
}
