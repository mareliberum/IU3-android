package com.bmstu.iu3.features.instructors.presentation

import com.bmstu.iu3.core.model.Catalog
import com.bmstu.iu3.core.model.StaffAssignments
import com.bmstu.iu3.features.instructors.presentation.InstructorsUiState

internal object InstructorDirectory {
    fun filter(catalog: Catalog, query: String): InstructorsUiState {
        val staff = catalog.staff.filter {
            (it.name + it.position + it.about.orEmpty() + catalog.interests[it.name].orEmpty()
                .joinToString()).contains(query, true)
        }
        val head = staff.firstOrNull(StaffAssignments::isHead)
        val deputies = staff.filter(StaffAssignments::isDeputy)
        return InstructorsUiState(
            query = query,
            staff = staff,
            head = head,
            deputies = deputies,
            others = staff.filter { it != head && it !in deputies }.sortedBy { it.name })
    }
}
