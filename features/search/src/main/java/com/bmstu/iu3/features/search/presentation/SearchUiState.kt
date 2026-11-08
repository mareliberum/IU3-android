package com.bmstu.iu3.features.search.presentation

import com.bmstu.iu3.features.search.domain.SearchResult

data class SearchUiState(
    val query: String = "",
    val recent: List<String> = emptyList(),
    val popular: List<String> = emptyList(),
    val results: List<SearchResult> = emptyList(),
    val sections: List<Pair<com.bmstu.iu3.core.navigation.Screen, String>> = emptyList(),
)
