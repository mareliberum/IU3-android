package com.bmstu.iu3.core.data

import com.bmstu.iu3.core.model.Catalog
import com.bmstu.iu3.core.model.CourseCategory
import com.bmstu.iu3.core.model.ImageAssets
import com.bmstu.iu3.core.model.StaffPortraits

internal fun Catalog.withDisplayDefaults(): Catalog = copy(
    staff = staff.map { person ->
        person.copy(photo = person.photo?.takeIf { it in images }
            ?: StaffPortraits.fallback(person.name))
    },
    courses = courses.map { course ->
        val fallback = when (course.category) {
            CourseCategory.Programming, CourseCategory.Algorithms -> ImageAssets.COURSE_CODE
            CourseCategory.SoftwareEngineering -> ImageAssets.COURSE_DESIGN_METHODS
            CourseCategory.Databases -> ImageAssets.COURSE_DATABASE
            CourseCategory.Networks -> ImageAssets.COURSE_ROUTER
            CourseCategory.InformationTechnology -> ImageAssets.COURSE_INFORMATICS
            CourseCategory.ArtificialIntelligence -> ImageAssets.COURSE_ROBOT
            CourseCategory.Electronics, CourseCategory.Physics -> ImageAssets.COURSE_CIRCUIT
            else -> ImageAssets.COURSE_BOOK
        }
        course.copy(image = course.image?.takeIf { it in images } ?: fallback)
    },
)
