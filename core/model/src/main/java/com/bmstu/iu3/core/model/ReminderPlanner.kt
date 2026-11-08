package com.bmstu.iu3.core.model

import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

object ReminderPlanner {
    val zone: ZoneId = AcademicTime.Zone
    fun plan(
        catalog: Catalog,
        profile: Profile,
        settings: ReminderSettings,
        now: Instant = Instant.now(),
    ): List<PlannedReminder> {
        if (!settings.enabled) return emptyList()
        val today = now.atZone(zone).toLocalDate()
        val result = mutableListOf<PlannedReminder>()
        fun add(
            date: LocalDate,
            time: LocalTime,
            type: ReminderKind,
            title: String,
            detail: String = "",
            minutes: Int = 0,
            room: String = "",
            eventStart: Instant? = null,
            allDay: Boolean = false,
            identity: String = title,
        ) {
            val instant = date.atTime(time).atZone(zone).toInstant()
            if (instant > now) result += PlannedReminder(
                key = "${type.key}:${instant.toEpochMilli()}:$identity",
                instant = instant,
                type = type,
                title = title,
                detail = detail,
                minutes = minutes,
                room = room,
                eventStart = eventStart,
                allDay = allDay
            )
        }

        val academic = profile.role in listOf(Role.Student, Role.Teacher, null)
        if (academic) {
            val days = (0..6).associate { offset ->
                val date = today.plusDays(offset.toLong()); date to StudyCalendar.lessons(
                catalog,
                profile,
                date
            )
            }
            days.forEach { (date, lessons) ->
                if (settings.lessons) lessons.filter {
                    it.type in settings.lessonTypes || it.type in setOf(
                        LessonType.Exam,
                        LessonType.Consultation
                    )
                }.forEach { lesson ->
                    val from =
                        runCatching { LocalTime.parse(lesson.from) }.getOrNull() ?: return@forEach
                    val at = date.atTime(from).minusMinutes(settings.minutes.toLong()).atZone(zone)
                        .toInstant()
                    if (at > now) result += PlannedReminder(
                        key = "${ReminderKind.Lesson.key}:$date:${lesson.from}:${lesson.title}:${lesson.room}:${lesson.groups.joinToString()}",
                        instant = at,
                        type = ReminderKind.Lesson,
                        title = lesson.title,
                        detail = if (profile.role == Role.Teacher) lesson.groups.joinToString() else lesson.teacher,
                        minutes = settings.minutes,
                        room = lesson.room
                    )
                }
                val labs = lessons.filter { it.type == LessonType.Laboratory }
                if (settings.labs && labs.isNotEmpty()) add(
                    date.minusDays(1),
                    LocalTime.of(settings.labHour, 0),
                    ReminderKind.Laboratories,
                    labs.joinToString("; ") { it.title },
                    labs.joinToString("\n") {
                        listOf(
                            it.from,
                            it.title,
                            it.room
                        ).filter(String::isNotBlank).joinToString(" · ")
                    })
                if (settings.summary && lessons.isNotEmpty() && LocalTime.of(
                        settings.summaryHour,
                        0
                    ) < LocalTime.parse(lessons.first().from)
                )
                    add(
                        date,
                        LocalTime.of(settings.summaryHour, 0),
                        ReminderKind.DaySummary,
                        "",
                        lessons.joinToString("\n") {
                            listOf(it.from, it.title, it.room).filter(String::isNotBlank)
                                .joinToString(" · ")
                        })
            }
            if (settings.controls) {
                val teacherLessons = (0..13).flatMap {
                    StudyCalendar.lessons(
                        catalog,
                        profile,
                        today.plusDays(it.toLong())
                    )
                }
                val plans =
                    catalog.plans.filter { plan -> if (profile.role == Role.Teacher) teacherLessons.any { lesson -> lesson.groups.any { it in plan.groups } } else profile.group in plan.groups }
                plans.forEach { plan ->
                    val start = runCatching {
                        LocalDate.parse(
                            plan.theory.substringBefore(AcademicTime.PeriodSeparator).trim(),
                            AcademicTime.PlanDate
                        )
                    }.getOrNull() ?: return@forEach
                    val firstMonday = start.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                    val controls = plan.disciplines.filter { discipline ->
                        profile.role != Role.Teacher || teacherLessons.any {
                            StudyPlans.normalize(it.title) == StudyPlans.normalize(discipline.title) && it.groups.any { group -> group in plan.groups }
                        }
                    }
                        .flatMap { discipline ->
                            discipline.events.filter { it.type in settings.controlTypes }
                                .map { event -> event.week to (discipline.title to event.type) }
                        }.groupBy({ it.first }, { it.second })
                    controls.forEach { (week, titles) ->
                        val monday = firstMonday.plusWeeks((week - 1).toLong())
                        val date = if (settings.controlFriday) monday.minusDays(3) else monday
                        if (date <= today.plusDays(28)) add(
                            date,
                            LocalTime.of(if (settings.controlFriday) 18 else 9, 0),
                            if (settings.controlFriday) ReminderKind.NextControls else ReminderKind.Controls,
                            titles.map { it.second }.distinct().joinToString(" · "),
                            titles.distinct().joinToString("\n") { it.first + " · " + it.second },
                            identity = "${plan.program}:${plan.semester}:$week"
                        )
                    }
                }
            }
        }
        if (settings.events) catalog.events.filter { event ->
            val text = normalize(event.title + " " + event.description)
            val duration = runCatching {
                Duration.between(
                    EventLogic.start(event),
                    EventLogic.end(event)
                ).seconds
            }.getOrDefault(0)
            EventLogic.start(event)
                .toInstant() > now && duration <= 3 * 86_400 && when (profile.role) {
                Role.Applicant -> applicantKeywords.any(text::contains)

                Role.Graduate -> graduateMeetingPattern.containsMatchIn(
                    normalize(event.title)
                )

                else -> event.source == EventSource.Featured || careerKeywords.any(text::contains)
            }
        }.sortedBy { EventLogic.start(it) }.take(10).forEach { event ->
            val start = EventLogic.start(event)
            val date = start.toLocalDate()
            val allDay = event.allDay || start.hour == 0
            add(
                date.minusDays(1),
                LocalTime.of(18, 0),
                ReminderKind.EventTomorrow,
                event.title,
                event.location,
                eventStart = start.toInstant(),
                allDay = allDay,
                identity = event.id
            )
            if (!academic && (allDay || LocalTime.of(9, 0) < start.toLocalTime())) add(
                date, LocalTime.of(9, 0), ReminderKind.EventToday, event.title, event.location,
                eventStart = start.toInstant(), allDay = allDay, identity = event.id
            )
        }
        return result.distinctBy { it.key }.sortedBy { it.instant }.take(60)
    }

    private fun normalize(value: String) = value.lowercase().replace('ё', 'е')

    private val graduateMeetingPattern =
        Regex("встреч[аеиуы]\\s+(с\\s+)?выпускник|слет выпускников")
    private val applicantKeywords = listOf(
        "день открытых дверей",
        "дни открытых дверей",
        "поступающ",
        "абитуриент",
        "профориентац"
    )
    private val careerKeywords = listOf(
        "хакатон",
        "ярмарка вакансий",
        "ярмарку вакансий",
        "день карьеры",
        "дни карьеры"
    )
}
