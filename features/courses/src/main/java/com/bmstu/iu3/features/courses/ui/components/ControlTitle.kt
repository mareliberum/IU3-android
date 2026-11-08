package com.bmstu.iu3.features.courses.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bmstu.iu3.core.model.ControlType
import com.bmstu.iu3.features.courses.R

@Composable
internal fun controlTitle(type: ControlType): String = when (type) {
    ControlType.Homework -> stringResource(R.string.courses_control_homework)
    ControlType.Checkpoint -> stringResource(R.string.courses_control_checkpoint)
    ControlType.Module -> stringResource(R.string.courses_control_module)
    ControlType.Test -> stringResource(R.string.courses_control_test)
    ControlType.CourseProject -> stringResource(R.string.courses_control_project)
    ControlType.Progress -> stringResource(R.string.courses_control_progress)
    else -> type.code
}
