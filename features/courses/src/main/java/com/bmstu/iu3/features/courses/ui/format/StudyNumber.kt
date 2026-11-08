package com.bmstu.iu3.features.courses.ui.format

import com.bmstu.iu3.features.courses.ui.components.russian
import java.text.NumberFormat

internal fun studyNumber(value: Double): String =
    NumberFormat.getNumberInstance(russian).apply { maximumFractionDigits = 1 }.format(value)
