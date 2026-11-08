package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Catalog(
    val contacts: DepartmentContacts,
    val staff: List<Staff>,
    val courses: List<Course>,
    val timetable: Timetable,
    val labs: List<Lesson>,
    val plans: List<Plan>,
    val events: List<FacultyEvent>,
    val news: List<News> = emptyList(),
    val honor: List<Honor>,
    val projects: List<Project>,
    val programs: List<AdmissionProgram>,
    val achievements: List<Achievement>,
    val technologies: Map<TechnologyStack, List<Technology>>,
    val interests: Map<String, List<String>>,
    val webFaculty: Map<String, FacultyDetail> = emptyMap(),
    val faces: Map<String, FaceFrame> = emptyMap(),
    val scholars: Map<String, ScholarEntry>,
    val legendWorks: Map<String, List<Publication>>,
    val images: Map<String, String>,
)
