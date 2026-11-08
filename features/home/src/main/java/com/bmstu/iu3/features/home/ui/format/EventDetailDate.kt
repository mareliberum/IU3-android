package com.bmstu.iu3.features.home.ui.format

import com.bmstu.iu3.core.model.AcademicTime
import com.bmstu.iu3.core.model.EventLogic
import com.bmstu.iu3.core.model.FacultyEvent

internal fun eventDetailDate(event: FacultyEvent): String {
    val title = event.title.lowercase()
    val sportsMatch =
        sportsMatchPattern.containsMatchIn(title) &&
            !tournamentPattern.containsMatchIn(title)
    val formatter = if (event.allDay) AcademicTime.DayMonth else AcademicTime.DayMonthTime
    val start = EventLogic.start(event).format(formatter)
    return if (event.end == null || sportsMatch || EventLogic.end(event) == EventLogic.start(event)) start
    else start + " — " + EventLogic.end(event).format(formatter)
}

private val sportsMatchPattern = Regex("волейбол|баскетбол|футбол|матч|\\bvs\\b|принимает «")
private val tournamentPattern = Regex("турнир|кубок|первенство|соревнован")
