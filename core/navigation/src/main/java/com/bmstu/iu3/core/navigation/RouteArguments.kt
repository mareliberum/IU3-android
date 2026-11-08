package com.bmstu.iu3.core.navigation

import com.bmstu.iu3.core.model.AdmissionProgramId
import com.bmstu.iu3.core.model.HonorKind
import com.bmstu.iu3.core.model.TechnologyStack
import kotlinx.serialization.Serializable

@Serializable
sealed interface RouteArguments {
    @Serializable
    data object None : RouteArguments

    @Serializable
    data class Courses(val stack: TechnologyStack? = null, val technology: String = "") :
        RouteArguments

    @Serializable
    data class StudyPlan(
        val program: com.bmstu.iu3.core.model.StudyProgram? = null,
        val semester: Int? = null
    ) : RouteArguments

    @Serializable
    data class HonorList(val kind: HonorKind) : RouteArguments

    @Serializable
    data class Admission(val program: AdmissionProgramId) : RouteArguments
}
