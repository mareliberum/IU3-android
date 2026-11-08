package com.bmstu.iu3.core.data.source

internal enum class CatalogPayload(val file: String, val field: String? = null) {
    Contacts("contacts"),
    Staff("staff", "staff"),
    Courses("courses", "courses"),
    Schedule("schedule"),
    Laboratories("lab_schedule", "lessons"),
    StudyPlans("study_plans", "sheets"),
    Events("events"),
    FeaturedEvents("featured_events"),
    News("news"),
    Honor("honor"),
    Projects("projects"),
    Programs("programs"),
    Achievements("achievements"),
    Technologies("technologies"),
    Interests("research_interests", "interests"),
    Faculty("web_faculty"),
    Faces("faces"),
    Scholars("scholar_links", "teachers"),
    LegendWorks("legend_works"),
    Images("image_manifest");

    val assetPath: String get() = "$DIRECTORY/$file.$EXTENSION"

    private companion object {
        const val DIRECTORY = "responses"
        const val EXTENSION = "json"
    }
}
