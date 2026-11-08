package com.bmstu.iu3.features.search.domain

import com.bmstu.iu3.core.model.AcademicTime
import com.bmstu.iu3.core.model.Catalog
import com.bmstu.iu3.core.model.EventLogic
import com.bmstu.iu3.core.model.HonorKind
import com.bmstu.iu3.core.model.Profile
import com.bmstu.iu3.core.model.Role
import com.bmstu.iu3.core.model.StudyCalendar
import com.bmstu.iu3.core.model.StudyProgram
import java.time.LocalDate

object SearchEngine {
    private fun normalized(value: String) = value.lowercase().replace('ё', 'е').trim()
    fun search(
        catalog: Catalog,
        profile: Profile,
        query: String,
        today: LocalDate = LocalDate.now(AcademicTime.Zone),
        includeTransient: Boolean = true,
    ): List<SearchResult> {
        val q = normalized(query)
        if (q.isBlank()) return emptyList()
        fun matches(vararg values: String) = normalized(values.joinToString(" ")).contains(q)
        val result = mutableListOf<SearchResult>()
        catalog.staff.filter {
            matches(
                it.name,
                it.position,
                it.about.orEmpty(),
                catalog.interests[it.name].orEmpty().joinToString()
            )
        }
            .forEach {
                result += SearchResult(
                    kind = SearchResultKind.Instructor,
                    id = it.name,
                    title = it.name,
                    subtitle = it.position
                )
            }
        catalog.courses.filter { matches(it.title, it.description) }
            .forEach {
                result += SearchResult(
                    kind = SearchResultKind.Course,
                    id = it.id,
                    title = it.title,
                    subtitle = it.level.code
                )
            }
        catalog.projects.filter { matches(it.title, it.subtitle, it.details) }
            .forEach {
                result += SearchResult(
                    kind = SearchResultKind.Project,
                    id = it.title,
                    title = it.title,
                    subtitle = it.subtitle
                )
            }
        catalog.honor.filter { matches(it.name, it.subtitle) }.forEach {
            result += SearchResult(
                kind = if (it.kind == HonorKind.Legend) SearchResultKind.Legend else SearchResultKind.Graduate,
                id = it.name,
                title = it.name,
                subtitle = it.subtitle
            )
        }
        if (includeTransient) {
            EventLogic.upcoming(catalog.events, today).filter { event ->
                q.split(whitespacePattern).all { word ->
                    normalized(event.title + " " + event.location + " " + event.organizer + " " + event.tags.joinToString() + " " + event.description).contains(
                        word
                    )
                }
            }
                .take(6).forEach {
                    result += SearchResult(
                        kind = SearchResultKind.Event,
                        id = it.id,
                        title = it.title,
                        subtitle = it.location
                    )
                }
            (0..13).flatMap { offset ->
                val date = today.plusDays(offset.toLong())
                StudyCalendar.lessons(catalog, profile, date).filter { lesson ->
                    (date > today || runCatching {
                        java.time.LocalTime.parse(lesson.to) > java.time.LocalTime.now(
                            AcademicTime.Zone
                        )
                    }.getOrDefault(false)) && matches(
                        lesson.title,
                        lesson.teacher,
                        ROOM_SEARCH_PREFIX + lesson.room
                    )
                }
            }.distinctBy { listOf(it.day, it.from, it.title) }.take(10).forEach {
                result += SearchResult(
                    kind = SearchResultKind.Schedule,
                    id = listOf(it.day.toString(), it.from, it.title).joinToString(),
                    title = it.title,
                    subtitle = "${it.from} · ${it.teacher} · ${it.room}"
                )
            }
        }
        if (q.length >= 3) {
            catalog.plans.filter { it.program != StudyProgram.InternationalBachelor }
                .flatMap { plan ->
                    plan.disciplines.filter { matches(it.title) }
                        .map {
                            SearchResult(
                                kind = SearchResultKind.StudyPlan,
                                id = plan.program.code,
                                title = it.title,
                                subtitle = plan.semester.toString(),
                                semester = plan.semester
                            )
                        }
                }
                .distinctBy { it.title }.take(8).forEach { result += it }
            if (profile.role == Role.Applicant) catalog.programs.filter {
                matches(
                    it.title,
                    it.summary,
                    it.shortTitle
                )
            }
                .forEach {
                    result += SearchResult(
                        kind = SearchResultKind.AdmissionProgram,
                        id = it.id.code,
                        title = it.title,
                        subtitle = it.level
                    )
                }
        }
        return result
    }

    private val whitespacePattern = Regex("\\s+")
    private const val ROOM_SEARCH_PREFIX = "Ауд. "
}
