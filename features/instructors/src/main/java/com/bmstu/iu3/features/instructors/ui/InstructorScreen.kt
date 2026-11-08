package com.bmstu.iu3.features.instructors.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bmstu.iu3.core.designsystem.AppGray
import com.bmstu.iu3.core.designsystem.EmptyState
import com.bmstu.iu3.core.designsystem.FacultyImage
import com.bmstu.iu3.core.designsystem.ItemCard
import com.bmstu.iu3.core.designsystem.Page
import com.bmstu.iu3.core.designsystem.Section
import com.bmstu.iu3.core.designsystem.WeekStrip
import com.bmstu.iu3.core.designsystem.contentKey
import com.bmstu.iu3.core.designsystem.educationLevelTitle
import com.bmstu.iu3.core.model.Course
import com.bmstu.iu3.core.model.Lesson
import com.bmstu.iu3.core.model.Publication
import com.bmstu.iu3.core.model.ScholarProfile
import com.bmstu.iu3.core.model.ScholarProfileKind
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.core.navigation.external.ExternalUris
import com.bmstu.iu3.features.instructors.R
import com.bmstu.iu3.features.instructors.presentation.InstructorDetailUiState
import com.bmstu.iu3.features.instructors.ui.components.websiteTitle
import com.bmstu.iu3.features.instructors.ui.format.image
import com.bmstu.iu3.features.instructors.ui.state.InstructorTab
import java.time.LocalDate

@Composable
fun InstructorScreen(
    state: InstructorDetailUiState,
    lessonsFor: (LocalDate) -> List<Lesson>,
    back: () -> Unit,
    navigate: (Route) -> Unit,
    open: (String) -> Unit,
) {
    val person = state.person
    val works = state.works
    val profiles = state.profiles
    val teaching = state.teaching
    var tab by rememberSaveable(person.name) { mutableStateOf(InstructorTab.Contacts) }
    val tabs = listOf(
        InstructorTab.Contacts to stringResource(R.string.instructors_contacts),
        InstructorTab.Biography to stringResource(R.string.instructors_bio)
    ) +
        (if (teaching) listOf(
            InstructorTab.Courses to stringResource(R.string.instructors_courses),
            InstructorTab.Schedule to stringResource(R.string.instructors_schedule)
        ) else emptyList()) +
        (if (works.isNotEmpty() || profiles.any { it.kind == ScholarProfileKind.ResearchGate }) listOf(
            InstructorTab.Works to stringResource(
                R.string.instructors_works
            )
        ) else emptyList())
    var dateString by rememberSaveable(person.name) { mutableStateOf(state.initialDate.toString()) }
    val date = LocalDate.parse(dateString)
    Page(title = stringResource(R.string.instructors_teacher), back = back) {
        item {
            Row(
                Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEach { (key, label) ->
                    FilterChip(
                        tab == key,
                        { tab = key },
                        { Text(label) })
                }
            }
        }
        item {
            Text(
                person.name,
                style = MaterialTheme.typography.headlineMedium
            ); Text(person.position, color = AppGray)
        }
        when (tab) {
            InstructorTab.Contacts -> {
                item {
                    FacultyImage(
                        name = person.image(),
                        modifier = Modifier.fillMaxWidth().height(300.dp)
                    )
                }
                person.email?.let { email ->
                    item {
                        TextButton({ open(ExternalUris.email(email)) }) {
                            Text(
                                email
                            )
                        }
                    }
                }
                person.phone?.let { phone ->
                    item {
                        TextButton({ open(ExternalUris.phone(phone)) }) {
                            Text(
                                phone
                            )
                        }
                    }
                }
                items(
                    state.websites,
                    key = { contentKey<String>(id = it) },
                    contentType = { String::class }) { url ->
                    val label = websiteTitle(url = url)
                    TextButton({ open(url) }) { Text(label) }
                }
            }

            InstructorTab.Biography -> {
                item {
                    Section(title = stringResource(R.string.instructors_info))
                    Text(
                        state.biography
                            ?: stringResource(R.string.instructors_default_bio)
                    )
                }
                val interests = state.interests
                if (interests.isNotEmpty()) item {
                    Section(title = stringResource(R.string.instructors_interests_a919a)); Text(
                    interests.joinToString("\n") { it.replaceFirstChar(Char::titlecase) })
                }
            }

            InstructorTab.Courses -> {
                val courses = state.courses
                if (courses.isEmpty()) item { EmptyState(title = stringResource(R.string.instructors_no_courses)) }
                items(
                    courses,
                    key = { contentKey<Course>(id = it.id) },
                    contentType = { Course::class }) { course ->
                    ItemCard(
                        course.title,
                        educationLevelTitle(level = course.level),
                        course.image,
                        onClick = { navigate(Route(screen = Screen.Course, id = course.id)) })
                }
            }

            InstructorTab.Schedule -> {
                item {
                    WeekStrip(
                        date = date, select = { dateString = it.toString() },
                        hasLessons = {
                            lessonsFor(it).isNotEmpty()
                        })
                }
                val lessons = lessonsFor(date)
                if (lessons.isEmpty()) item { EmptyState(title = stringResource(R.string.instructors_no_lessons)) }
                items(
                    lessons,
                    key = {
                        contentKey<Lesson>(
                            id = listOf(
                                it.day.toString(),
                                it.from,
                                it.title,
                                it.room,
                                it.week.code,
                                it.groups.joinToString()
                            ).joinToString()
                        )
                    },
                    contentType = { Lesson::class }) { lesson ->
                    ItemCard(
                        title = lesson.title,
                        subtitle = "${lesson.from}–${lesson.to} · ${lesson.room}\n${lesson.groups.joinToString()}"
                    )
                }
            }

            InstructorTab.Works -> {
                items(
                    profiles,
                    key = { contentKey<ScholarProfile>(id = it.kind.code + it.url) },
                    contentType = { ScholarProfile::class }) { link ->
                    TextButton({ open(link.url) }) { Text(link.kind.code.replaceFirstChar(Char::uppercase)) }
                }
                items(
                    works,
                    key = { contentKey<Publication>(id = it.title + it.year) },
                    contentType = { Publication::class }) { work ->
                    ItemCard(
                        work.title,
                        work.year.toString(),
                        onClick = {
                            open(
                                work.url
                                    ?: ExternalUris.scholar("\"${work.title}\"")
                            )
                        })
                }
            }
        }
    }
}
