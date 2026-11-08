package com.bmstu.iu3.features.search.data

import android.content.Context
import com.bmstu.iu3.core.navigation.Screen
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import com.bmstu.iu3.features.search.ui.format.SearchSection
import com.bmstu.iu3.features.search.R

internal class SearchLexicon @Inject constructor(@ApplicationContext context: Context) {
    private val resources = context.resources
    val sections = listOf(
        SearchSection(
            screen = Screen.StudyPlan,
            title = resources.getString(R.string.search_plan),
            keywords = resources.getStringArray(R.array.search_aliases_studyplan).toList()
        ),
        SearchSection(
            screen = Screen.Instructors,
            title = resources.getString(R.string.search_teachers),
            keywords = resources.getStringArray(R.array.search_aliases_instructors).toList()
        ),
        SearchSection(
            screen = Screen.Stacks,
            title = resources.getString(R.string.search_stacks),
            keywords = resources.getStringArray(R.array.search_aliases_stacks).toList()
        ),
        SearchSection(
            screen = Screen.Courses,
            title = resources.getString(R.string.search_courses),
            keywords = resources.getStringArray(R.array.search_aliases_courses).toList()
        ),
        SearchSection(
            screen = Screen.Honor,
            title = resources.getString(R.string.search_honor),
            keywords = resources.getStringArray(R.array.search_aliases_honor).toList()
        ),
        SearchSection(
            screen = Screen.Contacts,
            title = resources.getString(R.string.search_contacts),
            keywords = resources.getStringArray(R.array.search_aliases_contacts).toList()
        ),
        SearchSection(
            screen = Screen.Projects,
            title = resources.getString(R.string.search_projects),
            keywords = resources.getStringArray(R.array.search_aliases_projects).toList()
        ),
        SearchSection(
            screen = Screen.Achievements,
            title = resources.getString(R.string.search_achievements),
            keywords = resources.getStringArray(R.array.search_aliases_achievements).toList()
        ),
        SearchSection(
            screen = Screen.NewsList,
            title = resources.getString(R.string.search_news),
            keywords = resources.getStringArray(R.array.search_aliases_newslist).toList()
        ),
        SearchSection(
            screen = Screen.Events,
            title = resources.getString(R.string.search_events),
            keywords = resources.getStringArray(R.array.search_aliases_events).toList()
        ),
        SearchSection(
            screen = Screen.Admissions,
            title = resources.getString(R.string.search_admissions),
            keywords = resources.getStringArray(R.array.search_aliases_admissions).toList()
        )
    )
    val popularPool = resources.getStringArray(R.array.search_popular_pool).toList()
    val applicantQueries = resources.getStringArray(R.array.search_applicant_queries).toSet()
    val popularTeacher = resources.getString(R.string.search_popular_teacher)
}
