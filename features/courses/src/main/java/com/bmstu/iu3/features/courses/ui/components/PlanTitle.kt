package com.bmstu.iu3.features.courses.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bmstu.iu3.core.model.StudyProgram
import com.bmstu.iu3.features.courses.R

@Composable
internal fun planTitle(program: StudyProgram): String = when (program) {
    StudyProgram.Bachelor -> stringResource(R.string.courses_bachelor_program)
    StudyProgram.InternationalBachelor -> stringResource(R.string.courses_international_program)
    StudyProgram.Master -> stringResource(R.string.courses_master_program)
    StudyProgram.GameDevelopment -> stringResource(R.string.courses_gamedev_program)
    else -> program.code
}
