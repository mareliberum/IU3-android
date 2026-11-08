package com.bmstu.iu3.features.profile.presentation

import com.bmstu.iu3.core.model.Catalog
import com.bmstu.iu3.core.model.PlannedReminder
import com.bmstu.iu3.core.model.Profile
import com.bmstu.iu3.core.model.ReminderSettings

data class ProfileUiState(
    val catalog: Catalog,
    val profile: Profile,
    val reminders: ReminderSettings,
    val planned: List<PlannedReminder>,
    val alumniMeetingYear: Int,
)
