package com.bmstu.iu3.core.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

object StudyCalendar {
    fun semesterStart(date: LocalDate): LocalDate {
        val year = date.year
        return if (date.monthValue >= 7) LocalDate.of(year, 9, 1)
        else if (date.monthValue == 1) LocalDate.of(year - 1, 9, 1)
        else LocalDate.of(year, 2, 9).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    }

    fun week(date: LocalDate): Int {
        val start = semesterStart(date).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        return (ChronoUnit.WEEKS.between(
            start,
            date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        ) + 1).toInt()
    }

    fun isTeaching(date: LocalDate): Boolean = date >= semesterStart(date) &&
        week(date) in 1..18 && date.monthValue !in setOf(
        1,
        7,
        8
    ) && date.dayOfWeek != DayOfWeek.SUNDAY

    fun teacherKey(name: String): String {
        val words =
            name.lowercase().replace('ё', 'е').split(nameWordSeparator).filter(String::isNotBlank)
        return words.firstOrNull().orEmpty() + words.drop(1).take(2).joinToString("") { it.take(1) }
    }

    fun lessons(
        catalog: Catalog,
        profile: Profile,
        date: LocalDate,
    ): List<Lesson> {
        if (!isTeaching(date)) return emptyList()
        val expected =
            (if (date.monthValue >= 7) AcademicSeason.Autumn else AcademicSeason.Spring).semester(
                date.year
            )
        if (catalog.timetable.semester != expected) return emptyList()
        val day = date.dayOfWeek.value.toString()
        val teacher = teacherKey(profile.teacher)
        val collected = catalog.timetable.groups.flatMap { (group, days) ->
            if (profile.group.isNotBlank() && profile.group != group) emptyList()
            else days[day].orEmpty().filter {
                profile.group.isNotBlank() ||
                    (teacher.isNotEmpty() && it.teacher.split(',')
                        .any { n -> teacherKey(n) == teacher })
            }.map { it.copy(groups = listOf(group), day = date.dayOfWeek.value) }
        }
        val labs = catalog.labs.filter {
            it.day == date.dayOfWeek.value &&
                (profile.group in it.groups || (profile.group.isBlank() && teacher.isNotEmpty() &&
                    it.teacher.split(',').any { n -> teacherKey(n) == teacher }))
        }
        return (collected + labs).filter { lesson ->
            (lesson.week == LessonWeek.All || lesson.week == if (week(date) % 2 == 1) LessonWeek.Numerator else LessonWeek.Denominator) &&
                (lesson.weeks == null || week(date) in lesson.weeks) && (lesson.fromWeek == null || week(
                date
            ) >= lesson.fromWeek) &&
                (lesson.fromDate == null || date >= LocalDate.parse(lesson.fromDate)) &&
                (lesson.toDate == null || date <= LocalDate.parse(lesson.toDate))
        }.map { lesson ->
            val slot = catalog.timetable.slots.firstOrNull { it.n == lesson.slot }
            lesson.copy(
                from = lesson.from.ifBlank { slot?.from.orEmpty() },
                to = lesson.to.ifBlank { slot?.to.orEmpty() })
        }.let { lessons ->
            val activeLabs = lessons.filter { it in labs }
            lessons.filter { lesson ->
                lesson in activeLabs || activeLabs.none { lab ->
                    lesson.groups.any { it in lab.groups } && lesson.from < lab.to && lesson.to > lab.from
                }
            }
        }.groupBy { listOf(it.title, it.from, it.to, it.room, it.type, it.teacher) }
            .values.map { group ->
                group.first().copy(groups = group.flatMap { it.groups }.distinct().sorted())
            }
            .sortedBy { it.from }
    }

    fun alumniMeeting(year: Int): LocalDate = LocalDate.of(year, 4, 12)
        .with(TemporalAdjusters.next(DayOfWeek.SATURDAY))

    private val nameWordSeparator = Regex("[^\\p{L}]+")
}
