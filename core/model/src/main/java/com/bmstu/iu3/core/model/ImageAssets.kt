package com.bmstu.iu3.core.model

object ImageAssets {
    const val BENTO_ACHIEVEMENTS = "BentoAchievements"
    const val BENTO_ADMISSIONS = "BentoAdmissions"
    const val BENTO_CONTACTS = "BentoContacts"
    const val BENTO_COURSES = "BentoCourses"
    const val BENTO_PROJECTS = "BentoProjects"
    const val BENTO_STUDY_PLAN = "BentoStudyPlan"
    const val BENTO_TEACHERS = "BentoTeachers"
    const val BENTO_TROPHY = "BentoTrophy"
    const val CONTACTS_BUILDING = "ContactsBuilding"
    const val COURSE_BOOK = "CourseBook"
    const val COURSE_CIRCUIT = "CourseCircuit"
    const val COURSE_CODE = "CourseCode"
    const val COURSE_DATABASE = "CourseDatabase"
    const val COURSE_DESIGN_METHODS = "CourseDesignMethods"
    const val COURSE_INFORMATICS = "CourseInformatics"
    const val COURSE_ROBOT = "CourseRobot"
    const val COURSE_ROUTER = "CourseRouter"
    const val ONBOARDING_SCHEDULE = "OnboardingSchedule"
    const val ONBOARDING_WELCOME = "OnboardingWelcome"
    const val PLACEHOLDER_FEMALE = "PlaceholderFemale"
    const val PLACEHOLDER_MALE = "PlaceholderMale"
    const val RECOMMEND_PLUS = "RecommendPlus"
    const val ROLE_APPLICANT = "RoleApplicant"
    const val ROLE_GRADUATE = "RoleGraduate"
    const val ROLE_STUDENT = "RoleStudent"
    const val ROLE_TEACHER = "RoleTeacher"
    const val STACK_AI = "StackAI"
    const val STACK_ELECTRONICS = "StackElectronics"
    const val STACK_IT = "StackIT"
    const val STACK_NETWORKS = "StackNetworks"
    const val STACK_PROGRAMMING = "StackProgramming"

    fun role(role: Role): String = when (role) {
        Role.Student -> ROLE_STUDENT
        Role.Teacher -> ROLE_TEACHER
        Role.Applicant -> ROLE_APPLICANT
        Role.Graduate -> ROLE_GRADUATE
    }
}
