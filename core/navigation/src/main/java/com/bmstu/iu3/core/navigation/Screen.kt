package com.bmstu.iu3.core.navigation

import kotlinx.serialization.Serializable

@Serializable
enum class Screen {
    Home, Schedule, Department, Search, Admissions, Alumni,
    Events, Event, NewsList, News, Projects, Project, Achievements,
    Instructors, Instructor, Stacks, Courses, Course, StudyPlan,
    Honor, HonorList, Legend, Graduate, Recommendation, Program, Contacts, Profile, Notifications,
}
