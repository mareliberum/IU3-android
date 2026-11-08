package com.bmstu.iu3.core.model

import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

object EventLogic {
    private val zone = AcademicTime.Zone
    fun start(event: FacultyEvent): OffsetDateTime =
        OffsetDateTime.parse(event.start).atZoneSameInstant(zone).toOffsetDateTime()

    fun end(event: FacultyEvent): OffsetDateTime =
        OffsetDateTime.parse(event.end ?: event.start).atZoneSameInstant(zone).toOffsetDateTime()

    fun visible(event: FacultyEvent, today: LocalDate = LocalDate.now(zone)): Boolean =
        end(event).atZoneSameInstant(zone).toLocalDate() >= today &&
            !restrictedAudiencePattern.containsMatchIn(
                event.title + " " + event.description.take(
                    300
                )
            )

    fun matches(event: FacultyEvent, query: String): Boolean {
        val text =
            (event.title + " " + event.location + " " + event.organizer + " " + event.tags.joinToString(
                " "
            ) + " " + event.description).lowercase().replace('ё', 'е')
        return query.trim().lowercase().replace('ё', 'е').split(whitespacePattern)
            .all(text::contains)
    }

    fun status(event: FacultyEvent, now: Instant = Instant.now()): EventStatus = when {
        now < start(event).toInstant() -> EventStatus.Upcoming
        now > (if (event.allDay) end(event).toLocalDate().plusDays(1).atStartOfDay(zone)
            .toInstant() else end(event).toInstant()) -> EventStatus.Finished

        else -> EventStatus.Ongoing
    }

    fun upcoming(
        events: List<FacultyEvent>,
        today: LocalDate = LocalDate.now(zone)
    ): List<FacultyEvent> =
        events.filter { visible(it, today) }.sortedWith(compareBy<FacultyEvent> { event ->
            val start = start(event).toLocalDate();
            val end = end(event).toLocalDate()
            when {
                start == today -> 0; start < today && end <= today.plusDays(7) -> 1; start > today -> 2; else -> 3
            }
        }.thenBy { event ->
            if (start(event).toLocalDate() < today && end(event).toLocalDate() <= today.plusDays(
                    7
                )
            ) end(event) else start(event)
        })

    fun featured(
        events: List<FacultyEvent>,
        role: Role?,
        today: LocalDate = LocalDate.now(zone),
    ): List<FacultyEvent> =
        upcoming(events, today).filter {
            ChronoUnit.DAYS.between(
                start(it).toLocalDate(),
                end(it).toLocalDate()
            ) <= 45
        }
            .sortedWith(compareByDescending<FacultyEvent> { score(it, role, today) }.thenBy {
                start(
                    it
                )
            }).take(6)

    private fun score(
        event: FacultyEvent,
        role: Role?,
        today: LocalDate,
    ): Double {
        val title = event.title.lowercase()
        val lead = (title + " " + event.description.take(400)).lowercase()
        val forApplicants =
            APPLICANT_TAG in event.tags || applicantAudiencePattern.containsMatchIn(
                lead
            )
        val audience = if (forApplicants) {
            if (role == Role.Applicant) 20 else -20
        } else 0
        val prominence = when {
            VOLLEYBALL_KEYWORD in lead && (SUPERLEAGUE_KEYWORD in lead || homeMatchPattern.containsMatchIn(
                title
            )) -> 25

            event.source == EventSource.Sports -> 0
            else -> (if (forumPattern.containsMatchIn(title)) 30
            else if (competitionPattern.containsMatchIn(title)) 15 else 0) +
                if (internationalPattern.containsMatchIn(title)) 10 else 0
        }
        val kind =
            if (digestPattern.containsMatchIn(title)) -60
            else (if (openCall.containsMatchIn(lead.take(title.length + 201))) -35 else 0) +
                if (remoteVenuePattern.containsMatchIn(title)) -20 else 0
        val engagement = event.engagement
        val reactions = engagement?.let { it.likes + 3.0 * it.reposts + it.views / 200.0 } ?: 0.0
        val popularity = minOf(20.0, 7 * kotlin.math.log10(1 + reactions))
        return urgency(
            event,
            today
        ) + popularity + audience + prominence + kind + if (event.source == EventSource.Featured) 10 else 0
    }

    private fun urgency(event: FacultyEvent, today: LocalDate): Double {
        val first = ChronoUnit.DAYS.between(today, start(event).toLocalDate()).toInt()
        val last = ChronoUnit.DAYS.between(today, end(event).toLocalDate()).toInt()
        val span = last - first
        val finished = if (event.allDay) end(event).toLocalDate().plusDays(1).atStartOfDay(zone)
            .toInstant() else end(event).toInstant()
        if (finished < Instant.now()) return -40.0
        if (span == 0) return dayValue(first)
        if (span > 14) return if (last <= 1) 50.0 else 15.0
        if (first > 0) return dayValue(first) - 20
        return when (last) {
            0 -> 70.0; 1 -> 65.0; else -> if (span <= 7) 60.0 else 35.0
        }
    }

    private fun dayValue(days: Int): Double = when {
        days <= 0 -> 100.0
        days == 1 -> 75.0
        days == 2 -> 60.0
        days <= 7 -> (50 - 2 * days).toDouble()
        else -> 20.0
    }

    private val openCall =
        Regex("при[её]м заявок|открыт набор|набирает|набор на|стипенди|вакансии|продление")

    private val restrictedAudiencePattern = Regex(
        "\\bвуц\\b|военн[а-я]* учебн",
        RegexOption.IGNORE_CASE
    )
    private val whitespacePattern = Regex("\\s+")
    private val applicantAudiencePattern =
        Regex("школьник|абитуриент|поступающ|\\d{1,2}\\s*[–-]\\s*\\d{1,2}\\s*класс")
    private val homeMatchPattern = Regex("(мгту|бауманк[аи])\\s*(vs|—|–|-)\\s|бауманка принимает")
    private val forumPattern = Regex("конгресс|форум|саммит")
    private val competitionPattern = Regex("конференци|хакатон|олимпиад|фестивал")
    private val internationalPattern = Regex("международн|всероссийск|всемирн")
    private val digestPattern = Regex("дайджест|пульс недели|анонсы недели|афиша недели")
    private val remoteVenuePattern = Regex("^в\\s+(?!бауманк|мгту|москв)[а-яё-]+\\s+пройд")
    private const val APPLICANT_TAG = "Поступающим"
    private const val VOLLEYBALL_KEYWORD = "волейбол"
    private const val SUPERLEAGUE_KEYWORD = "суперлиг"
    fun hasPhysicalLocation(event: FacultyEvent): Boolean =
        event.location.isNotBlank() && !event.location.equals(ONLINE_LOCATION, true)

    private const val ONLINE_LOCATION = "Онлайн"
}
