package com.bmstu.iu3.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class Route(
    val screen: Screen,
    val id: String = "",
    val arguments: RouteArguments = RouteArguments.None,
    val instance: String = java.util.UUID.randomUUID().toString(),
) : NavKey
