package com.bmstu.iu3.features.instructors.domain

import com.bmstu.iu3.core.model.Catalog
import com.bmstu.iu3.core.model.Publication
import com.bmstu.iu3.core.model.Staff

internal fun publications(person: Staff, catalog: Catalog): List<Publication> {
    val scholar = catalog.scholars[person.name]
    val listed = catalog.webFaculty[person.name]?.publications.orEmpty().map { work ->
        work.copy(url = scholar?.links?.get(work.title))
    }
    return (listed + scholar?.extra.orEmpty()).distinctBy { it.title.lowercase() }
        .sortedWith(compareByDescending<Publication> { it.pinned }.thenByDescending { it.year })
}
