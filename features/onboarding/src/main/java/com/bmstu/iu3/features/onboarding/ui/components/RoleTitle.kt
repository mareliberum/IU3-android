package com.bmstu.iu3.features.onboarding.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bmstu.iu3.core.model.Role
import com.bmstu.iu3.features.onboarding.R

@Composable
fun roleTitle(role: Role?): String = when (role) {
    Role.Student -> stringResource(R.string.onboarding_student)
    Role.Teacher -> stringResource(R.string.onboarding_teacher)
    Role.Applicant -> stringResource(R.string.onboarding_applicant)
    Role.Graduate -> stringResource(R.string.onboarding_graduate)
    null -> stringResource(R.string.onboarding_guest)
}
