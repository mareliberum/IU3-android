package com.bmstu.iu3.app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Search
import com.bmstu.iu3.core.navigation.Screen

internal fun tabIcon(screen: Screen) = when (screen) {
    Screen.Home -> Icons.Filled.GridView
    Screen.Schedule -> Icons.Outlined.CalendarMonth
    Screen.Department -> Icons.Outlined.Computer
    Screen.Search -> Icons.Outlined.Search
    else -> Icons.Outlined.School
}
