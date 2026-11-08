package com.bmstu.iu3.features.courses.presentation

import com.bmstu.iu3.core.navigation.RouteArguments
import dagger.assisted.AssistedFactory

@AssistedFactory
interface StudyPlanViewModelFactory {
    fun create(arguments: RouteArguments.StudyPlan): StudyPlanViewModel
}
