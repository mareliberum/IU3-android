package com.bmstu.iu3.features.search.ui.format

import com.bmstu.iu3.core.navigation.Screen

data class SearchSection(
    val screen: Screen,
    val title: String,
    val keywords: List<String>,
)
