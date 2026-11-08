package com.bmstu.iu3.features.instructors.ui.format

import com.bmstu.iu3.core.model.Staff
import com.bmstu.iu3.core.model.StaffPortraits

internal fun Staff.image(): String = photo ?: StaffPortraits.fallback(name)
