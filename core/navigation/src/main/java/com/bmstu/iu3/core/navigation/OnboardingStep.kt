package com.bmstu.iu3.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class OnboardingStep(val index: Int) : NavKey
