package com.bmstu.iu3.core.model

import java.time.LocalDate
import java.time.DayOfWeek
import java.time.temporal.TemporalAdjusters

object StudyPlans {
    fun normalize(title: String): String {
        val key = title.replace(electivePrefixPattern, "").lowercase()
            .replace('ё', 'е').filter(Char::isLetterOrDigit)
        return titleAliases[key] ?: key
    }

    fun forGroup(catalog: Catalog, group: String): Plan? {
        val name = group.uppercase().replace(" ", "")
        val digits = name.substringAfter('-', "").takeWhile(Char::isDigit)
        val semester = digits.firstOrNull()?.digitToIntOrNull() ?: return null
        val program = when {
            name.endsWith(StudentGroups.MasterSuffix) -> if (digits.getOrNull(1) == '2') StudyProgram.GameDevelopment else StudyProgram.Master
            name.startsWith(StudentGroups.InternationalPrefix) -> StudyProgram.InternationalBachelor
            name.startsWith(StudentGroups.RegularPrefix) -> StudyProgram.Bachelor
            else -> return null
        }
        return catalog.plans.firstOrNull { it.program == program && it.semester == semester }
    }

    fun matchesCourse(course: Course, discipline: Discipline): Boolean {
        val coursework = course.title.contains(COURSEWORK_MARKER, true)
        val title = normalize(
            course.title.replace(
                courseworkSuffixPattern,
                ""
            )
        )
        return normalize(discipline.title) == title && coursework ==
            (discipline.extra == StudyActivity.Coursework && discipline.contact.lectures == 0.0 && discipline.contact.labs == 0.0)
    }

    fun forCourse(
        catalog: Catalog,
        course: Course,
        group: String,
    ): List<Plan> =
        catalog.plans.filter { plan ->
            (plan.program.isMaster == (course.level == EducationLevel.Master)) &&
                (plan.program == StudyProgram.InternationalBachelor) == group.startsWith(
                StudentGroups.InternationalDepartment,
                true
            ) &&
                plan.disciplines.any { matchesCourse(course, it) }
        }

    fun currentWeek(plan: Plan, date: LocalDate): Int? {
        val first = weekStart(plan, 1) ?: return null
        val end = runCatching {
            LocalDate.parse(
                plan.theory.substringAfter(AcademicTime.PeriodSeparator).trim(),
                AcademicTime.PlanDate
            )
        }.getOrNull() ?: return null
        if (date < first || date > end) return null
        val week = (java.time.temporal.ChronoUnit.DAYS.between(first, date) / 7 + 1).toInt()
        return week.takeIf { it in 1..plan.weeks + 1 }?.coerceAtMost(plan.weeks)
    }

    fun controls(plan: Plan): Map<Int, List<StudyControl>> =
        plan.disciplines.flatMap { discipline ->
            discipline.events.filter { it.type != ControlType.Progress }
                .map { StudyControl(discipline = discipline, event = it) }
        }.groupBy { it.event.week }.toSortedMap()

    fun weekStart(plan: Plan, week: Int): LocalDate? = runCatching {
        LocalDate.parse(
            plan.theory.substringBefore(AcademicTime.PeriodSeparator).trim(),
            AcademicTime.PlanDate
        )
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).plusWeeks((week - 1).toLong())
    }.getOrNull()

    private val electivePrefixPattern = Regex("ДВ №\\s*\\d+", RegexOption.IGNORE_CASE)
    private val courseworkSuffixPattern = Regex("\\. Курсовая работа", RegexOption.IGNORE_CASE)
    private const val COURSEWORK_MARKER = "курсовая работа"
    private val titleAliases = mapOf("технологиипрограммирования" to "технологияпрограммирования")
}
