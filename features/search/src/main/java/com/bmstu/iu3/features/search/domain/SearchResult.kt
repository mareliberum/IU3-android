package com.bmstu.iu3.features.search.domain


data class SearchResult(
    val kind: SearchResultKind,
    val id: String,
    val title: String,
    val subtitle: String = "",
    val semester: Int? = null,
)
