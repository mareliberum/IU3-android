package com.bmstu.iu3.features.home.presentation

import com.bmstu.iu3.core.model.FacultyEvent

data class EventsUiState(val query: String = "", val events: List<FacultyEvent> = emptyList())
