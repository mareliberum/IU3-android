package com.bmstu.iu3.features.instructors.presentation

import com.bmstu.iu3.core.model.Staff

data class InstructorsUiState(
    val query: String = "",
    val staff: List<Staff> = emptyList(),
    val head: Staff? = null,
    val deputies: List<Staff> = emptyList(),
    val others: List<Staff> = emptyList(),
)
