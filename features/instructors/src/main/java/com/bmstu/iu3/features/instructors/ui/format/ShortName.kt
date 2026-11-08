package com.bmstu.iu3.features.instructors.ui.format

import com.bmstu.iu3.core.model.Staff

internal fun Staff.shortName() =
    name.substringBefore(' ') + " " + name.substringAfter(' ', "").split(' ')
        .filter(String::isNotBlank).joinToString(" ") { it.take(1) + "." }
