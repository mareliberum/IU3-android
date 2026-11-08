package com.bmstu.iu3.features.courses.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.bmstu.iu3.core.designsystem.AppBlack
import com.bmstu.iu3.core.designsystem.AppBlue
import com.bmstu.iu3.core.designsystem.AppControlGreen
import com.bmstu.iu3.core.designsystem.AppControlNeutral
import com.bmstu.iu3.core.designsystem.AppControlOrange
import com.bmstu.iu3.core.model.ControlType

@Composable
internal fun controlColor(type: ControlType): Color = when (type) {
    ControlType.Module -> AppBlue
    ControlType.Checkpoint -> AppBlack
    ControlType.Test -> AppControlOrange
    ControlType.Progress -> AppControlNeutral
    else -> AppControlGreen
}
