package com.bmstu.iu3.core.model

object StaffAssignments {
    private const val HEAD_POSITION = "заведующий кафедрой"
    private val DEPUTY_POSITIONS = listOf("заместитель заведующего", "зам. зав.")
    private const val LAB_HEAD_POSITION = "Заведующий лабораторией"
    private const val HEAD_SURNAME = "Алфимцев"
    private const val EDUCATION_DEPUTY_SURNAME = "Тихомирова"
    private const val RESEARCH_DEPUTY_SURNAME = "Локтев"
    private const val CURATOR_SURNAME = "Питикин"
    private const val NON_TEACHING_SURNAME = "Фомченко"
    private val CONTACT_ORDER =
        listOf(HEAD_SURNAME, EDUCATION_DEPUTY_SURNAME, RESEARCH_DEPUTY_SURNAME, CURATOR_SURNAME)

    fun isHead(person: Staff): Boolean = person.position.contains(HEAD_POSITION, true)
    fun isDeputy(person: Staff): Boolean =
        DEPUTY_POSITIONS.any { person.position.contains(it, true) }

    fun isEducationDeputy(person: Staff): Boolean = person.name.startsWith(EDUCATION_DEPUTY_SURNAME)
    fun isCurator(person: Staff): Boolean = person.name.startsWith(CURATOR_SURNAME)
    fun isTeaching(person: Staff): Boolean =
        !person.position.startsWith(LAB_HEAD_POSITION, true) && !person.name.startsWith(
            NON_TEACHING_SURNAME
        )

    fun contacts(staff: List<Staff>): List<Staff> =
        CONTACT_ORDER.mapNotNull { surname -> staff.firstOrNull { it.name.startsWith(surname) } }

    fun contactEmail(person: Staff, department: DepartmentContacts): String? =
        if (person.name.startsWith(HEAD_SURNAME)) department.email else person.email
}
