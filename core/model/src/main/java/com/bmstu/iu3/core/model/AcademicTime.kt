package com.bmstu.iu3.core.model

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object AcademicTime {
    val Zone: ZoneId = ZoneId.of("Europe/Moscow")
    val Russian: Locale = Locale.forLanguageTag("ru")
    val DayMonth: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM", Russian)
    val ShortDayMonth: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMM", Russian)
    val FullWeekdayDate: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM", Russian)
    val ShortWeekday: DateTimeFormatter = DateTimeFormatter.ofPattern("EE", Russian)
    val PlanDate: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val DayMonthTime: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM, HH:mm", Russian)
    val Clock: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    const val PeriodSeparator = " - "
}
